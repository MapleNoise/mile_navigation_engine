//
//  StatisticsPresenter.swift
//  MyThalassa
//
//  Created Hassine on 04/11/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

/// Statistics Module Presenter
class StatisticsPresenter {
    
    weak private var _view: StatisticsViewProtocol?
    private var interactor: StatisticsInteractorProtocol
    private var wireframe: StatisticsRouterProtocol
    
    init(view: StatisticsViewProtocol) {
        self._view = view
        self.interactor = StatisticsInteractor()
        self.wireframe = StatisticsRouter()
    }
}

// MARK: - extending StatisticsPresenter to implement it's protocol
extension StatisticsPresenter: StatisticsPresenterProtocol {
    func fetch(objectFor view: StatisticsViewProtocol) {
        
    }
    
    func interactor(_ interactor: StatisticsInteractorProtocol, didFetch object: StatisticsEntity) {
        
    }
    
    func interactor(_ interactor: StatisticsInteractorProtocol, didFailWith error: Error) {
        
    }
    
    
}
