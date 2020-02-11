//
//  Extensions.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/15/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import UIKit


public extension UIButton
{
    func primaryStyle() -> Void {
        DispatchQueue.main.async {
            self.backgroundColor = Style.primaryColor
            self.titleLabel!.font = UIFont.init(name: "Gilroy-Regular", size: 16.0)
            self.setTitleColor(UIColor.white, for: .normal)
            self.layer.cornerRadius = 8
        }
    }

    func secondaryStyle() -> Void {
        DispatchQueue.main.async {
            self.backgroundColor = Style.accentColor
            self.titleLabel!.font = UIFont.init(name: "Gilroy-Regular", size: 16.0)
             self.setTitleColor(UIColor.white, for: .normal)

        }
    }
    func secondaryTextStyle() -> Void {
        DispatchQueue.main.async {
            self.titleLabel!.font = UIFont.init(name: "Gilroy-Semibold", size: 14.0)
            self.setTitleColor(Style.accentColor, for: .normal)

        }
    }



    func tertiaryStyle() -> Void {
        DispatchQueue.main.async {
            self.backgroundColor = UIColor.white
            self.titleLabel!.font = UIFont.init(name: "Gilroy-Semibold", size: 16.0)
            self.setTitleColor(Style.primaryColor, for: .normal)
            self.titleLabel?.textAlignment = .center

        }
    }

    func backStyle() -> Void {
        DispatchQueue.main.async {
            self.backgroundColor = UIColor.white
            self.layer.cornerRadius = self.frame.width/2
            self.setTitle("", for: UIControl.State.normal)
           // self.setImage(UIImage.init(named: "CommonIcons/back_ic"), for: UIControl.State.normal)
            self.tintColor = Style.accentColor

        }
    }
    
    func closeStyle() -> Void {
        DispatchQueue.main.async {
            self.backgroundColor = UIColor.white
            self.setTitle("", for: UIControl.State.normal)
            self.setImage(UIImage.init(named: "CommonIcons/close_ic"), for: UIControl.State.normal)
            self.tintColor = Style.accentColor

        }
    }


}

