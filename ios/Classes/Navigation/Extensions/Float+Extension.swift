//
//  Float+Extension.swift
//  MyThalassa
//
//  Created by Hassine on 09/10/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation


public extension Int
{

    
    func secondsToHoursMinutesSeconds (seconds : Int) -> (String) {
        
     
        return ("\(seconds / 3600):\(seconds % 3600)")
    }

}
