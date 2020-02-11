//
//  StatisticsContracts.swift
//  MyThalassa
//
//  Created Hassine on 04/11/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

//MARK: View -
/*
 Should replace "class" with "BaseViewProtocol" if available;
 & that will allow the View to act as a UIViewController;
 & Implement common view functions.
 */
/// Statistics Module View Protocol
protocol StatisticsViewProtocol: class {
    // Update UI with value returned.
    /// Set the view Object of Type StatisticsEntity
    func set(object: StatisticsEntity)
}

//MARK: Interactor -
/// Statistics Module Interactor Protocol
protocol StatisticsInteractorProtocol {
    // Fetch Object from Data Layer
    func fetch(objectFor presenter: StatisticsPresenterProtocol)
}

//MARK: Presenter -
/// Statistics Module Presenter Protocol
protocol StatisticsPresenterProtocol {
    /// The presenter will fetch data from the Interactor thru implementing the Interactor fetch function.
    func fetch(objectFor view: StatisticsViewProtocol)
    /// The Interactor will inform the Presenter a successful fetch.
    func interactor(_ interactor: StatisticsInteractorProtocol, didFetch object: StatisticsEntity)
    /// The Interactor will inform the Presenter a failed fetch.
    func interactor(_ interactor: StatisticsInteractorProtocol, didFailWith error: Error)
}

//MARK: Router (aka: Wireframe) -
/// Statistics Module Router Protocol
protocol StatisticsRouterProtocol {
    // Show Details of Entity Object coming from ParentView Controller.
    // func showDetailsFor(object: StatisticsEntity, parentViewController viewController: UIViewController)
}
