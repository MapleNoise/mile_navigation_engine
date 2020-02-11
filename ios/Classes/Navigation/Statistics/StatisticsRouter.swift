//
//  StatisticsRouter.swift
//  MyThalassa
//
//  Created Hassine on 04/11/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

/// Statistics Module Router (aka: Wireframe)
class StatisticsRouter: StatisticsRouterProtocol {
      
    static var mainStoryboard: UIStoryboard {
            return UIStoryboard(name: "Statistics", bundle: Bundle.main)
    }
    
    static func createViewController( parentViewController viewController: UIViewController) -> UIViewController {
           let controller = StatisticsRouter.mainStoryboard.instantiateViewController(withIdentifier: "StatisticsView") as! StatisticsView
           return controller
       }
    
}
