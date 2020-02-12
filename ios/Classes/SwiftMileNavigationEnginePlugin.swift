import Flutter
import UIKit
import MapboxDirections
import MapboxCoreNavigation
import MapboxNavigation

public class SwiftMileNavigationEnginePlugin: NSObject, FlutterPlugin, FlutterStreamHandler {
  var _navigationViewController: NavigationViewController? = nil

      var _distanceRemaining: Double?
      var _durationRemaining: Double?
      var _navigationMode: String?

    public static func register(with registrar: FlutterPluginRegistrar) {
      let channel = FlutterMethodChannel(name: "flutter_mapbox_navigation", binaryMessenger: registrar.messenger())
      let eventChannel = FlutterEventChannel(name: "flutter_mapbox_navigation/arrival", binaryMessenger: registrar.messenger())
      let instance = SwiftMileNavigationEnginePlugin()
      registrar.addMethodCallDelegate(instance, channel: channel)

        eventChannel.setStreamHandler(instance as? FlutterStreamHandler & NSObjectProtocol)

      let viewFactory = FlutterMapboxNavigationViewFactory()
      registrar.register(viewFactory, withId: "FlutterMapboxNavigationView")

    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {

      let arguments = call.arguments as? NSDictionary

      if(call.method == "getDistanceRemaining")
      {
          result(_distanceRemaining)
      }
      else if(call.method == "getDurationRemaining")
      {
          result(_durationRemaining)
      }
      else if(call.method == "finishNavigation")
      {
          endNavigation(result: result)
      }
      else if(call.method == "startNavigation")
      {
          guard let oRoute = arguments?["currentRoute"] as? String else {return}
          guard let oGpsColor = arguments?["gpsColor"] as? String else {return}
          guard let oAccessToken = arguments?["accessToken"] as? String else {return}
          guard let oMode = arguments?["mode"] as? String else {return}

        print(oRoute)
          startNavigation(route: oRoute, gpsColor: oGpsColor, accessToken: oAccessToken, mode: oMode)
      }

      result("iOS " + UIDevice.current.systemVersion)
    }

        func startNavigation(route: String, gpsColor: String, accessToken: String, mode: String)
        {

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

      func endNavigation(result: FlutterResult?)
      {
          if(self._navigationViewController != nil)
          {
              self._navigationViewController?.navigationService.endNavigation(feedback: nil)
              self._navigationViewController?.dismiss(animated: true, completion: {
              self._navigationViewController = nil
                  if(result != nil)
                  {
                      result!(true)
                  }
              })
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

      public func navigationViewController(_ navigationViewController: NavigationViewController, didUpdate progress: RouteProgress, with location: CLLocation, rawLocation: CLLocation) {
            _distanceRemaining = progress.distanceRemaining
            _durationRemaining = progress.durationRemaining
            //_currentLegDescription =  progress.currentLeg.description
            if(AppDataHolder.eventSink != nil) {
                let arrived = progress.isFinalLeg && progress.currentLegProgress.userHasArrivedAtWaypoint
                AppDataHolder.eventSink!(arrived)

            }
        }

      public func navigationViewControllerDidDismiss(_ navigationViewController: NavigationViewController, byCanceling canceled: Bool) {
          endNavigation(result: nil)
      }

      public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
          AppDataHolder.eventSink = events
          return nil
      }

      public func onCancel(withArguments arguments: Any?) -> FlutterError? {
          AppDataHolder.eventSink = nil
          return nil
      }
  }

  public class FlutterMapboxNavigationViewFactory : NSObject, FlutterPlatformViewFactory
  {
      public func create(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
          return FlutterMapboxNavigationView(frame, viewId: viewId, args: args)
      }

      public func createArgsCodec() -> FlutterMessageCodec & NSObjectProtocol {
          return FlutterStandardMessageCodec.sharedInstance()
      }
  }

  public class FlutterMapboxNavigationView : NSObject, FlutterPlatformView
  {
      let frame: CGRect
      let viewId: Int64
      let arguments: NSDictionary?

      init(_ frame: CGRect, viewId: Int64, args: Any?)
      {
          self.frame = frame
          self.viewId = viewId
          self.arguments = args as? NSDictionary;
      }

      public func view() -> UIView
      {
          let zero = UIView(frame: CGRect.zero)

          guard let oRoute = arguments?["currentRoute"] as? String else {return zero}
          guard let oGpsColor = arguments?["gpsColor"] as? String else {return zero}
          guard let oAccessToken = arguments?["accessToken"] as? String else {return zero}
          guard let oMode = arguments?["mode"] as? String else {return zero}


          var navView = UIView(frame: frame)


           /* let nav = NavigationViewController(for: route)
            navView = nav.view
            nav.navigationService.start()*/

          return navView
      }
}
