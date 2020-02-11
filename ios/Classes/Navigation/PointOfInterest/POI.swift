//
//  POI.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/24/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import CoreLocation


struct PointOfInterest {
    
    var key: String
    let ref: String?
    private let descriptionEN : String
    private let descriptionFR : String
    private let nameEN : String
    private let nameFR :  String
    let imageURL : String?
    private let audioURLFr : String?
    private let audioURLEn : String?
    private let audioTextFr : String?
    private let audioTextEn : String?
    let isAutoDirectionActivated : Bool
    let isBloquant : Bool
    let isDirectionnal : Bool
    let range : Int
    var coordinatesGPX: [CLLocationCoordinate2D] = []
    var isNavigationActivated :Bool = true
    var idDestination: String
    var audioActivated: Bool = false
    var status : PoiStatuts = .notPlayed
    var type : PoiCategory
    var typeDirection : Int
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


    var audioText: String? {
           get {

               if "fr" == Utils.getLanguageISO()
               {
                   return audioTextFr
               }
               else
               {
                   return audioTextEn
               }
           }
       }


    var audioURL: String? {
            get {
              
                if "fr" == Utils.getLanguageISO()
                {
                    return audioURLFr
                }
                else
                {
                    return audioURLEn
                }
            }
        }


    init(snapshotValue: [String: Any]) {
            let arrayDescription = snapshotValue["arrayDescription"] as? [String: String]
            descriptionEN = arrayDescription!["EN"] ?? ""
            descriptionFR = arrayDescription!["FR"] ?? ""
            let arrayName = snapshotValue["arrayName"] as? [String: String]
            nameEN = arrayName!["EN"] ?? ""
            nameFR = arrayName!["FR"] ?? ""
            imageURL = snapshotValue["imageURL"] as? String ?? ""

            let arrayMP3 = snapshotValue["arrayMP3"] as? [String: String]
            audioURLFr = arrayMP3!["EN"] ?? ""
            audioURLEn = arrayMP3!["FR"] ?? ""

            let arrayTextMP3 = snapshotValue["arrayTextMP3"] as? [String: String]
            audioTextEn = arrayTextMP3!["MP3TexteEN"] ?? ""
            audioTextFr = arrayTextMP3!["MP3TexteFR"] ?? ""
            // status = snapshotValue["Status"] as? Int ?? 0
            let typeInt = snapshotValue["idType"] as? Int ?? 0
            type = PoiCategory.init(rawValue: typeInt) ?? PoiCategory.init(rawValue: 0)!
            idDestination = snapshotValue["idCity"] as? String ?? ""
            isBloquant = snapshotValue["isBloquant"] as? Bool ?? false
            isDirectionnal = snapshotValue["isDirectionnal"] as? Bool ?? false
            audioActivated = snapshotValue["audioActivated"] as? Bool ?? false
            isAutoDirectionActivated = snapshotValue["isAutoDirectionActivated"] as? Bool ?? true
            typeDirection = snapshotValue["type"] as? Int ?? 0

            if let coordinates = snapshotValue["arrayCoordinates"] as? [[String: Double]] {
                for coordinate in coordinates{
                    let latitude = coordinate["latitude"] ?? 0
                    let longitude = coordinate["longitude"] ?? 0
                    let gpx = CLLocationCoordinate2D.init(latitude: latitude, longitude: longitude)
                    coordinatesGPX.append(gpx)
                }
            }

            range = snapshotValue["Range"] as? Int ?? 10
            ref = nil
            key = snapshotValue["id"] as? String ?? ""

        /*else
        {
            descriptionEN =  "Error"
            descriptionFR = "Error"
            nameEN = "Error"
            nameFR = "Error"
            imageURL = "Error"
            audioURLFr = "Error"
            audioURLEn = "Error"
            audioTextEn = "Error"
            audioTextFr = "Error"
            let typeInt = 0
            type = PoiCategory.init(rawValue: typeInt) ?? PoiCategory.init(rawValue: 0)!
            idDestination = "Error"
            isBloquant = false
            isDirectionnal = false
            audioActivated = false
            isAutoDirectionActivated = true
            range =  10
            ref = snapshot.ref
             typeDirection = 0
        }*/
    }

    func getLocations() -> [CLLocation] {
        var locations = [CLLocation]()
        for coordinate in coordinatesGPX
        {
            locations.append(CLLocation.init(latitude: coordinate.latitude, longitude: coordinate.longitude))
        }
        return locations
    }
    
}
