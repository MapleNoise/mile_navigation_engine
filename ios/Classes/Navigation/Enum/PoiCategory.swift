//
//  PoiCategory.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/25/19.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit


enum PoiCategory: Int {

    case undefined = 0
    case monument = 1
    case culture = 2
    case divertissement = 3
    case marchand = 4
    case ruePlaceQuartier = 5
    case religion = 6
    case nature = 7
    case eau = 8

    case directionnelHoraire = 9
    case directionnelAntiHoraire = 10

    case noAnnotation = 11
    case theme = 12
    case sport = 13

    func image() -> UIImage? {
        switch self {
        case .culture:
            return  UIImage(named: "pin_culture")
        case .divertissement:
            return  UIImage(named: "pin_loisir")
        case .eau:
            return  UIImage(named: "pin_eau")
        case .marchand:
            return  UIImage(named: "pin_marchand")
        case .monument:
            return  UIImage(named: "pin_monument")
        case .nature:
            return  UIImage(named: "pin_nature")
        case .religion:
            return  UIImage(named: "pin_religion")
        case .ruePlaceQuartier:
            return  UIImage(named: "pin_quartier")
        case .undefined:
            return  UIImage(named: "pin_undefined")
        case .noAnnotation:
            //No annotation for this category
            return nil
        case .theme:
            return  UIImage(named: "pin_theme")
        case .sport:
                       return  UIImage(named: "pin_theme")
        case .directionnelHoraire:
            //No annotation for this category
            return nil
        case .directionnelAntiHoraire:
            //No annotation for this category
            return nil
        }
    }
}
