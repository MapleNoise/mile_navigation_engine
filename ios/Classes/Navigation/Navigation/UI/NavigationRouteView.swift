//
//  NavigationView.swift
//  MyThalassa
//
//  Created hassine othmane on 9/23/19.
//  Copyright © 2019 mile. All rights reserved.
//


enum NavigationStatus
{
    case none
    case calculating
    case navigation
}

import UIKit
import MapboxNavigation
import Mapbox
import MapboxDirections
import MapboxCoreNavigation
import AVFoundation
import MapboxSpeech

import FloatingPanel

/// Navigation Module View
class NavigationRouteView: UIViewController,FloatingPanelControllerDelegate {
    
    @IBOutlet weak var pauseAndStop_btn: UIButton!
    @IBOutlet weak var map: NavigationMapView!
    @IBOutlet weak var back_btn: UIButton!
    @IBOutlet weak var container_instructionsBannerView: UIView!
    @IBOutlet weak var navigationInoformation_view: UIView!
    @IBOutlet weak var timeTitle_lbl: UILabel!
    @IBOutlet weak var timeValue_lbl: UILabel!
    @IBOutlet weak var distanceTitle_lbl: UILabel!
    @IBOutlet weak var distanceValue_lbl: UILabel!
    @IBOutlet weak var speedTitle_lbl: UILabel!
    @IBOutlet weak var speedValue_lbl: UILabel!
    @IBOutlet weak var instructionBannerView_containerView: UIView!
    @IBOutlet weak var topContainer_view: UIView!
    @IBOutlet weak var containerSpeedButtons_stackView: UIStackView!
    @IBOutlet weak var containerInformations_view: UIView!
    @IBOutlet weak var followLocation_btn: UIButton!
    @IBOutlet weak var pause_btn: UIButton!
    @IBOutlet weak var finish_btn: UIButton!
  //  @IBOutlet weak var poiBannerContainer_view: UIView!
    
    
    
    
    
    private let ui = NavigationViewUI()
    private var presenter: NavigationPresenterProtocol!
    var object : NavigationEntity?
    var navigationService: NavigationService!
    var previewInstructionsView: StepInstructionsView?
    var voiceController: CustomVoiceController?
    var stepsViewController: StepsViewController?
    var instructionsBannerView : InstructionsBannerView!
    var waypoints = [Waypoint]()
    var lastWaypoint : Waypoint!
    var firstWaypoint : Waypoint!
    var routes = [Route]()
    var player: AVAudioPlayer?
    var previewStepIndex: Int?
    var previousLocation : CLLocation?
    var listAudio : [Data] = []
    var indexAudio = 0
    var indexRoute = 0
    var indexStartingPoint = 0
    var playerManagerDelegate : PlayerManagerDelegate? = nil
    static var mutedZone : Bool = false
    var timer:                          Timer = Timer()
    var timer2:                          Timer = Timer()
    var numberofSecondPastOnSR = 0
    let distanceFormatter = DistanceFormatter()
    var simulatedLocationManager : SimulatedLocationManager?
    var locationManager = CLLocationManager()
    static var statusAudioPlayerPoi : PoiStatuts = .notPlayed
    static var statusAudioPlayerDirectionnal : PoiStatuts = .notPlayed
    var timeStampLeaveRoute : TimeInterval?
    var navigationStatus : NavigationStatus = .none
    var longGesture = UILongPressGestureRecognizer()
    var fpc: FloatingPanelController!
    var contentVC : StatisticsView?
    var timerProgressButton : Timer? = nil
    var checkNearsetPoint = true
    var isProgressing = true
    var isPaused = false
    
    var timestamp : Int64?
    var bannerViewAnimated = false
    // Restez appuyé pour mettre en pause
    // Démarrer la navigation
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter = NavigationPresenter(view: self)
       
        DispatchQueue.main.async {
            self.initLocationManager()
            self.initUI()
            self.initInstructionBannerView()
            self.initMap()
            self.initSpeedButtons()
            
            self.longGesture = UILongPressGestureRecognizer(target: self, action: #selector(self.longPress(_:)))
            self.longGesture.minimumPressDuration = 3
            self.pauseAndStop_btn.addGestureRecognizer(self.longGesture)
        }
    }
    

