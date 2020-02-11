//
//  NavigationRouteView+FunctionsUI.swift
//  MyThalassa
//
//  Created by Hassine on 18/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit
import MapboxNavigation
import MapboxCoreNavigation
import MapboxDirections
import Mapbox
extension NavigationRouteView
{
    
    
    func initMap() -> Void {
        DispatchQueue.main.async {
            self.playerManagerDelegate = self
            self.map.autoresizingMask = [.flexibleWidth, .flexibleHeight]
            self.map.delegate = self
            self.map.showsUserLocation = true
            self.map.compassView.isHidden = true
        
            //self.map.routeAlternateColor = .green
            //            self.map.zoomLevel = 15
            //            self.map.setUserTrackingMode(MGLUserTrackingMode.followWithCourse, animated: true) {
            //            }
            
            
            if CLLocationManager.locationServicesEnabled() {
                switch CLLocationManager.authorizationStatus() {
                case .notDetermined, .restricted, .denied:
                    print("No access")
                    let camera = MGLMapCamera(lookingAtCenter: (self.object?.route.coordinatesGPX.first)!, altitude: 10500, pitch: 15, heading: 0)
                 //   self.map.setCamera(camera, withDuration: 0, animationTimingFunction: CAMediaTimingFunction(name: CAMediaTimingFunctionName.easeOut))
                case .authorizedAlways, .authorizedWhenInUse:
                    print("Access")
                @unknown default:
                    print("error")
                }
            } else {
                
                let camera = MGLMapCamera(lookingAtCenter: (self.object?.route.coordinatesGPX.first)!, altitude: 4500, pitch: 15, heading: 180)
              //  self.map.setCamera(camera, withDuration: 0 , animationTimingFunction: CAMediaTimingFunction(name: CAMediaTimingFunctionName.easeOut))
            }
            
        
            
            
            
            
        }
    }
    
    func initUI() -> Void {
        DispatchQueue.main.async {
            self.map.logoView.isHidden = true
            //self.map.attributionButton.isHidden = true
            self.pause_btn.primaryStyle()
            self.finish_btn.primaryStyle()
            self.back_btn.backStyle()
            self.navigationInoformation_view.cardStyle()
            self.pauseAndStop_btn.primaryStyle()
            self.pauseAndStop_btn.layer.cornerRadius = 8
            self.timeTitle_lbl.titleNavigationCard()
            self.timeValue_lbl.valueNavigationCard()
            self.distanceTitle_lbl.titleNavigationCard()
            self.distanceValue_lbl.valueNavigationCard()
            self.speedTitle_lbl.titleNavigationCard()
            self.speedValue_lbl.valueNavigationCard()
            self.containerInformations_view.alpha = 0
            self.navigationInoformation_view.backgroundColor = .clear
            self.pauseAndStop_btn.setTitle("Start navigation".localized, for: UIControl.State.normal)
            self.timeTitle_lbl.text = "Time".localized
            self.speedTitle_lbl.text = "Speed".localized

            if CLLocationManager.locationServicesEnabled() {
                switch CLLocationManager.authorizationStatus() {
                case  .restricted, .denied:
                    DispatchQueue.main.async {
                        self.pauseAndStop_btn.isEnabled = false
                        self.pauseAndStop_btn.backgroundColor = Style.primaryColorDisabled
                        self.followLocation_btn.isHidden = true
                    }
                    self.locationManager.requestWhenInUseAuthorization()
                case .authorizedAlways, .authorizedWhenInUse , .notDetermined:
                    print("Access")
                    DispatchQueue.main.async {
                    self.pauseAndStop_btn.backgroundColor = Style.primaryColor
                    self.pauseAndStop_btn.alpha = 1
                    self.pauseAndStop_btn.isEnabled = true
                    self.followLocation_btn.isHidden = false
                    }
                    self.locationManager.startUpdatingLocation()
                    self.map.setUserTrackingMode(MGLUserTrackingMode.followWithCourse, animated: false) {
                        
                    }
                    self.map.zoomLevel = 15
                }
            } else {
                print("Location services are not enabled")
                  DispatchQueue.main.async {
                self.pauseAndStop_btn.isEnabled = false
                self.pauseAndStop_btn.backgroundColor = Style.primaryColorDisabled
                }
            }
            
            
        }
    }
    
    
    func initInstructionBannerView() -> Void {
        DispatchQueue.main.async {
            self.instructionsBannerView = InstructionsBannerView.init(frame:self.container_instructionsBannerView.frame)
            self.container_instructionsBannerView.addSubview(self.instructionsBannerView)
            self.instructionsBannerView.primaryLabel.textColor  = UIColor.white
            self.instructionsBannerView.secondaryLabel.textColor  = UIColor.white
            self.instructionsBannerView.maneuverView.primaryColor = UIColor.white
            self.instructionsBannerView.maneuverView.secondaryColor = UIColor.white
            self.topContainer_view.backgroundColor = Style.primaryColor
            self.container_instructionsBannerView.backgroundColor = Style.primaryColor
            self.topContainer_view.layer.cornerRadius = 20
            if #available(iOS 11.0, *) {
                self.topContainer_view.layer.maskedCorners = [.layerMinXMaxYCorner, .layerMaxXMaxYCorner]
            } else {
                // Fallback on earlier versions
            }
            self.instructionBannerView_containerView.layer.cornerRadius = 20
            if #available(iOS 11.0, *) {
                self.instructionBannerView_containerView.layer.maskedCorners = [.layerMinXMaxYCorner, .layerMaxXMaxYCorner]
            } else {
                // Fallback on earlier versions
            }
        }
    }
    
    
    func showAlertAskFinishRoute(_ callBack:@escaping () -> Void) {
        DispatchQueue.main.async {
            let alert = UIAlertController(title: "Are you sure you want to stop?".localized, message: "", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "Continue".localized, style: .cancel, handler: nil))
            alert.addAction(UIAlertAction(title: "Stop".localized, style: .default, handler:{ action in
                callBack()
                self.locationManager.stopUpdatingLocation()
            }))
            self.present(alert, animated: true)
        }
    }
    
    func showAlertConfirmTheEndofRoute(_ callBack:@escaping () -> Void) {
        DispatchQueue.main.async {
            let alert = UIAlertController(title: "Parcours terminé", message: "Merci", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler:{ action in
                callBack()
            }))
            self.present(alert, animated: true)
        }
    }
    
    func initSpeedButtons() -> Void {
        DispatchQueue.main.async {
            //if AppDelegate.testMode == false
            //{
                self.containerSpeedButtons_stackView.isHidden = true
            /*}
            else
                
            {
                self.containerSpeedButtons_stackView.isHidden = false
            }*/
        }
    }
}
