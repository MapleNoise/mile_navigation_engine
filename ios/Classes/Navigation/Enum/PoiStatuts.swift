//
//  PoiStatuts.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/24/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation

enum PoiStatuts
{
    case notPlayed
    case isPlaying
    case played
    case paused
    case none
}



enum TypeRoute : Int
{
    case walking = 1
    case run = 2
    case bike = 3
    case none = -1
}
