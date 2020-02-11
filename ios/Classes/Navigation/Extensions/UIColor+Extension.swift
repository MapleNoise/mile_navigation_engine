//
//  UIColor+Extension.swift
//  MyThalassa
//
//  Created by hassine othmane on 10/8/19.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit

public extension UIColor {
    convenience init(hex: Int, alpha: CGFloat = 1) {
        let r = CGFloat((hex & 0xFF0000) >> 16) / 255
        let g = CGFloat((hex & 0xFF00) >> 8) / 255
        let b = CGFloat((hex & 0xFF)) / 255
        self.init(red: r, green: g, blue: b, alpha: alpha)
    }
}
