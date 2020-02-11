//
//  NavigationRouteView+NavigationServiceDelegate.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/25/19.
//  Copyright © 2019 mile. All rights reserved.
//

import MapboxCoreNavigation
import UIKit
import Mapbox
import MapboxDirections
import FloatingPanel
import MapboxNavigation
import AVFoundation



// MARK: - NavigationServiceDelegate
extension NavigationRouteView :NavigationServiceDelegate, AVAudioPlayerDelegate
{
    
    

    // MARK: - update position
    func navigationService(_ service: NavigationService, didUpdate progress: RouteProgress, with location: CLLocation, rawLocation: CLLocation) {
        DispatchQueue.main.async {
            self.instructionsBannerView.primaryLabel.font =  UIFont.boldSystemFont(ofSize: 28)
            self.instructionsBannerView.secondaryLabel.font =  UIFont.boldSystemFont(ofSize: 22)
            self.instructionsBannerView.primaryLabel.textColor  = UIColor.white
            self.instructionsBannerView.secondaryLabel.textColor  = UIColor.white
            self.instructionsBannerView.maneuverView.primaryColor = UIColor.white
            self.instructionsBannerView.distanceLabel.textColor = UIColor.white
            self.distanceValue_lbl.text = self.distanceFormatter.string(from: progress.distanceTraveled)
            self.distanceValue_lbl.text = self.distanceValue_lbl.text!.replacingOccurrences(of: "-", with: "")
            let distance =  Measurement(value: location.speed, unit: UnitSpeed.metersPerSecond)
            
            let speedString = distance.converted(to: UnitSpeed.kilometersPerHour)
            
           let formatter = MeasurementFormatter()
            formatter.unitOptions = .providedUnit
            formatter.numberFormatter.maximumFractionDigits = 1
            
            
    
            self.speedValue_lbl.text = formatter.string(from: speedString) // "20.22 mg"
           
        }
        
             print("progress...")
        
            if(self.previousLocation != nil && self.indexRoute == 1)
            {
                if self.timer.isValid
                {
                    DispatchQueue.main.async {
                    self.detectPOI(in: location, from: self.previousLocation)
                    }
                }
                
                if self.object?.route.isNavigationActivated == false
                {
                    self.checkIfUserLeave(myPosition: location, coordinatesArray: self.object!.route.coordinatesGPX)
                    self.checkTehEndOfTheRoute(myPosition: location)
                }
            }
            self.previousLocation = location
            if self.isProgressing
            {
                self.routes[0] = progress.route
                      
                DispatchQueue.main.async {
                     self.showRoutes()
                }
                      
            }
            if self.timer.isValid
            {
                
                let distance =  Measurement(value: location.speed, unit: UnitSpeed.metersPerSecond)
                let speedString = distance.converted(to: UnitSpeed.kilometersPerHour)

            }
        
        
        
    }
    
     //
    // MARK: - ArriveAt waypoint ( we will detect the lasts waypoints of the way and the route: must return true)
    func navigationService(_ service: NavigationService, didArriveAt waypoint: Waypoint) -> Bool {
        // end of the route
        
            if (waypoint == self.lastWaypoint)
                  {
                      if self.indexRoute == 1
                      {
                         DispatchQueue.main.async {
                        self.timer.invalidate()
                        
                          self.deinitNavigation()
                                self.playSynthesizer(description: "endRoute".localized)
                                self.fpc = FloatingPanelController()
                                self.fpc.delegate = self // Optional
                                self.fpc.surfaceView.cornerRadius = 30.0
                                self.contentVC = StatisticsRouter.createViewController(parentViewController: self) as!  StatisticsView
                                self.contentVC!.set(object: StatisticsEntity(distance: self.distanceValue_lbl.text, speed: self.speedValue_lbl.text, time: self.timeValue_lbl.text))
                                self.fpc.set(contentViewController: self.contentVC)
                                self.fpc.addPanel(toParent: self)
                            self.locationManager.stopUpdatingLocation()
                          
                        }
                          
                      }
                  }
                  else
                  {
                    self.isProgressing = false
                      // end of the way route
                      if self.indexRoute == 0
                      {
                        self.suspendNotifications()
                        self.indexRoute += 1
                          self.routes.removeFirst()
                        DispatchQueue.main.async {
                             self.map.removeRoutes()
                        }
                         
                       
                          
                              if self.object?.route.isNavigationActivated == true
                              {
                                self.timer = Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(self.timerDuringSmartRun), userInfo: nil, repeats: true)
                                                       self.timestamp = Int64(Date().timeIntervalSince1970)
                                 DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+2) {
                                    self.iniSimulatedNavigation()
                                    self.showRoutes()
                                }
                                  
                                  DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+5) {
                                  self.isProgressing = true
                                  }
                              }
                              else
                              {
                                    
                                  self.iniSimulatedNavigation()
                                
                              }
                          }
                  }
         return true
    }
    
    @objc func timerDuringSmartRun(){
        numberofSecondPastOnSR          += 1
        var minute:     Int?
        var seconde:    Int?
        var heure:      Int?
        heure       = numberofSecondPastOnSR/3600
        minute      = (numberofSecondPastOnSR % 3600) / 60
        seconde     = (numberofSecondPastOnSR % 3600 ) % 60
        if minute != nil && seconde != nil && heure != nil {
            DispatchQueue.main.async {
                self.timeValue_lbl.text =   String(format: "%02d", heure!) + ":" + String(format: "%02d", minute!) + ":" + String(format: "%02d", seconde!)
            }
        }
    }
    
    @objc func timerDuringSmartRun2(){
        print("background")
    }
    
}
