//
//  StatisticsView.swift
//  MyThalassa
//
//  Created Hassine on 04/11/2019.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

/// Statistics Module View
class StatisticsView: UIViewController {
    
    @IBOutlet weak var titile_lbl: UILabel!
    @IBOutlet weak var finish_btn: UIButton!
    @IBOutlet weak var question_lbl: UILabel!
    
    @IBOutlet weak var timeTitle_lbl: UILabel!
       @IBOutlet weak var timeValue_lbl: UILabel!
       @IBOutlet weak var distanceTitle_lbl: UILabel!
       @IBOutlet weak var distanceValue_lbl: UILabel!
       @IBOutlet weak var speedTitle_lbl: UILabel!
       @IBOutlet weak var speedValue_lbl: UILabel!
    
    
    private let ui = StatisticsViewUI()
    private var presenter: StatisticsPresenterProtocol!
    
    private var object : StatisticsEntity?
    
//    override func loadView() {
//        // setting the custom view as the view controller's view
//        ui.delegate = self
//        ui.dataSource = self
//        view = ui
//    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter = StatisticsPresenter(view: self)
        
        // Informs the Presenter that the View is ready to receive data.
        presenter.fetch(objectFor: self)
        finish_btn.primaryStyle()
        titile_lbl.title1Style()
        question_lbl.textBodyStyle()
        question_lbl.text = "How was your experience ?".localized
        finish_btn.setTitle("Finish".localized, for: UIControl.State.normal)
        self.timeTitle_lbl.text = "Time".localized
        self.speedTitle_lbl.text = "Speed".localized
        self.timeTitle_lbl.titleStatistics()
        
        self.timeValue_lbl.valueStatistics()
        timeValue_lbl.textColor = Style.primaryColor
        self.distanceTitle_lbl.titleStatistics()
        self.distanceValue_lbl.valueStatistics()
        distanceValue_lbl.textColor = Style.primaryColor
        self.speedTitle_lbl.titleStatistics()
        self.speedValue_lbl.valueStatistics()
        speedValue_lbl.textColor = Style.primaryColor
        
        timeValue_lbl.text = object?.time
        speedValue_lbl.text = object?.speed
        distanceValue_lbl.text = object?.distance
        titile_lbl.text = "Statistics".localized
        
    }
    @IBAction func finish_action(_ sender: Any) {
      //    self.backToSpeceficViewController(viewController: HomeView.self)
    }
    
}

// MARK: - extending StatisticsView to implement it's protocol
extension StatisticsView: StatisticsViewProtocol {
    func set(object: StatisticsEntity) {
        self.object = object
    }
    
    
}

// MARK: - extending StatisticsView to implement the custom ui view delegate
extension StatisticsView: StatisticsViewUIDelegate {
    
}

// MARK: - extending StatisticsView to implement the custom ui view data source
extension StatisticsView: StatisticsViewUIDataSource {
    func objectFor(ui: StatisticsViewUI) -> StatisticsEntity {
        return StatisticsEntity()
    }
    
    // Pass the pre-defined object to the dataSource.
}
