//
//  UIView+Extension.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/18/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import UIKit


public extension UIView
{
    
    func createGradientLayer(gradientLayer:CAGradientLayer) {
        //        gradientLayer.frame = self.bounds
        //        gradientLayer.colors = [UIColor(red:1.00, green:1.00, blue:1.00, alpha:0).cgColor, UIColor(red:0.00, green:0.15, blue:0.28, alpha:0.6).cgColor]
        //        self.layer.addSublayer(gradientLayer)
        
        
        let colorSet =  [UIColor(red:0.00, green:0.15, blue:0.28, alpha:0.1), UIColor(red:0.00, green:0.15, blue:0.28, alpha:0.5)]
        let location = [0.2, 1.0]
        self.addGradient(with: gradientLayer, colorSet: colorSet, locations: location)
    }
    
    func cardBreezometerStyle() -> Void {
        self.backgroundColor = Style.cardColor
        self.layer.cornerRadius = 18
    }
    
    func addShadow() -> Void {
        self.layer.shadowPath =
            UIBezierPath(roundedRect: self.bounds,
                         cornerRadius: self.layer.cornerRadius).cgPath
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOpacity = 0.5
        self.layer.shadowOffset = CGSize(width: 2, height: 2)
        self.layer.shadowRadius = self.layer.cornerRadius
        self.layer.masksToBounds = false
        
    }
    
    func cardStyle() -> Void {
        self.layer.cornerRadius = 10
        self.backgroundColor = Style.cardColor
        self.layer.applySketchShadow(alpha: 1, x: 0, y: 10, blur: 20, spread: 0)
    }
    
    
    
    func findViewController() -> UIViewController? {
        if let nextResponder = self.next as? UIViewController {
            return nextResponder
        } else if let nextResponder = self.next as? UIView {
            return nextResponder.findViewController()
        } else {
            return nil
        }
    }
    
    func addGradient(with layer: CAGradientLayer, gradientFrame: CGRect? = nil, colorSet: [UIColor],
                     locations: [Double], startEndPoints: (CGPoint, CGPoint)? = nil) {
        layer.frame = gradientFrame ?? self.bounds
        layer.frame.origin = .zero
        
        let layerColorSet = colorSet.map { $0.cgColor }
        let layerLocations = locations.map { $0 as NSNumber }
        
        layer.colors = layerColorSet
        layer.locations = layerLocations
        
        if let startEndPoints = startEndPoints {
            layer.startPoint = startEndPoints.0
            layer.endPoint = startEndPoints.1
        }
        
        self.layer.insertSublayer(layer, above: self.layer)
    }
    
    
    

    
    
}



extension UIViewController {
    var layoutInsets: UIEdgeInsets {
        if #available(iOS 11.0, *) {
            return view.safeAreaInsets
        } else {
            return UIEdgeInsets(top: topLayoutGuide.length,
                                left: 0.0,
                                bottom: bottomLayoutGuide.length,
                                right: 0.0)
        }
    }

    var layoutGuide: LayoutGuideProvider {
        if #available(iOS 11.0, *) {
            return view!.safeAreaLayoutGuide
        } else {
            return CustomLayoutGuide(topAnchor: topLayoutGuide.bottomAnchor,
                                     bottomAnchor: bottomLayoutGuide.topAnchor)
        }
    }
}


protocol LayoutGuideProvider {
    var topAnchor: NSLayoutYAxisAnchor { get }
    var bottomAnchor: NSLayoutYAxisAnchor { get }
}
extension UILayoutGuide: LayoutGuideProvider {}

class CustomLayoutGuide: LayoutGuideProvider {
    let topAnchor: NSLayoutYAxisAnchor
    let bottomAnchor: NSLayoutYAxisAnchor
    init(topAnchor: NSLayoutYAxisAnchor, bottomAnchor: NSLayoutYAxisAnchor) {
        self.topAnchor = topAnchor
        self.bottomAnchor = bottomAnchor
    }
}




extension UIViewController
{
    func backToSpeceficViewController(viewController:AnyClass ) -> Void {
        for controller in self.navigationController!.viewControllers as Array {
            if controller.isKind(of: viewController.self) {
                self.navigationController!.popToViewController(controller, animated: true)
                break
            }
        }
    }
    
}
