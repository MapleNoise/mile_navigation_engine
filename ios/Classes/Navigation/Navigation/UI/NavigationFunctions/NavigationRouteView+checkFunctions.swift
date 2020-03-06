//
//  NavigationRouteView+Functions.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/25/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import MapboxCoreNavigation
import MapboxNavigation
import Mapbox
import MapboxDirections
import AVFoundation
import FloatingPanel
import Animo
import AudioToolbox

extension NavigationRouteView 
{
    
    func checkTehEndOfTheRoute(myPosition: CLLocation) -> Void {
        if numberofSecondPastOnSR > 600 {
            //The end of Smart Run cannot be raise until 5 min passed since the beginning of the Smart Run. Otherwise the 40m limits could be triggered because the starting point is next to the ending point. numberofSecondToTestOnArrival must be reinit to 0 each time the user ask for a inversing of SR orientation
            if lastWaypoint != nil {
                let deltaToEndingPoint: CLLocationDistance = myPosition.distance(from: CLLocation.init(latitude: lastWaypoint.coordinate.latitude, longitude:  lastWaypoint.coordinate.longitude) )
                if deltaToEndingPoint < 40 {
                    self.deinitNavigation()
                    self.playSynthesizer(description: "endRoute".localized)
                    self.fpc = FloatingPanelController()
                    self.fpc.delegate = self // Optional
                    self.fpc.surfaceView.cornerRadius = 30.0
                    self.contentVC = StatisticsRouter.createViewController(parentViewController: self) as!  StatisticsView
                    self.contentVC!.set(object: StatisticsEntity(distance: self.distanceValue_lbl.text, speed: self.speedValue_lbl.text, time: self.timeValue_lbl.text))
                    self.fpc.set(contentViewController: self.contentVC)
                    self.fpc.addPanel(toParent: self)
                }
            }
        }
    }
    
    
    func checkIfUserLeave(myPosition : CLLocation,coordinatesArray: Array<CLLocationCoordinate2D>) {
        if let nearestGPXPoint = Utils.checkNearestGPXPoint(myPosition, inArrayGPS: coordinatesArray) {
            
            let ditance = nearestGPXPoint.distance(from: myPosition)
            let maximumDistance = 390.0
            
            if (Double(ditance) > maximumDistance)
            {
                if(timeStampLeaveRoute == nil)
                {
                    print("leave")
                    timeStampLeaveRoute = NSDate().timeIntervalSince1970
                } else if  ( (NSDate().timeIntervalSince1970 - timeStampLeaveRoute! )>20)
                {
                    print("leave")
                    timeStampLeaveRoute = NSDate().timeIntervalSince1970
                }
            }
            
        }
    }
    
    
    func checkIfInDirectionalZone(_ myDirectionPOIArray : Array<PointOfInterest>?, _ userPosition : CLLocation) -> Bool {
        guard userPosition.horizontalAccuracy < 50 else {
            return false
        }
        
        if let tempArrayPOIDirection = myDirectionPOIArray {
            for poiDirection in tempArrayPOIDirection {
                for coorGPS in poiDirection.getLocations() {
                    let delta: CLLocationDistance = userPosition.distance(from: coorGPS)
                    if delta < Double(poiDirection.range) {
                        NSLog("is in Direction zone")
                        return true
                    }
                }
            }
            return false
        }else{
            return false
        }
    }
    
    
    func detectPOI(in actualLocation : CLLocation, from previousLocation : CLLocation?) {
        //We use actualLocation to check if user is in a POI zone, and previousLocation is used to create a path to compare if POI is on the right, left ....
        if actualLocation.horizontalAccuracy < 50 { //We add 5 seconds to POI detector in order to let the TTS to annouce the user arrived on starting point without
            NavigationRouteView.mutedZone = checkIfInDirectionalZone(object!.directionals, actualLocation)
            for (index,directional) in object!.directionals.enumerated() {
                for (_, location ) in directional.getLocations().enumerated() {
                    let delta: CLLocationDistance = actualLocation.distance(from: location)
                    if delta < Double(directional.range) {
                        if (object!.directionals![index].status == .notPlayed && object!.directionals![index].isBloquant == false) {

                            print(directional.description)
                            object!.directionals![index].status = .isPlaying
                            
                            if (NavigationRouteView.statusAudioPlayerPoi == .isPlaying)
                            {
                                PlayerManager.shared.audioPlayerPoiDescriptionQueue?.pause()
                                NavigationRouteView.statusAudioPlayerPoi = .paused
                            }
                            
                            self.instructionsBannerView.primaryLabel.text = directional.description
                            playSynthesizerDirectionnal(description: directional.description)
                            
                        } else if (object!.directionals![index].status == .notPlayed && object!.directionals![index].isBloquant == false) {
                            
                        }
                    }
                }
            }
            for (index,poi) in object!.pois.enumerated() {
                for (location ) in poi.getLocations() {
                    let delta: CLLocationDistance = actualLocation.distance(from: location)
                    if delta < Double(poi.range) {
                        if (object!.pois![index].status == .notPlayed)
                        {
                            object!.pois![index].status = .isPlaying
                            Locale.nationalizedCurrent = Locale.init(identifier: Utils.getLanguageAndCountry())
                            //  let speechSynthesizer = SpeechSynthesizer.shared
                            object!.pois![index].status = .isPlaying
                            AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) { }

                            flutterDetectPOI()

                            if let player = self.voiceController?.audioPlayer
                            {
                                if player.isPlaying
                                {
                                    let time = player.duration-player.currentTime + 2
                                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+time) {
                                        NavigationRouteView.statusAudioPlayerPoi = .isPlaying
                                        self.playSynthesizerPoiDescription(description:poi.description)
                                    }
                                }
                                else
                                {
                                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+1) {
                                        NavigationRouteView.statusAudioPlayerPoi = .isPlaying
                                        self.playSynthesizerPoiDescription(description:poi.description)
                                    }
                                }
                                
                            }
                            else
                            {
                                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+1) {
                                    NavigationRouteView.statusAudioPlayerPoi = .isPlaying
                                    self.playSynthesizerPoiDescription(description:poi.description)
                                }
                            }
                            
                            
                        }
                    }
                }
            }
            
        }
    }
    
    
}
