package com.mile.mile_navigation_engine.Navigation.service;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.ui.v5.voice.NavigationSpeechPlayer;
import com.mapbox.services.android.navigation.ui.v5.voice.SpeechAnnouncement;
import com.mapbox.services.android.navigation.ui.v5.voice.SpeechPlayerProvider;
import com.mapbox.services.android.navigation.ui.v5.voice.VoiceInstructionLoader;
import com.mile.mile_navigation_engine.Navigation.ApplicationRunner;
import com.mile.mile_navigation_engine.interfaces.MP3Listener;
import com.mile.mile_navigation_engine.interfaces.interfaceAudioService;
import com.mile.mile_navigation_engine.model.POI;
import com.mile.mile_navigation_engine.utils.MapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import okhttp3.Cache;
import timber.log.Timber;

import static android.content.Context.AUDIO_SERVICE;

public class AudioService extends UtteranceProgressListener implements AudioManager.OnAudioFocusChangeListener{

    static private AudioService INSTANCE = new AudioService();
    private String COMPONENT_NAVIGATION_INSTRUCTION_CACHE =
            "component-navigation-instruction-cache";
    private Long TEN_MEGABYTE_CACHE_SIZE = (long) (10 * 1024 * 1024);

    private Context context;
    private TextToSpeech                        currentSynth;
    private ArrayList<String> arrayStringPOIQueue         = new ArrayList<String>();
    private boolean isSynthReady = false;
    private boolean                             bAskedForFlush              = false;
    private boolean                             hasReadEnding               = false;
    private float                               defaultSpeechRate           = 0.95f;
    private float currentSpeechRate = 0.95f;
    private int                                 pauseBetweenSentences       = 100;
    private interfaceAudioService myAudioServiceListener, poiListener = null;
    private AudioManager                        am;
    private Voice female;
    private Voice                               male;
    private MediaPlayer mediaPlayer;
    private boolean                             isReading = false;
    private MediaPlayer mPlayer = new MediaPlayer();
    private Handler playerHandler;
    private Boolean isMP3PlayingPending = false;
    public POI currentPOI = null;

    private NavigationSpeechPlayer speechPlayer = null;

    public enum AudioContent {
        start_navigation,
        resume_navigation,
        pause_navigation
    }

    public enum VoiceGender {
        MALE,
        FEMALE
    }

    private AudioService(){

    }

    public void init(Context ctx, Application app){
        this.context = ctx;
        arrayStringPOIQueue.clear();
        male = null; // we reinit male and female voice selection in case of language change
        female = null;
        currentSynth = new TextToSpeech(ctx, status -> initTTS(status),"com.google.android.tts");
        mPlayer = new MediaPlayer();

        initializeSpeechPlayer(app);
    }

    public void initTTS(int status){
        Locale TTSLanguage = null;

        if(status == TextToSpeech.SUCCESS){

            currentSynth.setSpeechRate(defaultSpeechRate);

            switch (ApplicationRunner.appLanguage) {
                case FR:
                    TTSLanguage = Locale.FRENCH;
                    currentSynth    .setSpeechRate(1.2f);
                    currentSpeechRate = 1.2f;
                    break;
                default:
                    TTSLanguage = Locale.ENGLISH;
                    currentSynth    .setSpeechRate(1.3f);
                    currentSpeechRate = 1.3f;
            }

            //We set some default parameters for the synthesizer
            int result = currentSynth.setLanguage(TTSLanguage);

            if (result == TextToSpeech.LANG_MISSING_DATA) { //j'ai supprimé le check LANG_NOT_SUPPORTED car il arrive que la langue soit bien installée et pourtant considérée comme absente.
                Log.e("TTS", "This Language is not available, attempting download");
                myAudioServiceListener.alertTTSNotInstalled();

            }else{
                selectVoices(TTSLanguage);
                //We set up the TextToSpeech listeners
                currentSynth.setOnUtteranceProgressListener(AudioService.this);
                isSynthReady = true;
            }
        }else{
            if (status == TextToSpeech.ERROR_NOT_INSTALLED_YET){
                myAudioServiceListener.alertTTSNotInstalled();
            }
            //TODO: give error back to NavigationActivity to warn the user.
            isSynthReady = false;
        }
    }

