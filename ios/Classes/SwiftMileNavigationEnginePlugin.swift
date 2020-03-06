import Flutter
import UIKit
import MapboxDirections
import MapboxCoreNavigation
import MapboxNavigation

public class SwiftMileNavigationEnginePlugin: NSObject, FlutterPlugin {
  var _navigationViewController: NavigationViewController? = nil

      var _distanceRemaining: Double?
      var _durationRemaining: Double?
      var _navigationMode: String?

    /*public static func register(with registrar: FlutterPluginRegistrar) {
      let channel = FlutterMethodChannel(name: "flutter_mapbox_navigation", binaryMessenger: registrar.messenger())
      let eventChannel = FlutterEventChannel(name: "flutter_mapbox_navigation/arrival", binaryMessenger: registrar.messenger())
      let activePOIChannel = FlutterEventChannel(name: "flutter_mapbox_navigation/active_poi", binaryMessenger: registrar.messenger())
      let instance = SwiftMileNavigationEnginePlugin()
      registrar.addMethodCallDelegate(instance, channel: channel)

      eventChannel.setStreamHandler(instance as? FlutterStreamHandler & NSObjectProtocol)
      activePOIChannel.setStreamHandler(instance as? FlutterStreamHandler & NSObjectProtocol)

      let viewFactory = FlutterMapboxNavigationViewFactory()
      registrar.register(viewFactory, withId: "FlutterMapboxNavigationView")
    }*/

    public static func register(with registrar: FlutterPluginRegistrar) {
        let instance = MileNavigationEngineFactory(withRegistrar: registrar)
        registrar.register(instance, withId: "navigation_view")
    }

    func startNavigation(route: String, gpsColor: String, accessToken: String, mode: String) {
            let dict = convertToDictionary(text: route)
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
           let object = NavigationEntity.init(route: route, pois: pois, directionals: [PointOfInterest](), checkNearsetPoint: false)
           controller.set(object: object)
           AppDataHolder.flutterNavigationMode = mode
           controller.modalPresentationStyle = .fullScreen

            if UIApplication.shared.delegate?.window??.rootViewController is UINavigationController {
                UIApplication.shared.delegate?.window??.rootViewController!.present(controller, animated: true, completion: nil)
            } else {
                let flutterViewController = UIApplication.shared.delegate?.window??.rootViewController as! FlutterViewController
                flutterViewController.present(controller, animated: true, completion: nil)
            }
        }


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
