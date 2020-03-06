//
//  NavigationRouteView+initFunctions.swift
//  MyThalassa
//
//  Created by Hassine on 30/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import CoreLocation
import MapboxCoreNavigation
import MapboxNavigation
import Animo



extension NavigationRouteView
{
    
    func initLocationManager() -> Void {
        
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation
        self.locationManager.delegate = self
        self.locationManager.allowsBackgroundLocationUpdates = true
        self.locationManager.requestWhenInUseAuthorization()
        self.locationManager.startUpdatingLocation()
        self.locationManager.distanceFilter = 5
        self.locationManager.pausesLocationUpdatesAutomatically = true
        self.locationManager.activityType = .fitness
    }
    
    
    func initTheNavigationtoTheRoute() {
        calculateWayToRoute { (routes) in
            if let route = routes?.first
            {
                self.routes.append(route)
                self.navigationStatus = .navigation
                if self.bannerViewAnimated == false
                {
                    self.bannerViewAnimated = true
                    self.topContainer_view.layer.runAnimation(
                        Animo.move( // Moves the layer's position
                            by: CGPoint(x: 0, y: self.topContainer_view.frame.height), // "by", "from", and "to" arguments are supported
                            duration: 1,
                            timingMode: .spring(damping: 0.5) // simplistic spring function that doesn't rely on physics
                        )
                    )
                }
                
                self.navigationInoformation_view.layer.runAnimation(
                    
                    Animo.sequence(
                        Animo.keyPath(
                            "backgroundColor",
                            from: UIColor.clear,
                            to: Style.cardColor,
                            duration: 1,
                            timingMode: .easeInOut
                        )
                        
                    )
                )
                self.pauseAndStop_btn.isHidden = true
                UIView.animate(withDuration: TimeInterval.init(0.5)) {
                    self.pause_btn.isHidden = false
                    self.finish_btn.isHidden  = false
                }
                self.topContainer_view.addSubview(UIButton.init(frame: CGRect.init(x: 0, y: 0, width: 200, height: 200)))
                self.containerInformations_view.layer.runAnimation(
                    Animo.fade(from: 0, by: 1, to: 1, duration: 1, timingMode: TimingMode.easeInOut, options: Options.init())
                )
                if let isNavigationActivated = self.object?.route.isNavigationActivated {
                    if isNavigationActivated == true  && AppDataHolder.flutterNavigationMode == NavigationMode.NAVIGATE_IN_ROUTE {
                        self.calculateRoute { (routes, waypoints) in
                            if routes == nil && waypoints == nil {
                                self.drawRoutePolyline()
                                self.iniSimulatedNavigation()
                            } else {
                                self.lastWaypoint = waypoints!.last
                                self.firstWaypoint = waypoints!.first
                                self.routes.append(routes!.first!)
                                self.iniSimulatedNavigation()
                            }
                        }
                    } else {
                        if AppDataHolder.flutterNavigationMode == NavigationMode.NAVIGATE_IN_ROUTE {
                            self.drawRoutePolyline()
                        }
                        self.showRoutes()
                        self.iniSimulatedNavigation()
                    }
                }
            }
        }
    }
    
    func iniSimulatedNavigation() -> Void {
        suspendNotifications()
        self.instructionsBannerView.removeFromSuperview()
        self.instructionsBannerView = nil
        if self.routes.count  > 0
        {
            
            
            /*if (AppDelegate.testMode == false )
            {*/
                navigationService = MapboxNavigationService(route: self.routes[0])
            
            /*}
            else
            {
                simulatedLocationManager = SimulatedLocationManager(route: self.routes[0])
                simulatedLocationManager?.speedMultiplier = 1
                navigationService = MapboxNavigationService(route: self.routes[0], locationSource: simulatedLocationManager, simulating:.always)
            }*/
            self.voiceController = CustomVoiceController(navigationService: self.navigationService)
            self.voiceController?.initNotification()
            self.navigationService.delegate = self
            self.navigationService.routeProgress.route.routeOptions.locale = .init(identifier: Utils.getLanguageAndCountry())
            self.navigationService.start()
            self.map.recenterMap()
            
            addPointOfInterest()
            self.showRoutes()
        }
        
        self.instructionsBannerView = InstructionsBannerView.init(frame: self.container_instructionsBannerView.frame)
        // self.instructionsBannerView.delegate = self
        self.container_instructionsBannerView.addSubview(self.instructionsBannerView)
        self.resumeNotifications()
        
    }
    
    
    func iniSimulatedPolylineNavigation() -> Void {
        self.suspendNotifications()
        self.map.setUserTrackingMode(MGLUserTrackingMode.follow, animated: true) {
        }
    }
    
    
    
    
}
