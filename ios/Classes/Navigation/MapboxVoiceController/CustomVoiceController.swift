//
//  CustomVoiceController.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/24/19.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit
import MapboxSpeech
import Mapbox
import AVFoundation
import MapboxSpeech
import MapboxCoreNavigation
import MapboxDirections
import MapboxNavigation


/// MapboxVoiceController 82

//@objc public func audioPlayerDidFinishPlaying(_ player: AVAudioPlayer, successfully flag: Bool) {
//    do {
//        try unDuckAudio()
//         NotificationCenter.default.post(name:NSNotification.Name("finishAudio"),object: nil)
//        print("finishAudio")
//    } catch {
//        voiceControllerDelegate?.voiceController?(self, spokenInstructionsDidFailWith: error)
//    }
//
//}

class CustomVoiceController: MapboxVoiceController {
    
    //  var poiPlayer : AVAudioPlayer?
    //var poiAudioStatus : PoiStatuts?
    //var directionnalAudioStatus : PoiStatuts?
    //var poiSpeakerStatus : PoiStatuts?
    
    override func didPassSpokenInstructionPoint(notification: NSNotification) {
        super.didPassSpokenInstructionPoint(notification: notification)
    }
    
    override func speak(_ instruction: SpokenInstruction) {
        super.speak(instruction)
    }
    
    func initNotification()
    {
        NotificationCenter.default.addObserver(self,selector: #selector(playerDidFinishPlaying),name: NSNotification.Name ("finishAudio"),object: nil)
    }
    

    
    
    override func play(_ data: Data) {
        
        if (NavigationRouteView.mutedZone == false)
        {
          
            if ( NavigationRouteView.statusAudioPlayerPoi == .isPlaying)
            {
                 //PlayerManager.shared.pausePlayerPoiDescription()
                 PlayerManager.shared.audioPlayerPoiDescriptionQueue?.pause()
                PlayerManager.shared.speechSynthesizer.stopSpeaking(at: AVSpeechBoundary.word)
                 NavigationRouteView.statusAudioPlayerPoi = .paused
            }
            if ( NavigationRouteView.statusAudioPlayerDirectionnal == .isPlaying)
            {
                  PlayerManager.shared.pausePlayerDirectional()
                 NavigationRouteView.statusAudioPlayerDirectionnal = .paused
            }
            let semaphore = DispatchSemaphore(value: 1)
            let queue = DispatchQueue(label: "queuename")
            queue.async {
                semaphore.wait()
                super.play(data)
                semaphore.signal()
            }
        }
    }
    
    
    @objc func playerDidFinishPlaying() {
        if NavigationRouteView.statusAudioPlayerPoi == .paused
        {
               if ( PlayerManager.shared.audioPlayerPoiDescriptionQueue != nil)
            {
                if PlayerManager.shared.audioPlayerPoiDescriptionQueue?.currentItem?.currentTime() != nil
                {
                    if ( PlayerManager.shared.audioPlayerPoiDescriptionQueue!.currentItem!.currentTime() > CMTime.init(value: 2, timescale: 1))
                                             {
                                               
                                               var time = PlayerManager.shared.audioPlayerPoiDescriptionQueue!.currentTime() - CMTime.init(value:2,timescale: 1)
                                               PlayerManager.shared.audioPlayerPoiDescriptionQueue!.seek(to:time)
                                              // PlayerManager.shared.audioPlayerPoiDescriptionQueue!.currentItem. =  PlayerManager.shared.audioPlayerPoiDescription!.currentTime - 2
                                                  NavigationRouteView.statusAudioPlayerPoi = .isPlaying
                                               PlayerManager.shared.audioPlayerPoiDescriptionQueue?.play()
                                                PlayerManager.shared.speechSynthesizer.continueSpeaking()
                                               //  print("PlayerManager.shared.audioPlayer!.currentTime : \( PlayerManager.shared.audioPlayerPoiDescription!.currentTime)")
                                             }
                                             else
                                             {
                                                PlayerManager.shared.audioPlayerPoiDescriptionQueue!.seek(to: CMTime.init(value:0,timescale: 1))
                                                  NavigationRouteView.statusAudioPlayerPoi = .isPlaying
                                                 PlayerManager.shared.audioPlayerPoiDescriptionQueue?.play()
                                                PlayerManager.shared.speechSynthesizer.continueSpeaking()
                                             }
                }
               
           }
            
        //     PlayerManager.shared.audioPlayerPoiDescriptionQueue?.play()
          
        }
        
//        if NavigationRouteView.statusAudioPlayerPoi == .paused
//
//            {
//
//                if (self.poiPlayer!.currentTime > 2)
//                {
//                    self.poiPlayer!.currentTime =  self.poiPlayer!.currentTime - 2
//                    self.poiPlayer!.play()
//                }
//                else
//                {
//                    self.poiPlayer!.play()
//                }
//            }
        
    }
}