    private void initializeSpeechPlayer(Application app) {
        String appLanguage = ApplicationRunner.appLanguage.getLocaleLanguage();
        Cache cache = new Cache(
                new File(app.getCacheDir(), COMPONENT_NAVIGATION_INSTRUCTION_CACHE),
                TEN_MEGABYTE_CACHE_SIZE
        );
        VoiceInstructionLoader voiceInstructionLoader = new VoiceInstructionLoader(
                app,
                Mapbox.getAccessToken(), cache
        );
        SpeechPlayerProvider speechPlayerProvider = new SpeechPlayerProvider(
                app, appLanguage, true,
                voiceInstructionLoader
        );
        speechPlayer = new NavigationSpeechPlayer(speechPlayerProvider);
    }

    public void selectVoices(Locale locale){
        if (Build.VERSION.SDK_INT >= 21){
            //We are going to select two voices reference for a female and male voice.
            Set<Voice> map =  currentSynth.getVoices();
            if (map == null) return; //if no voice is selected, this will use default package.
            for (Voice voice : map){
                //For some languages, we can use a male and female voices. If no voices are configured, TTS will used default language for current language.
                if (voice.getLocale().getLanguage().equals(locale.getLanguage()) && !voice.isNetworkConnectionRequired()){

                    switch (voice.getName()){
                        case "fr-fr-x-vlf-local":
                            female = voice;
                            break;
                        case "fr-fr-x-vlf#male_3-local":
                            male = voice;
                            break;
                        case "en-us-x-sfg-local":
                            female = voice;
                            break;
                        case "en-us-x-sfg#male_1-local":
                            male = voice;
                            break;
                        default:
                            break;
                    }
                }
            }
            //Now we check if data for this language is installed. Otherwise, if user is offline, no TTS will be listend
            //We test only female because if an error occured on male then it would also on female

            if (female == null) return;
            for (Iterator<String> it = female.getFeatures().iterator(); it.hasNext(); ) {
                String s = it.next();
                if (s.equals("notInstalled")){
                    if (female.isNetworkConnectionRequired()) myAudioServiceListener.alertTTSNotInstalled(); //le TTS peut ne pas être installé mais ne pas demander de connexion internet. Souvent c'est un package en basse qualité qui est utilisé.
                }
            }
        }
    }

    public void play(String phrase, boolean isInstruction, boolean priority, final VoiceGender voiceGender){
        if (phrase == null)
            return;

        // In case mp3 description is being played, we need to pause it
        if(isInstruction && mPlayer != null && mPlayer.isPlaying()){
            pauseDescriptionMP3(true);
        }

        isReading = true;
        final String stringToPlay = correctSpellingFrom(phrase);
        //Before playing anything, we have to check that the TTS player is ready and allowed to play
        if (isSynthReady) {

            am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
            int requestResult = am.requestAudioFocus(
                    this, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

            if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                        String.valueOf(AudioManager.STREAM_NOTIFICATION));

                //If the String is a POI, it has be reading after the other instructions
                //If the String is a Direction, it has to be read in priority

                if (priority && isInstruction) {
                    //DIRECTION
                    bAskedForFlush = true;
                    Timber.d("audio new string flush : %s", stringToPlay);
                    try {
                        Thread.sleep(1000);
                        speak(stringToPlay, TextToSpeech.QUEUE_FLUSH, voiceGender, true);
                        speechPlayer.play(SpeechAnnouncement.builder().announcement(stringToPlay).build());
                        //The utterance was interrupted
                        //We need to track the point where the utterances were flushed
                        //If code asked to replayStoppedUtterance, we will keep in memory stopped utterance :
                        for (int i = 0; i < arrayStringPOIQueue.size(); i++) {
                            playTTS(arrayStringPOIQueue.get(i), true, false);
                        }
                        bAskedForFlush = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    //POI
                    String[] splittedSpeech = stringToPlay.split("\\."); // We get sentences
                    for (String s : splittedSpeech) {
                        String wordToPlay = s.trim();
                        speak(wordToPlay, TextToSpeech.QUEUE_ADD, voiceGender, false);

                        // The play silent utterance differs depending the build bersion of the SDK
                        currentSynth.playSilentUtterance(pauseBetweenSentences, TextToSpeech.QUEUE_ADD, wordToPlay);

                        if (!arrayStringPOIQueue.contains(wordToPlay)) {
                            arrayStringPOIQueue.add(wordToPlay);
                        }
                    }
                }

            }
        }
    }

