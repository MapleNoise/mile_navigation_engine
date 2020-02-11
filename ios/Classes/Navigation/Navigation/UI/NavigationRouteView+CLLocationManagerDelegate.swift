//
//  NavigationRouteView+CLLocationManagerDelegate.swift
//  MyThalassa
//
//  Created by Hassine on 29/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import CoreLocation



extension NavigationRouteView : CLLocationManagerDelegate
{
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
    }
     func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
       print("\(error.localizedDescription)")
    }
    
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        switch status {
        case  .restricted, .denied:
            print("No access")
            self.pauseAndStop_btn.isEnabled = false
            self.pauseAndStop_btn.backgroundColor = Style.primaryColorDisabled
            self.followLocation_btn.isHidden = true
        case .authorizedAlways, .authorizedWhenInUse, .notDetermined:
            
            print("Access")
            self.locationManager.startUpdatingLocation()
            self.pauseAndStop_btn.isEnabled = true
            self.pauseAndStop_btn.backgroundColor = Style.accentColor
            self.followLocation_btn.isHidden = false
            
        default : break
        }
        
    }
    
}
