//
//  NavigationRouteView+Flutter.swift
//  mile_navigation_engine
//
//  Created by Lionel Malloggi on 06/03/2020.
//

import Foundation

extension NavigationRouteView : onStatisticClosed {

    func initFlutterChannel() {
        channel = FlutterMethodChannel(name: "flutter_mapbox_navigation_\(viewId)", binaryMessenger: registrar.messenger())
        channel!.setMethodCallHandler(onMethodCall)
    }

    func onMethodCall(methodCall: FlutterMethodCall, result: @escaping FlutterResult) {
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
    }

    func flutterDetectPOI() {
       channel?.invokeMethod("onActivePOI", arguments: [
            "poi": "{}",
        ])
    }

    func flutterPauseActionStartNavigation() {
        channel?.invokeMethod("onNavigationStarted", arguments: [
            "isStarted": true,
        ])
    }

    func flutterMapViewDidFinishLoadingMap() {
        isMapReady = true
        mapReadyResult?(nil)
    }

    func onStatisticClosed() {
        channel?.invokeMethod("onNavigationFinished", arguments: [
            "isFinished": false,
        ])
    }
}

protocol onStatisticClosed: class {
    func onStatisticClosed()
}

@IBDesignable
class GradientView: UIView {

    @IBInspectable var firstColor: UIColor = UIColor.clear {
        didSet {
            updateView()
        }
    }

    @IBInspectable var secondColor: UIColor = UIColor.clear {
        didSet {
            updateView()
        }
    }

    @IBInspectable var isHorizontal: Bool = true {
       didSet {
          updateView()
       }
    }

    override class var layerClass: AnyClass {
       get {
          return CAGradientLayer.self
       }
    }

    func updateView() {
        let layer = self.layer as! CAGradientLayer
        layer.colors = [firstColor, secondColor].map{$0.cgColor}
        if (self.isHorizontal) {
           layer.startPoint = CGPoint(x: 0, y: 0.5)
           layer.endPoint = CGPoint (x: 1, y: 0.5)
        } else {
           layer.startPoint = CGPoint(x: 0.5, y: 0)
           layer.endPoint = CGPoint (x: 0.5, y: 1)
        }
    }

}