    /**
     * Triggers MP3 description player
     * @param poi
     * @param mp3Listener
     */
    public void playDescriptionMP3(POI poi, final MP3Listener mp3Listener){
        currentPOI = poi;
        try{
            if(mPlayer != null && mPlayer.isPlaying()){
                mPlayer.stop();
                mPlayer.release();
            }
            mPlayer = MediaPlayer.create(this.context, this.context.getResources().getIdentifier(poi.getMp3FileString(),
                    "raw", this.context.getPackageName()));
            mPlayer.start();
        }catch(Exception e){
            Timber.e(e.getLocalizedMessage());
        }

        mp3Listener.initMP3Duration(mPlayer.getDuration());
        Log.d("playMP3 duration", String.valueOf(mPlayer.getDuration()));

        playerHandler = new Handler(Looper.getMainLooper());

        playerHandler.post(new Runnable() {
            public void run() {
                int currentDuration;
                if (mPlayer != null && mPlayer.isPlaying()) {
                    currentDuration = mPlayer.getCurrentPosition();
                    mp3Listener.onListeningProgress(mPlayer.getDuration(), currentDuration);
                    playerHandler.postDelayed(this, 1000);
                    Log.d("playDescriptionMP3", String.valueOf(mPlayer.getCurrentPosition()));
                }else {
                    playerHandler.removeCallbacks(this);
                    mp3Listener.onListeningFinished();
                }
            }
        });
    }

    public boolean isPOIBeingRead(POI poi){
        if(currentPOI != null){
            return poi.getId().equals(currentPOI.getId());
        }else return false;
    }
    /**
     * Manages state of current media player
     * @param isPaused
     */
    public void pauseDescriptionMP3(Boolean isPaused) {
        if(isPaused){
            mPlayer.pause();
            isMP3PlayingPending = true;

            Timber.d("audio media player paused");
        }
        else{
            mPlayer.start();
            isMP3PlayingPending = false;
            Timber.d("audio media player play");
        }
    }

    public void stop(){
        stopAudioService(true);
    }

    //OVERLOAD METHOD
    //Used not to specified voicegender everytime when unecessary
    //In general cases, priority phrase are instruction and spoken with male voice. Else female voice is for POI, Smart Facts...
    public void playTTS(String phrase, boolean isInstruction, boolean priority) {
        if (priority) play(phrase, isInstruction, priority, VoiceGender.FEMALE);
        else play(phrase, isInstruction, priority, VoiceGender.FEMALE);
    }

    public void playTTS(SpeechAnnouncement announcement, boolean isInstruction) {
        if(isInstruction)
            play(announcement.announcement(), true, true, VoiceGender.FEMALE);
        else
            play(announcement.announcement(), false, false, VoiceGender.FEMALE);
    }

    private boolean removeAudioFocus() {
        am = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                am.abandonAudioFocus(this);
    }

