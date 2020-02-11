//
//  PoiBanner.swift
//  MyThalassa
//
//  Created by Hassine on 13/01/2020.
//  Copyright © 2020 mile. All rights reserved.
//

import UIKit

class PoiBanner: UIView {

    @IBOutlet weak var showDetails_btn: UIButton!
    @IBOutlet weak var widthConstraint: NSLayoutConstraint!
    @IBOutlet weak var namePoi_lbl: UILabel!
    @IBOutlet weak var descirption_lbl: UILabel!
    
    
     var delegate: NavigationViewControllerDelegate?
    var poi: PointOfInterest?
    
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */
    
    class func instanceFromNib() -> PoiBanner {
        var nib = UINib(nibName: "PoiBanner", bundle: nil).instantiate(withOwner: nil, options: nil)[0] as! PoiBanner
        nib.frame = CGRect.init(x: 0, y: 0, width: UIScreen.main.bounds.width, height: 150)
        return nib
    }
    
 
    @IBAction func displayDescription(_ sender: Any) {
        delegate?.displayPoi(poi: poi!)
    }
    
}