    @objc func longPress(_ sender: UILongPressGestureRecognizer) {
        showAlertAskFinishRoute {
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+0.5) {
                self.deinitNavigation()
                self.playSynthesizer(description: "endRoute".localized)
                self.fpc = FloatingPanelController()
                self.fpc.delegate = self // Optional
                self.fpc.surfaceView.cornerRadius = 30.0
                self.contentVC = StatisticsRouter.createViewController(parentViewController: self) as!  StatisticsView
                self.contentVC!.set(object: StatisticsEntity(distance: self.distanceValue_lbl.text, speed: self.speedValue_lbl.text, time: self.timeValue_lbl.text))
                self.fpc.set(contentViewController: self.contentVC)
                self.fpc.addPanel(toParent: self)
            }
        }
        
    }
    
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        deinitNavigation()
    }
    
    deinit {
        deinitNavigation()
    }
    
    
    func deinitNavigation()
    {
        
        PlayerManager.shared.audioPlayerPoiDescriptionQueue?.pause()
        if self.navigationService != nil
        {
            self.navigationService.stop()
        }
        self.voiceController = nil
        self.suspendNotifications()
    }
    
    
    
    @IBAction func localisation_action(_ sender: Any)
    {
        switch navigationStatus {
        case .none:
            if let coordinate = map.userLocation?.coordinate
            {
                map.setCenter(coordinate, animated: true)
            }
        case .navigation:
            map.recenterMap()
            
        default:
            print("error")
        }
        
    }
    
    @objc func rerouted(_ notification: NSNotification) {
        self.map.showRoutes(routes)
    }
    @IBAction func pauseNavigation_action(_ sender: Any) {
        
        if isPaused == true
        {
            
            self.playSynthesizer(description: "resumeNavigation".localized)
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+2) {
                self.navigationService.start()
                self.pause_btn.setTitle("Pause", for: UIControl.State.normal)
                PlayerManager.shared.playPlayerPoiDescription()
                self.timer = Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(self.timerDuringSmartRun), userInfo: nil, repeats: true)
            }
            
            
        }
        else
        {
            self.navigationService.stop()
            pause_btn.setTitle("Reprendre", for: UIControl.State.normal)
            if let voiceController = self.voiceController
            {
                voiceController.audioPlayer?.pause()
            }
            PlayerManager.shared.stopPlayer()
            PlayerManager.shared.stopPoiDirectional()
            PlayerManager.shared.pausePlayerPoiDescription()
            timer.invalidate()
            speedValue_lbl.text = "0.0km/h"
            self.playSynthesizer(description: "Route paused".localized)
            
        }
        isPaused = !isPaused
        
        
    }
    @IBAction func stopNavigation_action(_ sender: Any) {
        showAlertAskFinishRoute {
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+0.5) {
                self.deinitNavigation()
                self.playSynthesizer(description: "endRoute".localized)
                self.fpc = FloatingPanelController()
                self.fpc.delegate = self // Optional
                self.fpc.surfaceView.cornerRadius = 30.0
                self.contentVC = StatisticsRouter.createViewController(parentViewController: self) as!  StatisticsView
                self.contentVC!.set(object: StatisticsEntity(distance: self.distanceValue_lbl.text, speed: self.speedValue_lbl.text, time: self.timeValue_lbl.text))
                self.fpc.set(contentViewController: self.contentVC)
                self.fpc.addPanel(toParent: self)
            }
        }
    }
    
    @IBAction func pause_action(_ sender: Any) {
        
        self.back_btn.isHidden = true
                       self.back_btn.isEnabled = false
        switch navigationStatus {
        case .none:
            print("noe")
            playSynthesizer(description: "startNavigationToRoute".localized)
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+3) {
               
                self.initTheNavigationtoTheRoute()
            }
            
            break
        case .navigation:
            
            print("click")
            
            
            break
            
            
        default:
            print("error")
        }
        
        
        
        
    }
    @IBAction func pauseTouch_action(_ sender: Any) {
        switch navigationStatus {
        case .none:
            break
        case .navigation:

            break

        default:
            print("error")
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    
    
    
    @IBAction func leave_action(_ sender: Any) {
        
    }
    
    @IBAction func backButton_action(_ sender: Any) {
        _ = navigationController?.popViewController(animated: true)
    }
    
    @IBAction func augmentSpeed_action(_ sender: Any) {
        if navigationService != nil{
            if let locationManager = navigationService.locationManager as? SimulatedLocationManager {
                       let displayValue = 1 + locationManager.speedMultiplier
                       locationManager.speedMultiplier = Double(displayValue)
                       print(locationManager.speedMultiplier)
                   }
        }
       
        
    }
    @IBAction func decreaseSpeed_action(_ sender: Any) {
        if let locationManager = navigationService.locationManager as? SimulatedLocationManager {
            if (  locationManager.speedMultiplier > Double(0) )
            {
                let displayValue =  locationManager.speedMultiplier - 1
                locationManager.speedMultiplier = Double(displayValue)
                print(locationManager.speedMultiplier)
            }
        }
    }
}

// MARK: - extending NavigationView to implement it's protocol
extension NavigationRouteView: NavigationViewProtocol {
    func set(object: NavigationEntity) {
        self.object = object
        self.indexStartingPoint = object.startingPoint
        checkNearsetPoint = object.checkNearsetPoint
    }
}

// MARK: - extending NavigationView to implement the custom ui view delegate
extension NavigationRouteView: NavigationViewUIDelegate {
    
}

// MARK: - extending NavigationView to implement the custom ui view data source
extension NavigationRouteView: NavigationViewUIDataSource {
    func objectFor(ui: NavigationViewUI) -> NavigationEntity {
        return NavigationEntity(checkNearsetPoint: true)
    }
}
