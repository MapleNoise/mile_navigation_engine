//
//  NavigationEntity.swift
//  MyThalassa
//
//  Created hassine othmane on 9/23/19.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

/// Navigation Module Entity
struct NavigationEntity {
    var route:RouteInformmation!
    var pois:[PointOfInterest]!
    var directionals:[PointOfInterest]!
    var checkNearsetPoint : Bool
    var startingPoint : Int = 0
}
