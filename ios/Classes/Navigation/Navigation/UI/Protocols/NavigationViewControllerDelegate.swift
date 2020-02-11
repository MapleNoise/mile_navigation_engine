//
//  NavigationViewControllerDelegate.swift
//  MyThalassa
//
//  Created by Hassine on 14/01/2020.
//  Copyright © 2020 mile. All rights reserved.
//



import Foundation
 
import UIKit
 
// NavigationRouteView+NavigationViewControllerDelegate
// This is the DELEGATE PROTOCOL
//
protocol NavigationViewControllerDelegate {
    // Classes that adopt this protocol MUST define
    // this method -- and hopefully do something in
    // that definition.
    func displayPoi(poi:PointOfInterest)
}
