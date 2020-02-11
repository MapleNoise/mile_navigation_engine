//
//  CALayer+Extension.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/19/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import UIKit


public extension CALayer {
    func applySketchShadow(
        color: UIColor = .black,
        alpha: Float ,
        x: CGFloat ,
        y: CGFloat ,
        blur: CGFloat,
        spread: CGFloat)
    {
        shadowColor = color.cgColor
        shadowOpacity = alpha
        shadowOffset = CGSize(width: x, height: y)
        shadowRadius = blur / 2.0
        if spread == 0 {
            shadowPath = nil
        } else {
            let dx = -spread
            let rect = bounds.insetBy(dx: dx, dy: dx)
            shadowPath = UIBezierPath(rect: rect).cgPath
        }
    }
}
