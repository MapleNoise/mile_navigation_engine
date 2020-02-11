//
//  NavigationPresenter.swift
//  MyThalassa
//
//  Created hassine othmane on 9/23/19.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

/// Navigation Module Presenter
class NavigationPresenter {
    
    weak private var _view: NavigationViewProtocol?
    private var interactor: NavigationInteractorProtocol
    private var wireframe: NavigationRouterProtocol
    
    init(view: NavigationViewProtocol) {
        self._view = view
        self.interactor = NavigationInteractor()
        self.wireframe = NavigationRouter()
    }
}

// MARK: - extending NavigationPresenter to implement it's protocol
extension NavigationPresenter: NavigationPresenterProtocol {
    
    func fetch(objectFor view: NavigationViewProtocol) {

    }

    func interactor(_ interactor: NavigationInteractorProtocol, didFetch object: NavigationEntity) {

    }

    func interactor(_ interactor: NavigationInteractorProtocol, didFailWith error: Error) {
        
    }
    
    
    func navigateToStatistics(object: StatisticsEntity, parentViewController: UIViewController) {
          wireframe.showStatisticsFor(object: object, parentViewController: parentViewController)
      }

    
}
