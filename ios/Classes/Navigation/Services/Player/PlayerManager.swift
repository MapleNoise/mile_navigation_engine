//
//  PlayerManager.swift
//  MyThalassa
//
//  Created by Hassine on 09/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import AVFoundation




class PlayerManager {
    
    static let shared = PlayerManager()
    var audioPlayer: AVAudioPlayer?
  //  var audioPlayerPoiDescription: AVAudioPlayer?
    var audioPlayerPoiDescriptionQueue: AVQueuePlayer?
    var audioPlayerDirectional: AVAudioPlayer?
    var delegate : PlayerManagerDelegate? = nil
    var timer : Timer? = nil
    
//    func startPoiDescription(mp3URL:URL, delegate: PlayerManagerDelegate?, _ callBack:@escaping () -> Void) {
//        URLSession.shared.dataTask(with: mp3URL) { (data, response, error) in
//            if error != nil
//            {
//                print(error)
//                return
//            }
//            do
//            {
//                if data != nil {
//                    self.audioPlayerPoiDescription = try AVAudioPlayer.init(data: data!, fileTypeHint: AVFileType.mp3.rawValue)
//                    guard let audioPlayer = self.audioPlayerPoiDescription else { return }
//                    audioPlayer.numberOfLoops = 0
//
//                    let audioSession = AVAudioSession.sharedInstance()
//                    try audioSession.setCategory(AVAudioSession.Category.playback)
//                    try audioSession.setActive(true)
//                    audioPlayer.prepareToPlay()
//                    self.delegate = delegate
//                    callBack()
//                }
//                else
//                {
//                    print("data nil")
//                    return
//                }
//            }
//            catch
//            {
//            }
//
//        }.resume()
//    }
    
    
    
    
    
    
//    func startPoiDescription(data:Data, delegate: PlayerManagerDelegate?, _ callBack:@escaping () -> Void) {
//
//            do
//            {
//                if data != nil {
//                    self.audioPlayerPoiDescription = try AVAudioPlayer.init(data: data, fileTypeHint: AVFileType.mp3.rawValue)
//                    guard let audioPlayer = self.audioPlayerPoiDescription else { return }
//                    audioPlayer.numberOfLoops = 0
//
//                    let audioSession = AVAudioSession.sharedInstance()
//                    try audioSession.setCategory(AVAudioSession.Category.playback)
//                    try audioSession.setActive(true)
//                    audioPlayer.prepareToPlay()
//                    self.delegate = delegate
//                    callBack()
//                }
//                else
//                {
//                    print("data nil")
//                    return
//                }
//            }
//            catch
//            {
//            }
//
//
//    }
//
    
     var speechSynthesizer = AVSpeechSynthesizer()
    
