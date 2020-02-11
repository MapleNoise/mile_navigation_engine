//
//  LocationManager.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/20/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import CoreLocation

class LocationService: NSObject {

    let locationManager: CLLocationManager!

    init(locationManager: CLLocationManager) {
        self.locationManager = locationManager
        super.init()
        self.locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters
        self.locationManager.delegate = self
        self.locationManager.requestWhenInUseAuthorization()
        self.locationManager.startUpdatingLocation()
        self.locationManager.startMonitoringSignificantLocationChanges()
    }

    func getCurrentLocation() {
        self.locationManager.requestLocation()
        self.locationManager.requestWhenInUseAuthorization()
    }
}

extension LocationService: CLLocationManagerDelegate {

    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations:[CLLocation]) {
        guard let location = locations.first else { return }
        print("The location is: \(location)")
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Error: \(error)")
    }

    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        print("status  \(status)")
    }
}
