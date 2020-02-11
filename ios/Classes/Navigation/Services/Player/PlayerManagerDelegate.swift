//
//  PlayerManagerDelegate.swift
//  MyThalassa
//
//  Created by Hassine on 09/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation


protocol PlayerManagerDelegate {
    func updateTimer(playerStatus: Float, currentTime: Float) -> Void
    func  PlayerManagerDidFinishPoiDesciption() -> Void
    func  PlayerManagerDidFinishDirectionnal() -> Void
}