    public void stopAudioService(boolean withReInit) {
        arrayStringPOIQueue.clear();

        // Managing MediaPlayer
        if(mPlayer != null && mPlayer.isPlaying())
            mPlayer.stop();

        if(mPlayer != null)
            mPlayer.release();

        // Managing synth
        if (isSynthReady) {
            currentSynth.stop();
            currentSynth.shutdown();

            if (withReInit) {
                currentSynth = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        initTTS(status);
                    }
                }, "com.google.android.tts");
            }else{
                removeAudioFocus();
            }
        }

        mPlayer = null;
        isReading = false;
    }

    /**
     * Methods used to trigger Android embedded synthesizer to play string
     * @param text
     * @param QUEUE_PROPERTIES
     * @param withVoiceGender
     * @param parallelOfMapbox
     */
    public void speak(String text, int QUEUE_PROPERTIES, VoiceGender withVoiceGender, boolean parallelOfMapbox) {
        isReading = true;

        // We check the number version of the SDK
        // The speaking methods will depends on the build version of the SDK

        if (withVoiceGender != null){
            Voice voice;
            if (withVoiceGender == VoiceGender.MALE) {
                voice = male;
            } else {
                voice = female;
            }
            if (voice != null) currentSynth.setVoice(voice);
        }

        HashMap<String, String> params = new HashMap<>();
        Bundle paramsBundled = new Bundle();
        if(parallelOfMapbox){
            currentSynth.setSpeechRate(defaultSpeechRate);
            params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "0");
            paramsBundled.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 0f);
        }else{
            currentSynth.setSpeechRate(currentSpeechRate);
            params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1");
            paramsBundled.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1f);
        }
        Timber.d("audio reading %s", text);
        if(text == null || text.length() < 3)
            text = "Directionnel";
        //currentSynth.speak(timerText, QUEUE_PROPERTIES, params);
        currentSynth.speak(text, QUEUE_PROPERTIES, paramsBundled, text.subSequence(0,2).toString());
        //currentSynth.playSilentUtterance(pauseBetweenSentences, TextToSpeech.QUEUE_ADD, timerText);
    }


    /*********************************/
    /********* TTS Listeners *********/
    /*********************************/

    /**
     * When TTS finished to speak
     * @param utteranceId
     */
    @Override public void onDone(String utteranceId){
        Timber.d("audio synth finished reading");
        if (!bAskedForFlush) {
            boolean askForUnduck = true;
            arrayStringPOIQueue.remove(utteranceId);
            if (!arrayStringPOIQueue.isEmpty()) {
                askForUnduck = false;
            }

            if (askForUnduck) {
                am.abandonAudioFocus(this);
            }
        }

        Timber.d("audio synth finished MP3 is %s", isMP3PlayingPending);
        if(isMP3PlayingPending){
            try {
                Thread.sleep(1000);
                pauseDescriptionMP3(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(poiListener != null)
            poiListener.launchSmartRun();

        isReading = false;
    }

    @Override public void onError(String utteranceId){
        System.out.print("onFirebaseError");
    }
    @Override public void onStart(String utteranceId){
        boolean isReadingPOIorPrio = false;         //If the user is reading a POI or a prioPOI we show a stop button

        if (arrayStringPOIQueue.contains(utteranceId)) {
            isReadingPOIorPrio = true;
        }
        isReading = true;
    }
    @Override public void onStop(String utteranceId, boolean interrupted){
        if (bAskedForFlush == false) {
            boolean askForUnduck = true;
            arrayStringPOIQueue.remove(utteranceId);
            if (arrayStringPOIQueue.isEmpty() == false) {
                askForUnduck = false;
            }

            if (askForUnduck) {
                am.abandonAudioFocus(this);
            }
        }

        isReading = false;
    }

    /**
     * Used to remove words that speaker will failed to say
     * @param utterance
     * @return
     */
    private String correctSpellingFrom(String utterance){
        String newUtterance;
        //Used to change pronouciation of TTS by changing spelling
        switch (ApplicationRunner.appLanguage){
            case FR:
                newUtterance = utterance.replace("-vous"," vous"); //Si précédé d'un -, le vous est lu vousse. Exemple : le saviez-vousse ?
                //newUtterance = newUtterance.replace("allée","chemin");
                //newUtterance = newUtterance.replace("Allée","chemin");
                newUtterance = newUtterance.replace("sens","sence");
                break;
            default:
                newUtterance = utterance;
                break;
        }

        return newUtterance;
    }

    public void onAudioFocusChange(int focusChange) {
        // AudioFocus is a new feature: focus updates are made verbose on
        // purpose
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                break;
            default:
        }
    }

    /**
     * Generate timerText that speaker will read
     * @param previousPosition
     * @param currentPosition
     * @param poi
     * @return
     */
    public String generateAudioDescriptionForPOI(Location previousPosition, Location currentPosition, POI poi){
        //Create the sentence to be spoken by the AudioGuide. This will be the POI description or AutoDirection + Description
        //If POI description is not retrieved with TTS language parameter, then we don't read description
        if (previousPosition != null && currentPosition != null ) {
            //We check if the POI is to the left or to the right to the user
            final String direction = MapUtils.Companion.getFacingDirection(previousPosition, currentPosition, poi);
            final String speakString = direction.concat("\n").concat(poi.getDescription()); //To add \n help synthesizer to pause voice between direction and description.

            return speakString;
            //Now we can read the description of the POI
        }else{
            return "";
        }
    }

    static public AudioService getInstance(){
        return INSTANCE;
    }

    public void setPoiListener(interfaceAudioService poiListener) {
        this.poiListener = poiListener;
    }

    public boolean isReading() {
        return isReading;
    }

    public void setReading(boolean reading) {
        isReading = reading;
    }
}