    func playSynt(description:String)
      {
         
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
    
    
    func startPlayer(nameFile:String, _ callBack:@escaping () -> Void) {
        
          let path = Bundle.main.path(forResource: nameFile, ofType:"mp3")!
          let url = URL(fileURLWithPath: path)
            do
            {
                if url != nil {
                    self.audioPlayer = try AVAudioPlayer.init(contentsOf: url)
                    guard let audioPlayer = self.audioPlayer  else { return }
                    audioPlayer.numberOfLoops = 0
                    let audioSession = AVAudioSession.sharedInstance()
                    try audioSession.setCategory(AVAudioSession.Category.playback)
                    try audioSession.setActive(true)
                    audioPlayer.prepareToPlay()
                    callBack()
                }
                else
                {
                    print("data nil")
                    return
                }
            }
            catch
            {
            }
            
       
    }
    
    
    func startPlayerDirectional(data:Data, delegate: PlayerManagerDelegate?, _ callBack:@escaping () -> Void) {
       
            do
            {
               
                if data != nil {
                    self.audioPlayerDirectional = try AVAudioPlayer.init(data: data, fileTypeHint: AVFileType.mp3.rawValue)
                    guard let audioPlayer = self.audioPlayerDirectional else { return }
                    audioPlayer.numberOfLoops = 0
                    
                    let audioSession = AVAudioSession.sharedInstance()
                    try audioSession.setCategory(AVAudioSession.Category.playback)
                    try audioSession.setActive(true)
                    audioPlayer.prepareToPlay()
                    self.delegate = delegate
                    callBack()
                }
                else
                {
                    print("data nil")
                    return
                }
            }
            catch
            {
            }
            
      
    }
    
    
    func startPlayerDirectional(mp3URL:URL, delegate: PlayerManagerDelegate?, _ callBack:@escaping () -> Void) {
        URLSession.shared.dataTask(with: mp3URL) { (data, response, error) in
            if error != nil
            {
                print(error)
                return
            }
            do
            {
               
                if data != nil {
                    self.audioPlayerDirectional = try AVAudioPlayer.init(data: data!, fileTypeHint: AVFileType.mp3.rawValue)
                    guard let audioPlayer = self.audioPlayerDirectional else { return }
                    audioPlayer.numberOfLoops = 0
                    
                    let audioSession = AVAudioSession.sharedInstance()
                    try audioSession.setCategory(AVAudioSession.Category.playback)
                    try audioSession.setActive(true)
                    audioPlayer.prepareToPlay()
                    self.delegate = delegate
                    callBack()
                }
                else
                {
                    print("data nil")
                    return
                }
            }
            catch
            {
            }
            
        }.resume()
    }
    
    
    
//    @objc func updateAudioProgressViewPoiDescription(timer: Timer)
//    {
//        if let player = audioPlayerPoiDescription
//        {
//
//            // Update progress
//            // progressView.setProgress(Float(audioPlayer.currentTime/audioPlayer.duration), animated: true)
//
//
//            if (player.duration - player.currentTime < 2)
//                       {
//                            timer.invalidate()
//                        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+1) {
//                            self.delegate!.PlayerManagerDidFinishPoiDesciption()
//                               print("ttt : self.delegate?.PlayerManagerDidFinishDirectionnal()")
//                           }
//
//                       }
//
//
//
//
//        }
//    }
//
    
    @objc func updateAudioProgressViewDirectionnal(timer: Timer)
    {
        if let player = audioPlayerDirectional
        {
            
            // Update progress
            // progressView.setProgress(Float(audioPlayer.currentTime/audioPlayer.duration), animated: true)
            delegate?.updateTimer(playerStatus: Float(player.currentTime/player.duration), currentTime: Float(player.currentTime))
            print("ttt : \(player.duration) \(player.currentTime)")
            
            if (player.duration - player.currentTime < 2)
            {
                 timer.invalidate()
                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+2) {
                    self.delegate?.PlayerManagerDidFinishDirectionnal()
                    print("ttt : self.delegate?.PlayerManagerDidFinishDirectionnal()")
                }
                
            }

        }
    }
    
    
    func stopPoiDirectional() -> Void {
        
        if let player = audioPlayerDirectional
        {
            player.stop()
            if let timer = timer
            {
                timer.invalidate()
            }
        }
        
    }
    
    
//    func stopPoiDescriptionPlayer() -> Void {
//
//        if let player = audioPlayerPoiDescription
//        {
//            player.stop()
//            if let timer = timer
//            {
//                timer.invalidate()
//            }
//        }
//
//    }
    
    
    func stopPlayer() -> Void {
           
           if let player = audioPlayer
           {
               player.stop()
              
           }
           
       }
    
    
    func pausePlayerDirectional() -> Void {
        if let player = audioPlayerDirectional
        {
            player.pause()
            
            if let timer = timer
            {
                timer.invalidate()
            }
        }
        
    }
    
    
    func pausePlayerPoiDescription() -> Void {
           if let player = audioPlayerPoiDescriptionQueue
           {
               player.pause()
           // PlayerManager.shared.audioPlayerPoiDescriptionQueue!.pause()
          
           }

       }
    
    
    
    func playPlayerDirectional() -> Void {
        if let player = audioPlayerDirectional
        {
            player.play()
            timer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(self.updateAudioProgressViewDirectionnal), userInfo: nil, repeats: true)
            timer!.fire()
        }
        
    }
    
    
    func playPlayerPoiDescription() -> Void {
         if let player = audioPlayerPoiDescriptionQueue
         {
             player.play()
             //timer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(self.updateAudioProgressViewPoiDescription), userInfo: nil, repeats: true)
           //  timer!.fire()
         }
         
     }
    
    
    func playPlayerl() -> Void {
         if let player = audioPlayer
         {
             player.play()

         }

     }
    
    
    
}




