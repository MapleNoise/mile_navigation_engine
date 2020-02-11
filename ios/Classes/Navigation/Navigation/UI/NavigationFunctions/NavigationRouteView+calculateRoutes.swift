//
//  NavigationRouteView+calculateRoutes.swift
//  MyThalassa
//
//  Created by Hassine on 30/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import MapboxCoreNavigation
import MapboxNavigation
import MapboxDirections

extension NavigationRouteView
{
    
    
    func calculateWayToRoute(_ callback:@escaping ([Route]?) -> Void) {
        if let location = self.map.userLocation , let waypoint = self.waypoints.first
        {
            navigationStatus = .calculating
            
            let option = NavigationRouteOptions(waypoints: [Waypoint.init(coordinate: location.coordinate),waypoint], profileIdentifier: .walking)
            option.locale = Locale.init(identifier: Utils.getLanguageAndCountry())
            option.distanceMeasurementSystem = Utils.getDistanceMeasurementSystem()
            option.waypoints = option.waypoints.map {
                $0.coordinateAccuracy = 100
                return $0
            }
            Directions.shared.calculate(option, completionHandler: { (waypoints, routes, error)  in
                if let routes = routes
                {
                    callback(routes)
                }
                else
                {
                    print("LOG : don't find way to the route")
                    self.navigationStatus = .none
                }
            })
        }
        
    }
    
    
    
    func calculateRoute(_ callback : @escaping ([Route]?,_ waypoints: [Waypoint]?) -> Void ) -> Void {
        
        
        var profileIdentifier = MBDirectionsProfileIdentifier.walking
        var a = [Waypoint]()
        for i in 0...waypoints.count-1
        {
            a.append(waypoints[i])
        }
        
        let matchOptions = NavigationMatchOptions(waypoints: a, profileIdentifier: profileIdentifier)
        matchOptions.includesSteps = true
        //matchOptions.waypointIndices = IndexSet([0, 1, waypoints.count - 1])
        matchOptions.waypoints.forEach { (waypoint) in
            waypoint.separatesLegs = false
        }
        
        matchOptions.distanceMeasurementSystem = Utils.getDistanceMeasurementSystem()
        matchOptions.locale = .init(identifier: Utils.getLanguageAndCountry())
        
        Directions.shared.calculateRoutes(matching: matchOptions) { (waypoints: [Waypoint]?, routes: [Route]?, error: Error?) in
            if routes == nil
            {
                callback(nil,nil)
            }
            else
            {
                
                
                callback(routes,waypoints)
            }
        }
        
    }
}
