//
//  MileNavigationEngineFactory.swift
//  mile_navigation_engine
//
//  Created by Lionel Malloggi on 05/03/2020.
//

import Flutter

class MileNavigationEngineFactory: NSObject, FlutterPlatformViewFactory {

    var registrar: FlutterPluginRegistrar

    init(withRegistrar registrar: FlutterPluginRegistrar) {
        self.registrar = registrar
        super.init()
    }

    func createArgsCodec() -> FlutterMessageCodec & NSObjectProtocol {
        return FlutterStandardMessageCodec.sharedInstance()
    }

    func create(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
        return MileNavigationEngineController(withFrame: frame, viewIdentifier: viewId, arguments: args, registrar: registrar)
    }
}
