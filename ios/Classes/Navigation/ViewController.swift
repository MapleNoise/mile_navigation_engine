//
//  ViewController.swift
//  ProjectEngineFlutter
//
//  Created by Hassine on 23/01/2020.
//  Copyright © 2020 com.mile. All rights reserved.
//

import UIKit
import Mapbox


class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        /*var routeService = RouteService.init()
        routeService.getRoute(id: "PA20") { (route) in
            var poiService = PointOfInterestService.init()
            poiService.gePois(by: route.poisId, cityId: "V0", routeId: 0) { (idRoute, pois) in
                var myroute = route
                myroute.pois = pois
                var object = NavigationEntity.init(route: myroute, pois: pois, directionals: [PointOfInterest]())
                
                
                let controller = NavigationRouter.mainStoryboard.instantiateViewController(withIdentifier: "NavigationRouteView") as! NavigationRouteView
                             controller.set(object: object)
                   controller.modalPresentationStyle = .fullScreen
                             self.show(controller, sender: nil)
                
                
                
            }
        }*/
        
        // Do any additional setup after loading the view.
    }

    

}

