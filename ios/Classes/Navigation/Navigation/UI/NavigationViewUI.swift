//
//  NavigationViewUI.swift
//  MyThalassa
//
//  Created hassine othmane on 9/23/19.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

// MARK: NavigationViewUI Delegate -
/// NavigationViewUI Delegate
protocol NavigationViewUIDelegate {
    // Send Events to Module View, that will send events to the Presenter; which will send events to the Receiver e.g. Protocol OR Component.
}

// MARK: NavigationViewUI Data Source -
/// NavigationViewUI Data Source
protocol NavigationViewUIDataSource {
    // This will be implemented in the Module View.
    /// Set Object for the UI Component
    func objectFor(ui: NavigationViewUI) -> NavigationEntity
}

class NavigationViewUI: UIView {
    
    var delegate: NavigationViewUIDelegate?
    var dataSource: NavigationViewUIDataSource?
    
    var object : NavigationEntity?
    
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
