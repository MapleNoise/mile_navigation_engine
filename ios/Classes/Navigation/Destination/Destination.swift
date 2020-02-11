//
//  Destination.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/20/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation


struct Destination {
    
    var key: String
    // var country: Country?
    let ref: String
    private let descriptionEN : String
    private let descriptionFR : String
    let imageURL : String?
    let latitude : Double?
    let longitude : Double?
    private let nameEN : String
    private let nameFR :  String
    var routesID : [String] = []
    var hotelsID : [String] = []


    var description: String {
                     get {
                         if "fr" == Utils.getLanguageISO()
                         {
                             return descriptionFR
                         }
                         else
                         {
                             return descriptionEN
                         }
                     }
                 }

              var name: String {
                     get {
                         if "fr" == Utils.getLanguageISO()
                         {
                             return nameFR
                         }
                         else
                         {
                             return nameEN
                         }
                     }
                 }

    
    init(snapshotValue: [String: Any]) {
        descriptionEN = snapshotValue["DescriptionEN"] as? String ?? ""
        descriptionFR = snapshotValue["DescriptionFR"] as? String ?? ""
        nameEN = snapshotValue["NameEN"] as? String ?? ""
        nameFR = snapshotValue["NameFR"] as? String ?? ""
        longitude = snapshotValue["Longitude"] as? Double
        latitude = snapshotValue["Latitude"] as? Double
        imageURL = snapshotValue["imageURL"] as? String ?? ""
        if let routes = snapshotValue["Route"] as? NSMutableDictionary
        {
            for route in routes
            {
                routesID.append(route.key as! String)
            }
        }
        if let hostels = snapshotValue["listHotels"] as? NSMutableDictionary
        {
            for hostel in hostels
            {
                hotelsID.append(hostel.key as! String)
            }
        }

        ref = ""
        key = ""
    }
    
    init() {
        key = ""
        descriptionEN = ""
        descriptionFR = ""
        nameEN = ""
        nameFR = ""
        imageURL = nil
        longitude = nil
        latitude = nil
        ref = ""
    }
    
    func toAnyObject() -> Any {
        return [
            //            "id": id,
            //            "departure": departure,
            //            "arrival": arrival,
            //            "company":company,
            //            "departureHour":departureHour,
            //            "arrivalHour":arrivalHour,
            
        ]
    }
}
