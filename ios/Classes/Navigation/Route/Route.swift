//
//  Route.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/23/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation
import CoreLocation


struct RouteInformmation {
    
    var key: String
    let ref: String
    private let descriptionEN : String
    private let descriptionFR : String
    private let nameEN : String
    private let nameFR :  String
    let imageURL : String?
    
    var isLoop : Bool = false

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

    var coordinatesGPX: [CLLocationCoordinate2D] = []
        
    var isNavigationActivated :Bool = true
    var idDestination: String
    var poisId : [String] = []
    var pois :[PointOfInterest] = []
    var directionals : [PointOfInterest] = []
    var isHoraire: Bool
    var typeRoute : TypeRoute = .none
    
    let averageDuration : Int?
    let length : Int?
    let ascent : Int?
    
    //let route : Route?
    //let listHotels : [Hotel]?
    
    init(snapshotValue: [String: Any]) {
        let arrayDescription = snapshotValue["arrayDescription"] as? [String: String]
        descriptionEN = arrayDescription!["EN"] ?? ""
        descriptionFR = arrayDescription!["FR"] ?? ""
        let arrayName = snapshotValue["arrayName"] as? [String: String]
        nameEN = arrayName!["EN"] ?? ""
        nameFR = arrayName!["FR"] ?? ""
        imageURL = snapshotValue["imageURL"] as? String ?? ""
        averageDuration = snapshotValue["averageDuration"] as? Int ?? 0
        length = snapshotValue["length"] as? Int ?? 0
        ascent = snapshotValue["ascent"] as? Int ?? 0
        isNavigationActivated = snapshotValue["isNavigationActivated"] as? Bool ?? true
        isHoraire = snapshotValue["isHoraire"] as? Bool ?? true
        isLoop = snapshotValue["isLoop"] as? Bool ?? true
        if let pois = snapshotValue["pois"] as? [String] {
            for poi in pois {
                poisId.append(poi)
            }
        }

        idDestination = snapshotValue["idDestination"] as? String ?? ""
        let typeRoutes = snapshotValue["methodLocomotion"]
        let index = typeRoutes as? Int ?? -1
        typeRoute = TypeRoute(rawValue : index) ?? .none


        if let coordinates = snapshotValue["arrayCoordinates"] as? [[String: Double]] {
            for coordinate in coordinates{
                let latitude = coordinate["latitude"] ?? 0
                let longitude = coordinate["longitude"] ?? 0
                let gpx = CLLocationCoordinate2D.init(latitude: latitude, longitude: longitude)
                coordinatesGPX.append(gpx)
            }
        }

        ref = ""
        key = snapshotValue["id"] as? String ?? ""
    }
    
    init() {
        key = ""
        descriptionEN = ""
        descriptionFR = ""
        nameEN = ""
        nameFR = ""
        imageURL = ""
        ref = ""
        idDestination = ""
        isHoraire = true
        averageDuration = 0
        length = 0
        ascent = 0
    }
    
    
    
}
