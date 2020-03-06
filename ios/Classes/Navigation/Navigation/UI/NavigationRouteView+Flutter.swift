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
