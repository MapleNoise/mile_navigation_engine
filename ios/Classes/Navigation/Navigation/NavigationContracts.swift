//
//  NavigationContracts.swift
//  MyThalassa
//
//  Created hassine othmane on 9/23/19.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

//MARK: View -
/*
 Should replace "class" with "BaseViewProtocol" if available;
 & that will allow the View to act as a UIViewController;
 & Implement common view functions.
 */
/// Navigation Module View Protocol
protocol NavigationViewProtocol: class {
    // Update UI with value returned.
    /// Set the view Object of Type NavigationEntity
    func set(object: NavigationEntity)
}

//MARK: Interactor -
/// Navigation Module Interactor Protocol
protocol NavigationInteractorProtocol {
    // Fetch Object from Data Layer
    func fetch(objectFor presenter: NavigationPresenterProtocol)
}

//MARK: Presenter -
/// Navigation Module Presenter Protocol
protocol NavigationPresenterProtocol {
    /// The presenter will fetch data from the Interactor thru implementing the Interactor fetch function.
     func navigateToStatistics(object: StatisticsEntity, parentViewController: UIViewController)
    /// The presenter will fetch data from the Interactor thru implementing the Interactor fetch function.
    func fetch(objectFor view: NavigationViewProtocol)
    /// The Interactor will inform the Presenter a successful fetch.
    func interactor(_ interactor: NavigationInteractorProtocol, didFetch object: NavigationEntity)
    /// The Interactor will inform the Presenter a failed fetch.
    func interactor(_ interactor: NavigationInteractorProtocol, didFailWith error: Error)
}

//MARK: Router (aka: Wireframe) -
/// Navigation Module Router Protocol
protocol NavigationRouterProtocol {
    // Show Details of Entity Object coming from ParentView Controller.
     func showStatisticsFor(object: StatisticsEntity, parentViewController viewController: UIViewController)
    
}
