//
//  DestinationService.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/20/19.
//  Copyright © 2019 mile. All rights reserved.
//

import Foundation

class DestinationService {
    
    /*func getDestination(id:String,_ callBack:@escaping (Destination) -> Void) {
        let ref = Database.database().reference()
        ref.child("Destination").observe(DataEventType.value) { snapshot in
            let destination = Destination(snapshot: snapshot)
            callBack(destination)
        }
    }
    
    
    
    func getAllDestinations(_ callBack:@escaping ([Destination]) -> Void){
        let ref = Database.database().reference()
        ref.child("Destination").observe(DataEventType.value) { snapshot in
            var destinations : [Destination] = []
            for item in snapshot.children {
                let destination = Destination(snapshot: item as! DataSnapshot)
                let status = UserDefaults.standard.bool(forKey: "notLocked")
                if  status == false
                {
                    if destination.key != "D5"
                    {
                        destinations.append(destination)
                    }
                }
                else
                {
                    destinations.append(destination)
                }
            }
            callBack(destinations)
        }
    }*/
    
}
