package com.mile.mile_navigation_engine.utils

import com.mile.mile_navigation_engine.Navigation.ApplicationRunner
import com.mile.mile_navigation_engine.model.LanguageCode

class LanguageResource(languageCode: LanguageCode?) {
    var endRoute = ""
    var inversionRoute = ""
    var routeReached = ""
    var startNavigationToPOI = ""
    var startNavigationToRoute = ""
    var wrongWay = ""
    var resumeNavigation = ""
    var pauseNavigation = ""
    var right = ""
    var left = ""
    var front = ""
    var continueStraight = ""
    private var uTurn = ""
    var moreInfo = ""
    var lowBattery = ""
    var turnLeft = ""
    var turnRight = ""
    var then = ""
    var onto = ""
    var toCrossTheRoad = ""
    var onDistanceMeters = ""
    var annonceSponsor = ""
    var and = ""
    var moreSponsorInformations = ""
    var infoPOINearby = ""
    var runStarted = ""
    var offRoute = ""
    var congratulationsFinishRoute = ""
    var finishNavigation = ""

    fun getuTurn(): String {
        return uTurn
    }

    companion object {
        val languagesResources: LanguageResource
            get() = LanguageResource(ApplicationRunner.appLanguage)
    }

    init {
        when (languageCode) {
            LanguageCode.FR -> {
                endRoute =
                        "Félicitations, vous avez fini votre parcours ! N'hésitez pas à nous donner votre avis."
                inversionRoute = "Nous venons de modifier l'orientation du parcours."
                routeReached = "Vous avez atteint le parcours. Continuez tout droit."
                startNavigationToPOI = "Nous allons maintenant vous guider à pied vers votre point d'intérè."
                startNavigationToRoute =
                        "Nous allons maintenant vous guider à pied vers le point le plu proche du parcours."
                wrongWay = "Vous courez dans le mauvais sens, faite demi tour."
                resumeNavigation = "Reprise du parcours"
                pauseNavigation = "Parcours en pause"
                right = "À votre droite, "
                left = "À votre gauche, "
                front = "Face à vous, "
                continueStraight = "continuez tout droit "
                uTurn = "Faites demi-tour, pui "
                moreInfo = "+ d'infos"
                lowBattery = "Attention, votre niveau de batterie est faible."
                turnRight = "Tournez à droite "
                turnLeft = "Tournez à gauche "
                toCrossTheRoad = "pour traverser la route,"
                then =
                        " pui " //Must be the same word than combine instruction in SKMaps.bundle language file General_TTS
                onto = " sur "
                onDistanceMeters = "sur %1\$s mètres"
                annonceSponsor = "Ce parcours vous est offert par "
                and = ", et, "
                moreSponsorInformations =
                        ". Pour plus d'informations, cliquez sur les marqueurs rouges."
                infoPOINearby =
                        "Vous êtes à proximité d'un point d'intérêt. Pour lire sa description, rendez-vous sur l'application."
                runStarted = "Parcours démarré. Bonne expérience !"
                offRoute =
                        "Vous êtes sortis du parcours, regardez votre téléphone pour repartir dans la bonne direction."
                congratulationsFinishRoute =
                        "Félicitations, vous avez terminé le parcours. Nous espérons que vous avez apprécié votre expérience."
                finishNavigation =
                        "Parcours terminé. Nous espérons que vous avez apprécié votre expérience."
            }
            LanguageCode.EN -> {
                endRoute =
                        "Congratulations, you have reached the end of this route! Please feel free to give us a feedback."
                inversionRoute = "We have changed the direction of the route"
                routeReached = "You have reached the route please carry straight on."
                startNavigationToPOI = "We will now guide you to your point of interest."
                startNavigationToRoute = "We will now guide you to the route."
                wrongWay = "You are running the wrong way, please make a u-turn."
                resumeNavigation = "Route resumed"
                pauseNavigation = "Route paused"
                right = "To your right, "
                left = "To your right, "
                front = "Straight ahead, "
                continueStraight = "carry straight on "
                uTurn = "Make a u-turn, then "
                moreInfo = "More info"
                lowBattery = "Warning, battery level is low."
                then =
                        " then " //Must be the same word than combine instruction in SKMaps.bundle language file General_TTS
                onto = " onto "
                turnRight =
                        "Turn right" //Must be the same word than combine instruction in SKMaps.bundle language file General_TTS
                turnLeft =
                        "Turn left" //Must be the same word than combine instruction in SKMaps.bundle language file General_TTS
                toCrossTheRoad = "to cross the road,"
                onDistanceMeters = "for %1\$s meters"
                annonceSponsor = "This route is brought to you by "
                and = ", and, "
                moreSponsorInformations = ". For more information, click on the red pin."
                infoPOINearby =
                        "A point of interest is nearby. To read its description, check the app."
                runStarted = "Route started. Enjoy your experience !"
                offRoute =
                        "You have left the designated route, check your phone to get back on track."
                congratulationsFinishRoute =
                        "Congratulations, you have completed the route. We hope you have enjoyed your experience."
                finishNavigation =
                        "Route ended. We hope you have enjoyed your experience."
            }
        }
    }
}