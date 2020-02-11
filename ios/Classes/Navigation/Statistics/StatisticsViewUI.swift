//
//  StatisticsViewUI.swift
//  MyThalassa
//
//  Created Hassine on 04/11/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

// MARK: StatisticsViewUI Delegate -
/// StatisticsViewUI Delegate
protocol StatisticsViewUIDelegate {
    // Send Events to Module View, that will send events to the Presenter; which will send events to the Receiver e.g. Protocol OR Component.
}

// MARK: StatisticsViewUI Data Source -
/// StatisticsViewUI Data Source
protocol StatisticsViewUIDataSource {
    // This will be implemented in the Module View.
    /// Set Object for the UI Component
    func objectFor(ui: StatisticsViewUI) -> StatisticsEntity
}

class StatisticsViewUI: UIView {
    
    var delegate: StatisticsViewUIDelegate?
    var dataSource: StatisticsViewUIDataSource?
    
    var object : StatisticsEntity?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUIElements()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override func didMoveToWindow() {
        super.didMoveToWindow()
        setupConstraints()
    }
    
    fileprivate func setupUIElements() {
        // arrange subviews
    }
    
    fileprivate func setupConstraints() {
        // add constraints to subviews
    }
    
    /// Reloading the data and update the ui according to the new data
    func reloadData() {
        self.object = dataSource?.objectFor(ui: self)
        // Should update UI
    }
}
