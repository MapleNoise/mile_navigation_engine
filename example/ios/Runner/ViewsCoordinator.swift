import Foundation
import UIKit

final class ViewsCoordinator: BaseCoordinator{
    weak var navigationController: UINavigationController?
    weak var delegate: ViewsToAppCoordinatorDelegate?

    override func start() {
        super.start()
        let storyboard = UIStoryboard(name: "FlutterGenericViewController", bundle: nil)
        if let container =  storyboard.instantiateViewController(withIdentifier: "FlutterGenericViewController") as? FlutterGenericViewController {
            container.coordinatorDelegate = self.navigationController?.pushViewController(container, animated: false) as? ViewsCoordinatorDelegate
        }
    }

    init(navigationController: UINavigationController?) {
      super.init()
      self.navigationController = navigationController
    }
}

protocol ViewsCoordinatorDelegate {
    func navigateToFlutter()
}

extension ViewsCoordinator: ViewsCoordinatorDelegate{
    func navigateToFlutter(){
       self.delegate?.navigateToFlutterViewController()
    }
}
