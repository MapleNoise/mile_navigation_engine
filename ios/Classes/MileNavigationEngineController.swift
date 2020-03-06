//
//  MileNavigationEngineController.swift
//  android_intent
//
//  Created by Lionel Malloggi on 05/03/2020.
//

import Flutter
import UIKit
import Mapbox

class MileNavigationEngineController: NSObject, FlutterPlatformView, MGLMapViewDelegate {

    //private var registrar: FlutterPluginRegistrar
    //private var channel: FlutterMethodChannel?
    private var navigationView: UIView?


    /*private var isMapReady = false
    private var mapReadyResult: FlutterResult?*/

    func view() -> UIView {
        return navigationView!
    }

    init(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?, registrar: FlutterPluginRegistrar) {
        //self.registrar = registrar

        let arguments = args as? NSDictionary
        let oRoute = arguments?["route"] as? String
        let oGpsColor = arguments?["gpsColor"] as? String
        let oMode = arguments?["mode"] as? String

        super.init()

        /*channel = FlutterMethodChannel(name: "flutter_mapbox_navigation_\(viewId)", binaryMessenger: registrar.messenger())
        channel!.setMethodCallHandler(onMethodCall)*/

        let dict = convertToDictionary(text: oRoute!)
        var route = RouteInformmation.init(snapshotValue: dict!)
        var pois: [PointOfInterest] = []
        if let poisData = dict!["arrayPois"] as? [[String: Any]] {
            for poiData in poisData {
                let poi = PointOfInterest.init(snapshotValue: poiData)
                print(poi)
                pois.append(poi)
            }
            route.pois = pois
        }
        
        let controller = NavigationRouter.mainStoryboard.instantiateViewController(withIdentifier: "NavigationRouteView") as! NavigationRouteView
        let object = NavigationEntity.init(route: route, pois: pois, directionals: [PointOfInterest](), checkNearsetPoint: false, registrar: registrar, viewId: viewId)
        controller.set(object: object)
        AppDataHolder.flutterNavigationMode = oMode!
        controller.modalPresentationStyle = .fullScreen
        navigationView = controller.view
    }

    /*func onMethodCall(methodCall: FlutterMethodCall, result: @escaping FlutterResult) {
        switch(methodCall.method) {
        case "map#waitForMap":
            if isMapReady {
                result(nil)
            } else {
                mapReadyResult = result
            }

        default:
            result(FlutterMethodNotImplemented)
        }
    }*/

    func convertToDictionary(text: String) -> [String: Any]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
}
