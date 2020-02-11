//
//  NavigationRouteView+drawFunctions.swift
//  MyThalassa
//
//  Created by Hassine on 30/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import Mapbox


extension NavigationRouteView
{
    
    
    func addPointOfInterest() -> Void {
        for(index, poi) in object!.route.pois.enumerated()
        {
            let annot = MGLPointAnnotation()
            if let first = poi.coordinatesGPX.first
            {
                annot.coordinate = first
                annot.title = "\(index)"
                map.addAnnotation(annot)
                
                
                #if DEBUG
                /*if AppDelegate.testMode == true
                {
                    for (index,coordinate) in  poi.coordinatesGPX.enumerated()
                    {
                        if index == 0
                        {
                            
                            var coordinates = calculateCoordinates(coordinate: coordinate, withMeterRadius: Double(poi.range))
                            let polygon = MGLPolygon(coordinates: &coordinates, count: UInt(coordinates.count))
                            self.map.addAnnotation(polygon)
                        }
                        else
                        {
                            var coordinates = calculateCoordinates(coordinate: coordinate, withMeterRadius: Double(poi.range))
                            let polygon = SecondPositionPointPolygon(coordinates: &coordinates, count: UInt(coordinates.count))
                            self.map.addAnnotation(polygon)
                        }
                        
                    }
                    
                }*/
                #endif
            }
            

            
        }
        #if DEBUG
        /*if AppDelegate.testMode == true
        {
            for poi in object!.route.directionals
            {
                
                
                
                if poi.isBloquant == true
                {
                    
                    let annot = BloquantPointAnnotation()
                    annot.coordinate = poi.coordinatesGPX.first!
                    annot.title = poi.description
                    map.addAnnotation(annot)
                    var coordinates = calculateCoordinates(coordinate: annot.coordinate, withMeterRadius: Double(poi.range))
                    let polygon = BloquantPointPolygon(coordinates: &coordinates, count: UInt(coordinates.count))
                    self.map.addAnnotation(polygon)
                    
                }
                else
                {
                    let annot = DirectionalPointAnnotation()
                    annot.coordinate = poi.coordinatesGPX.first!
                    annot.title = poi.description
                    map.addAnnotation(annot)
                    var coordinates = calculateCoordinates(coordinate: annot.coordinate, withMeterRadius:  Double(poi.range))
                    let polygon = DirectionalPointPolygon(coordinates: &coordinates, count: UInt(coordinates.count))
                    
                    self.map.addAnnotation(polygon)
                }
            }}*/
        #endif
        
    }
    
    
    
    
    
    func showRoutes() -> Void {
       
        self.map.showRoutes(routes)
        //resumeNotifications()
    }
    
    
    func drawRoutePolyline() -> Void {
        //  guard route.coordinateCount > 0 else {return}
        
        var sortedPoints = self.changeBeginningOfSmartRunWithnearestPoint(self.object!.route.coordinatesGPX, positionGPS: self.map.userLocation!.location)
        Utils.generateGpxTags(coordinatesArray: sortedPoints)
        let polyline = MGLPolylineFeature.init(coordinates: &sortedPoints, count: UInt(sortedPoints.count))
        
        if let source = map.style?.source(withIdentifier: "route-source") as? MGLShapeSource
        {
            source.shape = polyline
        }
        else
        {
            let source = MGLShapeSource.init(identifier: "route-source", features: [polyline], options: nil)
            let lineStyle = MGLLineStyleLayer.init(identifier: "route-style", source: source)
            lineStyle.lineColor = NSExpression(forConstantValue: Style.primaryColor)
            lineStyle.lineWidth = NSExpression(forConstantValue: 4)
            map.style!.addSource(source)
            map.style!.addLayer(lineStyle)
        }
        addPointOfInterest()
    }
    
}
