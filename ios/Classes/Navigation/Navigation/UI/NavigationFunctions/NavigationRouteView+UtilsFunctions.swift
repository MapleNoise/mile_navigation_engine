//
//  NavigationRouteView+UtilsFunctions.swift
//  MyThalassa
//
//  Created by Hassine on 30/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import CoreLocation
import MapboxDirections

extension NavigationRouteView
{
    
   
    func calculateCoordinates(coordinate: CLLocationCoordinate2D, withMeterRadius: Double) -> [CLLocationCoordinate2D] {
          let degreesBetweenPoints = 8.0
          //45 sides
          let numberOfPoints = floor(360.0 / degreesBetweenPoints)
          let distRadians: Double = withMeterRadius / 6371000.0
          // earth radius in meters
          let centerLatRadians: Double = coordinate.latitude * Double.pi / 180
          let centerLonRadians: Double = coordinate.longitude * Double.pi / 180
          var coordinates = [CLLocationCoordinate2D]()
          //array to hold all the points
          for index in 0 ..< Int(numberOfPoints) {
              let degrees: Double = Double(index) * Double(degreesBetweenPoints)
              let degreeRadians: Double = degrees * Double.pi / 180
              let pointLatRadians: Double = asin(sin(centerLatRadians) * cos(distRadians) + cos(centerLatRadians) * sin(distRadians) * cos(degreeRadians))
              let pointLonRadians: Double = centerLonRadians + atan2(sin(degreeRadians) * sin(distRadians) * cos(centerLatRadians), cos(distRadians) - sin(centerLatRadians) * sin(pointLatRadians))
              let pointLat: Double = pointLatRadians * 180 / Double.pi
              let pointLon: Double = pointLonRadians * 180 / Double.pi
              let point: CLLocationCoordinate2D = CLLocationCoordinate2DMake(pointLat, pointLon)
              coordinates.append(point)
          }
          return coordinates
      }
      
      
      func calculateWaypoints(_ callback:@escaping () -> Void) -> Void {
        
        var sortedPoints : [CLLocationCoordinate2D] = []
        
        if checkNearsetPoint == true
        {
             sortedPoints = changeBeginningOfSmartRunWithnearestPoint(object!.route.coordinatesGPX, positionGPS: map.userLocation!.location)
        }
        else
        {
             sortedPoints = object!.route.coordinatesGPX
        }
         
          for item in sortedPoints
          {
              waypoints.append(Waypoint.init(coordinate: item))
          }
          if (waypoints.count > 100)
          {
              waypoints = self.simplifyRoute(waypoints: self.waypoints)
              callback()
          }
          else
          {
              callback()
          }
          
          
      }
    
    
    func findNearestGPXPoint(_ myTablePointGPX: Array<CLLocationCoordinate2D>, positionGPS: CLLocation) -> Int{
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
    
    func changeBeginningOfSmartRunWithnearestPoint( _ myTablePointGPX: Array<CLLocationCoordinate2D>, positionGPS: CLLocation!) -> Array<CLLocationCoordinate2D>{
        guard positionGPS != nil else {
            return myTablePointGPX
        }
        let nearestPointGPX = findNearestGPXPoint(myTablePointGPX, positionGPS: positionGPS)
        var newTablePointGPX: Array<CLLocationCoordinate2D> = []
        for j in nearestPointGPX ..< myTablePointGPX.count {
            newTablePointGPX.append(myTablePointGPX[j])
        }
        for k in 0 ..< nearestPointGPX {
            newTablePointGPX.append(myTablePointGPX[k])
        }
        return newTablePointGPX
    }
    
    
    func simplifyRoute(waypoints: [Waypoint]) -> [Waypoint] {
        var retunredWayPoints : [Waypoint] = []
        for i in 2...100
        {
            let rest = waypoints.count % i
            if rest < 100
            {
                for (index,item ) in waypoints.enumerated()
                {
                    if index == 0 || (index+1) == waypoints.count
                    {
                        retunredWayPoints.append(item)
                    }
                    else if (index % (i+1)) == 0
                    {
                        retunredWayPoints.append(item)
                    }
                }
                return retunredWayPoints
            }
        }
        return retunredWayPoints
    }

    
    
    func splitedString(string: String, length: Int) -> [String] {
        
        var str = ""
        var strs : [String] = []
        let splitStr = string.split(separator: ".")
        for (index,item) in splitStr.enumerated()
        {
            let nbr = str.count + item.count
            if nbr  < length
            {
                str  = "\(str)\(item)"
                if index+1 == splitStr.count
                {
                    strs.append(str)
                }
            }
            else
            {
                strs.append(str)
                str = String(item)
            }
        }
        return strs
    }
    

}
