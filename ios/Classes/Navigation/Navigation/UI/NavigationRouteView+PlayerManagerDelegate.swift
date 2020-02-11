//
//  NavigationRouteView+FunctionsAVPlayer.swift
//  MyThalassa
//
//  Created by Hassine on 18/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit
import AVKit


//NavigationRouteView+PlayerFunctions
//NavigationRouteView+PlayerManagerDelegate

extension NavigationRouteView : PlayerManagerDelegate
    
{
    func PlayerManagerDidFinishPoiDesciption() {
       
    
      
    }
    
    func PlayerManagerDidFinishDirectionnal() {
         NavigationRouteView.statusAudioPlayerDirectionnal = .played
        if  (NavigationRouteView.statusAudioPlayerPoi == .paused)
        {
            
             PlayerManager.shared.audioPlayerPoiDescriptionQueue?.pause()
        }
        
    }
    
    func updateTimer(playerStatus: Float, currentTime: Float) {
    }
    
  
}

