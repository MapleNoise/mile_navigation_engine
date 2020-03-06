//
//  NavigationRouteView+MGLMapViewDelegate.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/25/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import Mapbox
import MapboxNavigation

extension NavigationRouteView: MGLMapViewDelegate {
    
    func mapViewDidFinishLoadingMap(_ mapView: MGLMapView) {
        isMapReady = true
        mapReadyResult?(nil)
        calculateWaypoints {
            //self.initTheNavigationtoTheRoute()
        }
    }
    
    func mapView(_ mapView: MGLMapView, alphaForShapeAnnotation annotation: MGLShape) -> CGFloat {
        // Set the alpha for all shape annotations to 1 (full opacity)
        return 1
    }
    
    func mapView(_ mapView: MGLMapView, lineWidthForPolylineAnnotation annotation: MGLPolyline) -> CGFloat {
        // Set the line width for polyline annotations
        return 2.0
    }
    
    
    func mapView(_ mapView: MGLMapView, annotationCanShowCallout annotation: MGLAnnotation) -> Bool {
        
        
        
        return true
    }
    
    func mapView(_ mapView: MGLMapView, strokeColorForShapeAnnotation annotation: MGLShape) -> UIColor {
        // Give our polyline a unique color by checking for its `title` property
        if (annotation.title == "Crema to Council Crest" && annotation is MGLPolyline) {
            // Mapbox cyan
            return UIColor(red: 59/255, green: 178/255, blue: 208/255, alpha: 1)
        } else {
            return Style.accentColor
        }
    }
    func mapView(_ mapView: MGLMapView, fillColorForPolygonAnnotation annotation: MGLPolygon) -> UIColor {
        if annotation is  DirectionalPointPolygon
        {
            return UIColor.green
        }
        else if annotation is BloquantPointPolygon
        {
            return UIColor.red
        }
        else if annotation is SecondPositionPointPolygon
        {
            return UIColor.magenta
        }
        else
        {
            return Style.primaryColor
        }
        
    }
    
    func mapView(_ mapView: MGLMapView, viewFor annotation: MGLAnnotation) -> MGLAnnotationView? {
        if annotation is MyCustomPointAnnotation {
            // Assign a reuse identifier to be used by both of the annotation views, taking advantage of their similarities.
            let reuseIdentifier = "reusableDotView"
            // For better performance, always try to reuse existing annotations.
            var annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: reuseIdentifier)
            // If there’s no reusable annotation view available, initialize a new one.
            if annotationView == nil {
                annotationView = MGLAnnotationView(reuseIdentifier: reuseIdentifier)
                annotationView?.frame = CGRect(x: 0, y: 0, width: 30, height: 30)
                annotationView?.layer.cornerRadius = (annotationView?.frame.size.width)! / 2
                annotationView?.layer.borderWidth = 4.0
                annotationView?.layer.borderColor = UIColor.white.cgColor
                annotationView!.backgroundColor = UIColor(red: 0.03, green: 0.80, blue: 0.69, alpha: 1.0)
            }
            
            return annotationView
        }
        else
        {
            let reuseIdentifier = "reusableDotView"
            let annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: reuseIdentifier)
            return annotationView
        }
        
    }
}
