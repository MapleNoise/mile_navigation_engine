//
//  NavigationRouteView+InstructionsBannerViewDelegate.swift
//  MyThalassa
//
//  Created by hassine othmane on 9/24/19.
//  Copyright © 2019 mile. All rights reserved.
//

import UIKit
import MapboxNavigation
import MapboxCoreNavigation
import MapboxDirections


extension NavigationRouteView: InstructionsBannerViewDelegate {

    
    

    func resumeNotifications() {
           NotificationCenter.default.addObserver(self, selector: #selector(progressDidChange(_ :)), name: .routeControllerProgressDidChange, object: nil)
           NotificationCenter.default.addObserver(self, selector: #selector(rerouted(_:)), name: .routeControllerDidReroute, object: nil)
           NotificationCenter.default.addObserver(self, selector: #selector(updateInstructionsBanner(notification:)), name: .routeControllerDidPassVisualInstructionPoint, object: navigationService.router)
       }

    func didTapInstructionsBanner(_ sender: BaseInstructionsBannerView) {
        toggleStepsList()
    }

    func didSwipeInstructionsBanner(_ sender: BaseInstructionsBannerView, swipeDirection direction: UISwipeGestureRecognizer.Direction) {
        if direction == .down {
            toggleStepsList()
            return
        }

        // preventing swiping if the steps list is visible
        guard stepsViewController == nil else { return }

        // Make sure that we actually have remaining steps left
        guard let remainingSteps = navigationService?.routeProgress.remainingSteps else { return }

        var previewIndex = -1
        var previewStep: RouteStep?

        if direction == .left {
            // get the next step from our current preview step index
            if let currentPreviewIndex = previewStepIndex {
                previewIndex = currentPreviewIndex + 1
            } else {
                previewIndex = 0
            }

            // index is out of bounds, we have no step to show
            guard previewIndex < remainingSteps.count else { return }
            previewStep = remainingSteps[previewIndex]
        } else {
            // we are already at step 0, no need to show anything
            guard let currentPreviewIndex = previewStepIndex else { return }

            if currentPreviewIndex > 0 {
                previewIndex = currentPreviewIndex - 1
                previewStep = remainingSteps[previewIndex]
            } else {
                previewStep = navigationService.routeProgress.currentLegProgress.currentStep
                previewIndex = -1
            }
        }

        if let step = previewStep {
            addPreviewInstructions(step: step)
            previewStepIndex = previewIndex
        }
    }


    func toggleStepsList() {
        // remove the preview banner while viewing the steps list
        removePreviewInstruction()

        if let controller = stepsViewController {
            controller.dismiss()
            stepsViewController = nil
        } else {
            guard let service = navigationService else { return }


            if let parentVC = self.parent {
                if let parentVC = parentVC as? UINavigationController {
                    // parentVC is someViewController
                    print("NavigationRouteView")

                    let controller = StepsViewController(routeProgress: service.routeProgress)
                               controller.delegate = self
                    parentVC.addChild(controller)
                    controller.view.backgroundColor = Style.primaryColor
                    
                               parentVC.view.addSubview(controller.view)

                    if #available(iOS 11.0, *) {
                        controller.view.topAnchor.constraint(equalTo: parentVC.view.safeAreaLayoutGuide.topAnchor).isActive = true
                    } else {
                        // Fallback on earlier versions
                         controller.view.topAnchor.constraint(equalTo: parentVC.view.topAnchor).isActive = true
                    }
                               controller.view.leadingAnchor.constraint(equalTo: parentVC.view.leadingAnchor).isActive = true
                               //controller.view.bottomAnchor.constraint(equalTo: instructionsBannerView.topAnchor).isActive = true
                    if #available(iOS 11.0, *) {
                        controller.view.bottomAnchor.constraint(equalTo: parentVC.view.safeAreaLayoutGuide.bottomAnchor).isActive = true
                    } else {
                        // Fallback on earlier versions
                          controller.view.bottomAnchor.constraint(equalTo: parentVC.view.bottomAnchor).isActive = true
                    }
                               controller.view.trailingAnchor.constraint(equalTo: parentVC.view.trailingAnchor).isActive = true

                               controller.didMove(toParent: self)
                               view.layoutIfNeeded()
                               stepsViewController = controller
                               return
                } else if (parentVC as? UIViewController) != nil {
                    // parentVC is anotherViewController
                     print("UIViewController")
                    let controller = StepsViewController(routeProgress: service.routeProgress)
                               controller.delegate = self
                               addChild(controller)
                               view.addSubview(controller.view)

                               controller.view.topAnchor.constraint(equalTo: instructionsBannerView.bottomAnchor).isActive = true
                               controller.view.leadingAnchor.constraint(equalTo: view.leadingAnchor).isActive = true
                               controller.view.bottomAnchor.constraint(equalTo: view.bottomAnchor).isActive = true
                               controller.view.trailingAnchor.constraint(equalTo: view.trailingAnchor).isActive = true

                               controller.didMove(toParent: self)
                               view.layoutIfNeeded()
                               stepsViewController = controller
                               return
                }
            }




        }
    }

    func addPreviewInstructions(step: RouteStep) {
        let route = navigationService.route

        // find the leg that contains the step, legIndex, and stepIndex
        guard let leg       = route.legs.first(where: { $0.steps.contains(step) }),
            let legIndex  = route.legs.firstIndex(of: leg),
            let stepIndex = leg.steps.firstIndex(of: step) else {
                return
        }

        // find the upcoming manuever step, and update instructions banner to show preview
        guard stepIndex + 1 < leg.steps.endIndex else { return }
        let maneuverStep = leg.steps[stepIndex + 1]
        updatePreviewBannerWith(step: step, maneuverStep: maneuverStep)

        // stop tracking user, and move camera to step location
        map.tracksUserCourse = false
        map.userTrackingMode = .none
        map.enableFrameByFrameCourseViewTracking(for: 1)
        map.setCenter(maneuverStep.maneuverLocation, zoomLevel: map.zoomLevel, direction: maneuverStep.initialHeading!, animated: true, completionHandler: nil)

        // add arrow to map for preview instruction
        map.addArrow(route: route, legIndex: legIndex, stepIndex: stepIndex + 1)
    }

    func updatePreviewBannerWith(step: RouteStep, maneuverStep: RouteStep) {
        // remove preview banner if it exists

        removePreviewInstruction()

        // grab the last instruction for step
        guard let instructions = step.instructionsDisplayedAlongStep?.last else { return }

        // create a StepInstructionsView and display that over the current instructions banner
        let previewInstructionsView = StepInstructionsView(frame: instructionsBannerView.frame)
        previewInstructionsView.delegate = self
        previewInstructionsView.swipeable = true
        previewInstructionsView.backgroundColor = instructionsBannerView.backgroundColor
        view.addSubview(previewInstructionsView)

        // update instructions banner to show all information about this step
        previewInstructionsView.updateDistance(for: RouteStepProgress(step: step))
        previewInstructionsView.update(for: instructions)

        self.previewInstructionsView = previewInstructionsView
        
    }

    func removePreviewInstruction() {
        guard let view = previewInstructionsView else { return }
        view.removeFromSuperview()

        // reclaim the delegate, from the preview banner
        instructionsBannerView.delegate = self

        // nil out both the view and index
        previewInstructionsView = nil
        previewStepIndex = nil
    }


    func suspendNotifications() {
           NotificationCenter.default.removeObserver(self, name: .routeControllerProgressDidChange, object: nil)
           NotificationCenter.default.removeObserver(self, name: .routeControllerWillReroute, object: nil)
           NotificationCenter.default.removeObserver(self, name: .routeControllerDidPassVisualInstructionPoint, object: nil)
       }

    @objc private func updateInstructionsBanner(notification: NSNotification) {
           guard let routeProgress = notification.userInfo?[RouteControllerNotificationUserInfoKey.routeProgressKey] as? RouteProgress else { return }
           instructionsBannerView.update(for: routeProgress.currentLegProgress.currentStepProgress.currentVisualInstruction)
       }


       // Notifications sent on all location updates
       @objc func progressDidChange(_ notification: NSNotification) {
           // do not update if we are previewing instruction steps
           guard previewInstructionsView == nil else { return }

           let routeProgress = notification.userInfo![RouteControllerNotificationUserInfoKey.routeProgressKey] as! RouteProgress
           let location = notification.userInfo![RouteControllerNotificationUserInfoKey.locationKey] as! CLLocation

           // Add maneuver arrow
           if routeProgress.currentLegProgress.followOnStep != nil {
               map.addArrow(route: routeProgress.route, legIndex: routeProgress.legIndex, stepIndex: routeProgress.currentLegProgress.stepIndex + 1)
           } else {
               map.removeArrow()
           }

           // Update the top banner with progress updates
           instructionsBannerView.updateDistance(for: routeProgress.currentLegProgress.currentStepProgress)
           instructionsBannerView.isHidden = false

           // Update the user puck
           map.updateCourseTracking(location: location, animated: true)
       }
}

