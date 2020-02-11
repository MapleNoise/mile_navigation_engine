//
//  NavigationRouter.swift
//  MyThalassa
//
//  Created hassine othmane on 9/23/19.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

/// Navigation Module Router (aka: Wireframe)
class NavigationRouter: NavigationRouterProtocol {
    
    func showStatisticsFor(object: StatisticsEntity, parentViewController viewController: UIViewController) {
        
        let controller = StatisticsRouter.createViewController(parentViewController: viewController) as! StatisticsView
            controller.set(object: object)
              viewController.show(controller, sender: nil)
    }
    
    
    

    

    static var mainStoryboard: UIStoryboard {
        return UIStoryboard(name: "Navigation", bundle: Bundle.main)
    }

 
}
