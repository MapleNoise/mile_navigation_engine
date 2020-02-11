//
//  NavigationRouteView+Functions.swift
//  MyThalassa
//
//  Created by Hassine on 18/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import CoreLocation
import MapboxSpeech
import AVKit

extension NavigationRouteView
{
    
    func playSynthesizerPoiDescription(description:String) -> Void {
        
        let speechSynthesizer = SpeechSynthesizer.shared
        let listText = splitedString(string: description, length: 450)
        
        
        for (index,item) in listText.enumerated()
        {
            // queue.async {
            
            //semaphore.wait()
            print("pt : E")
            let options = SpeechOptions(text:  item)
            options.speechGender = .female
            options.locale = Locale.init(identifier: Utils.getLanguageAndCountry())
            speechSynthesizer.audioData(with: options) { (data: Data?, error: NSError?) in
                guard error == nil else {
                    print("Error speechSynthesizer : \(error!)")
                    PlayerManager.shared.playSynt(description: description)
                    return
                }
                self.listAudio.append(data!)
                var asset : AVAsset!
                if (index == 0)
                {
                    asset = data!.getAVAsset(str: "pause")
                }
                else
                {
                    asset = data!.getAVAsset(str: "")
                }
                
                
                var playerItem = AVPlayerItem.init(asset: asset)
                
                // playerQueue.play()
                
                if  PlayerManager.shared.audioPlayerPoiDescriptionQueue   == nil
                {
                    PlayerManager.shared.audioPlayerPoiDescriptionQueue = AVQueuePlayer.init(playerItem: playerItem)
                    NotificationCenter.default.addObserver(self, selector: #selector(self.playerEndedPlaying), name: Notification.Name("AVPlayerItemDidPlayToEndTimeNotification"), object: nil)
                    PlayerManager.shared.audioPlayerPoiDescriptionQueue!.play()
                }
                else
                {
                    PlayerManager.shared.audioPlayerPoiDescriptionQueue?.insert(playerItem, after: PlayerManager.shared.audioPlayerPoiDescriptionQueue?.items().last)
                    PlayerManager.shared.audioPlayerPoiDescriptionQueue!.play()
                }
                
            }
            
        }
        // }
    }
    
    
    
    
    
    @objc func playerEndedPlaying(_ notification: Notification) {
        var url = (PlayerManager.shared.audioPlayerPoiDescriptionQueue?.currentItem?.asset as! AVURLAsset).url
        PlayerManager.shared.audioPlayerPoiDescriptionQueue?.pause()
        print()
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {[weak self] in
            PlayerManager.shared.audioPlayerPoiDescriptionQueue?.play()
        }
    }
    
    func playSynthesizerDirectionnal(description:String) -> Void {
        
        
        
        
        
        
        
        
        
        
        
        let speechSynthesizer = SpeechSynthesizer.shared
        let listText = splitedString(string: description, length: 450)
        
        for (index,item) in listText.enumerated()
        {
            
            
            let options = SpeechOptions(text:  item)
            options.speechGender = .female
            options.locale = Locale.init(identifier: Utils.getLanguageAndCountry())
            speechSynthesizer.audioData(with: options) { (data: Data?, error: NSError?) in
                guard error == nil else {
                    print("Error speechSynthesizer : \(error!)")
                    PlayerManager.shared.playSynt(description: description)
                    return
                }
                self.listAudio.insert(data!, at: 0)
                var asset : AVAsset!
                if (index == 0)
                {
                    asset = data!.getAVAsset(str: "pause")
                }
                else
                {
                    asset = data!.getAVAsset(str: "")
                }
                
                
                var playerItem = AVPlayerItem.init(asset: asset)
                
                
                
                if  PlayerManager.shared.audioPlayerPoiDescriptionQueue   == nil
                {
                    PlayerManager.shared.audioPlayerPoiDescriptionQueue = AVQueuePlayer.init(playerItem: playerItem)
                    NotificationCenter.default.addObserver(self, selector: #selector(self.playerEndedPlaying), name: Notification.Name("AVPlayerItemDidPlayToEndTimeNotification"), object: nil)
                    PlayerManager.shared.audioPlayerPoiDescriptionQueue!.play()
                }
                else
                {
                    PlayerManager.shared.audioPlayerPoiDescriptionQueue?.insert(playerItem, after: PlayerManager.shared.audioPlayerPoiDescriptionQueue?.items().last)
                    PlayerManager.shared.audioPlayerPoiDescriptionQueue!.play()
                }
                
            }
            
        }
        
        
    }
    
    
    
    func playSynthesizer(description:String) -> Void {
        let speechSynthesizer = SpeechSynthesizer.shared
        
        
        
        let options = SpeechOptions(text:  description)
        options.speechGender = .female
        options.locale = Locale.init(identifier: Utils.getLanguageAndCountry())
        speechSynthesizer.audioData(with: options) { (data: Data?, error: NSError?) in
            if let data = data
            {
                PlayerManager.shared.startPlayerDirectional(data: data, delegate:self) {
                    PlayerManager.shared.playPlayerDirectional()
                }
            }
            if error != nil
            {
                var speechSynthesizer = AVSpeechSynthesizer()
                let voices = AVSpeechSynthesisVoice.speechVoices()
                var voiceToUse: AVSpeechSynthesisVoice?
                for voice in voices {
                    print(voice)
                    if voice.quality == .enhanced && voice.language == "fr-FR"{
                        voiceToUse = voice
                    }
                }
                // Line 2. Create an instance of AVSpeechUtterance and pass in a String to be spoken.
                var speechUtterance: AVSpeechUtterance = AVSpeechUtterance(string: description)
                //Line 3. Specify the speech utterance rate. 1 = speaking extremely the higher the values the slower speech patterns. The default rate, AVSpeechUtteranceDefaultSpeechRate is 0.5
                speechUtterance.rate = 0.51
                
                // Line 4. Specify the voice. It is explicitly set to English here, but it will use the device default if not specified.
                speechUtterance.voice = voiceToUse ?? AVSpeechSynthesisVoice(language: "fr-FR")
                
                // Line 5. Pass in the urrerance to the synthesizer to actually speak.
                speechSynthesizer.speak(speechUtterance)
                
            }
            
        }
        
        
        
    }
    
}


extension Data {
    func getAVAsset(str:String) -> AVAsset {
        let directory = NSTemporaryDirectory()
        let fileName = "\(NSUUID().uuidString)\(str).mp3"
        let fullURL = NSURL.fileURL(withPathComponents: [directory, fileName])
        try! self.write(to: fullURL!)
        let asset = AVAsset(url: fullURL!)
        return asset
    }
}
