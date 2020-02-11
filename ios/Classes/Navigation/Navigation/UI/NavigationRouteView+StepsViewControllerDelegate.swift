//
//  NavigationRouteView+StepsViewControllerDelegate.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/24/19.
//  Copyright © 2019 mile. All rights reserved.
//

import MapboxCoreNavigation
import Mapbox
import MapboxNavigation

extension NavigationRouteView: StepsViewControllerDelegate {
    
    func didDismissStepsViewController(_ viewController: StepsViewController) {
        viewController.dismiss { [weak self] in
            self?.stepsViewController = nil
        }
    }

    func stepsViewController(_ viewController: StepsViewController, didSelect legIndex: Int, stepIndex: Int, cell: StepTableViewCell) {
        viewController.dismiss { [weak self] in
            self?.stepsViewController = nil
        }
    }
}
