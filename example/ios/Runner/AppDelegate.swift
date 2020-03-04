import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {

    lazy var flutterEngine = FlutterEngine(name: "io.flutter")
    private var mainCoordinator: AppCoordinator?
    private var navigationController: UINavigationController?

    override func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        let flutterViewController: FlutterViewController = window?.rootViewController as! FlutterViewController

        let methodChannel = FlutterMethodChannel(name: "com.mile.mile_navigation_engine_example/view_controller", binaryMessenger: flutterViewController.binaryMessenger)

        methodChannel.setMethodCallHandler {(call: FlutterMethodCall, result: FlutterResult) -> Void in
            if (call.method == "display_view_controller") {
                //self.mainCoordinator?.start()

                flutterViewController.pushRoute("/about")

                /*self.window?.rootViewController = nil
                let newController: FlutterViewController = FlutterViewController()
                newController.setInitialRoute("/about")
                let navigationController = UINavigationController(rootViewController: flutterViewController)
                self.window = UIWindow(frame: UIScreen.main.bounds)
                self.window?.makeKeyAndVisible()
                self.window.rootViewController = navigationController
                navigationController.isNavigationBarHidden = true
                navigationController.pushViewController(newController, animated: true)*/

            }
        }


        flutterEngine.run();
        GeneratedPluginRegistrant.register(with: self)
        
        /*self.navigationController = UINavigationController(rootViewController: flutterViewController)
        self.navigationController?.isNavigationBarHidden = true
        self.window?.rootViewController = navigationController
        self.mainCoordinator = AppCoordinator(navigationController: navigationController!)
        self.window?.makeKeyAndVisible()*/

        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }
}
