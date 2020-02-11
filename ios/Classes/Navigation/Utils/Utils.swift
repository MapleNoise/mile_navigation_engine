//
//  Utils.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/20/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import CoreLocation
import MapboxDirections

class Utils {
    
    public static func updateNearestCity(destinations:[Destination], detectedPosition: CLLocation) -> Destination?
    {
        var deltaComparaison: CLLocationDistance = 50000 //maximum distance from where nearest cities that can be matched.
        var nearestDestination : Destination!
        for destination in destinations {
            let secondLocation = CLLocation(latitude: destination.latitude!, longitude: destination.longitude!)
            let delta: CLLocationDistance = detectedPosition.distance(from: secondLocation)
            if delta < deltaComparaison {
                deltaComparaison = delta
                nearestDestination = destination
                
            }
        }
        return nearestDestination
    }
    
    public static func getLanguageISO() -> String {
        let locale = Locale.current
        guard let languageCode = locale.languageCode else {
            return "en"
        }
        return languageCode
    }
    
    
    public static func getDistanceMeasurementSystem() -> MeasurementSystem {
        let locale = Locale.current
               if (locale.languageCode == "fr" )
               {
                return .metric
               }
               else
               {
                return .imperial
               }
    }
    
    public static func getLanguageAndCountry() -> String {
        
        let locale = Locale.current
        if (locale.languageCode == "fr" )
        {
             return "fr-FR"
        }
        else
        {
             return "en-US"
        }
       
       
    }
    
    
    public static func changeBeginningOfSmartRunWithnearestPoint( _ myTablePointGPX: Array<CLLocationCoordinate2D>, positionGPS: CLLocation!) -> Array<CLLocationCoordinate2D>{
        guard positionGPS != nil else {
            return myTablePointGPX
        }
        let nearestPointGPX = Utils.findNearestGPXPoint(myTablePointGPX, positionGPS: positionGPS)
        var newTablePointGPX: Array<CLLocationCoordinate2D> = []
        for j in nearestPointGPX ..< myTablePointGPX.count {
            newTablePointGPX.append(myTablePointGPX[j])
        }
        for k in 0 ..< nearestPointGPX {
            newTablePointGPX.append(myTablePointGPX[k])
        }
        return newTablePointGPX
    }
    
    public static func findNearestGPXPoint(_ myTablePointGPX: Array<CLLocationCoordinate2D>, positionGPS: CLLocation) -> Int{
        var nearestPointGPX:    Int                 = 0
        var deltaComparaison:   CLLocationDistance  = positionGPS.distance(from: CLLocation.init(latitude: myTablePointGPX[0].latitude, longitude: myTablePointGPX[0].longitude) )
        for i in 0 ..< myTablePointGPX.count {
            let delta: CLLocationDistance = positionGPS.distance(from:CLLocation.init(latitude: myTablePointGPX[i].latitude, longitude: myTablePointGPX[i].longitude))
            if delta < deltaComparaison{
                deltaComparaison    = delta
                nearestPointGPX     = i
            }
        }
        return nearestPointGPX
    }
    
    
     public static func checkNearestGPXPoint(_ myLocation: CLLocation, inArrayGPS : Array<CLLocationCoordinate2D>?) -> CLLocation? {
        guard inArrayGPS != nil else {
            return nil
        }
        
        var closestPoint: CLLocation!
        var tempMinimumDelta:CLLocationDistance = 40
        //We check if the user is next to a GPX Position
        for coordGPX in inArrayGPS! {
            let delta: CLLocationDistance = myLocation.distance(from: CLLocation.init(latitude: coordGPX.latitude, longitude: coordGPX.longitude))
            if delta < 40 && delta < tempMinimumDelta {
                tempMinimumDelta = delta
                //The user is next to a GPX Position
                //We save this position
                closestPoint = CLLocation.init(latitude: coordGPX.latitude, longitude: coordGPX.longitude)
            }
        }
        
        return closestPoint
        
    }


     public static func generateGpxTags(coordinatesArray: Array<CLLocationCoordinate2D>) -> Void {

           var tagsGpx = ""
           for (index, poi) in coordinatesArray.enumerated()
           {
               let time =  index * 5


               let secondes = time % 60
               let mintues = time / 60
            let tag = "<wpt lat='\(poi.latitude)' lon='\(poi.longitude)'>\n<name>Rue dugesclien/villeroy</name>\n<time>2014-09-24T14:\(mintues):\(secondes)Z</time>\n</wpt>"
               tagsGpx = "\(tagsGpx)\n\(tag)"
           }

           print(tagsGpx)


       }
}
