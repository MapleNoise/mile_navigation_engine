//
//  UILabel+Extension.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/15/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import UIKit


public extension UILabel
{
    func textBodyStyle() -> Void {

        self.font = UIFont.init(name: "Gilroy-Regular", size: 14.0)
        self.textColor = Style.textBodyColor
        let attributedString = NSMutableAttributedString(string: self.text ?? "")
        let paragraphStyle = NSMutableParagraphStyle()
        paragraphStyle.lineSpacing = 3
        paragraphStyle.alignment = .left
        attributedString.addAttribute(NSAttributedString.Key.paragraphStyle, value:paragraphStyle, range:NSMakeRange(0, attributedString.length))
        self.attributedText = attributedString
    }

    func titleStyle() -> Void {
        self.font = UIFont.init(name: "Gilroy-Semibold", size: 26.0)
        self.textColor = Style.titleColor
    }

    func title1Style() -> Void {
        self.font = UIFont.init(name: "Gilroy-Semibold", size: 20.0)
        self.textColor = Style.titleColor
    }

    func title2Style() -> Void {
        self.font = UIFont.init(name: "Gilroy-Semibold", size: 18.0)
        self.textColor = Style.titleColor
    }

    func title3Style() -> Void {
        self.font = UIFont.init(name: "Gilroy-Semibold", size: 16.0)
        self.textColor = Style.titleColor
    }
    
    func titleWebPageStyle() -> Void {
           self.font = UIFont.init(name: "Gilroy-Semibold", size: 15.0)
           self.textColor = Style.titleColor
       }

    func progressTimeStyle() -> Void {
        self.font = UIFont.init(name: "Gilroy-Semibold", size: 14.0)
        self.textColor = UIColor(red:0.96, green:0.64, blue:0.19, alpha:1.0)
    }

    func titleCardStyle() -> Void {
        self.font = UIFont.init(name: "Gilroy-Semibold", size: 14.0)
        self.textColor = UIColor.white
    }
    
    func bodyCardStyle() -> Void {
        self.font = UIFont.init(name: "Gilroy-Bold", size: 20.0)
        self.textColor = UIColor.white
    }

    func titleNavigationCard() -> Void {
        self.font = UIFont.init(name: "Gilroy-Medium", size: 12.0)
        self.textColor = UIColor(red:0.56, green:0.56, blue:0.58, alpha:1.0)
    }
    
    func titleStatistics() -> Void {
        self.font = UIFont.init(name: "Gilroy-Medium", size: 14.0)
        self.textColor = UIColor(red:0.56, green:0.56, blue:0.58, alpha:1.0)
    }

    func valueNavigationCard() -> Void {
        self.font = UIFont.init(name: "Gilroy-Medium", size: 16.0)
        self.textColor = UIColor.white
       }
    
    func valueStatistics() -> Void {
     self.font = UIFont.init(name: "Gilroy-Medium", size: 14.0)
        self.textColor = Style.primaryColor
    }



    func titleDestinationStyle() -> Void {
        self.font = UIFont.init(name: "Gilroy-Bold", size: 50.0)
        self.textColor = UIColor.white
    }

    func titleDestinationCradStyle() -> Void {
          self.font = UIFont.init(name: "Gilroy-Bold", size: 18.0)
          self.textColor = UIColor.white
      }
    
    func titleRouteCradStyle() -> Void {
        self.font = UIFont.init(name: "Gilroy-Semibold", size: 16.0)
        self.textColor = UIColor.white
    }
    
    func detailRouteCradStyle() -> Void {
         self.font = UIFont.init(name: "Gilroy-Semibold", size: 12.0)
        self.textColor = UIColor.gray
     }




    func countLabelLines() -> Int {
        // Call self.layoutIfNeeded() if your view uses auto layout
        let myText = self.text! as NSString

        let rect = CGSize(width: self.bounds.width, height: CGFloat.greatestFiniteMagnitude)
        let labelSize = myText.boundingRect(with: rect, options: .usesLineFragmentOrigin, attributes: [NSAttributedString.Key.font: self.font!], context: nil)
            return Int(ceil(CGFloat(labelSize.height) / self.font.lineHeight))


    }

}

extension UITextView
{

    func textBodyStyle() -> Void {

        self.font = UIFont.init(name: "Gilroy-Regular", size: 14.0)
        self.textColor = Style.textBodyColor
        self.isEditable = false
        
        
        let attributedString = NSMutableAttributedString(string: self.text!)
        let paragraphStyle = NSMutableParagraphStyle()
        paragraphStyle.lineSpacing = 3
        paragraphStyle.alignment = .left
        self.isEditable = false
        self.showsVerticalScrollIndicator = false
        attributedString.addAttribute(NSAttributedString.Key.paragraphStyle, value:paragraphStyle, range:NSMakeRange(0, attributedString.length))
        self.attributedText = attributedString
        self.backgroundColor = .white
    }
    

    func title2Style() -> Void {
           self.font = UIFont.init(name: "Gilroy-Semibold", size: 18.0)
           self.textColor = Style.titleColor
    }

}
