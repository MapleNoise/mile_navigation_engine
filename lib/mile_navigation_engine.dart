part of mile_navigation_lib;

/// Turn-By-Turn Navigation Provider
class MileNavigationEngine {

  factory MileNavigationEngine({ValueSetter<bool> onNavigationFinished}) {
    if (_instance == null) {
      final MethodChannel methodChannel = const MethodChannel('flutter_mapbox_navigation');
      final EventChannel eventChannel = const EventChannel('flutter_mapbox_navigation/arrival');
      _instance = MileNavigationEngine.private(methodChannel, eventChannel, onNavigationFinished);
    }
    return _instance;
  }

  @visibleForTesting
  MileNavigationEngine.private(this._methodChannel, this._routeProgressEventchannel, this._routeProgressNotifier, );

  static MileNavigationEngine _instance;

  final MethodChannel _methodChannel;
  final EventChannel _routeProgressEventchannel;

  final ValueSetter<bool> _routeProgressNotifier;

  Stream<bool> _onRouteProgress;
  StreamSubscription<bool> _routeProgressSubscription;

  ///Current Device OS Version
  Future<String> get platformVersion => _methodChannel
      .invokeMethod('getPlatformVersion')
      .then<String>((dynamic result) => result);

  ///Total distance remaining in meters along route.
  Future<double> get distanceRemaining => _methodChannel
      .invokeMethod<double>('getDistanceRemaining')
      .then<double>((dynamic result) => result);

  ///Total seconds remaining on all legs.
  Future<double> get durationRemaining => _methodChannel
      .invokeMethod<double>('getDurationRemaining')
      .then<double>((dynamic result) => result);


  Future startNavigation({String route,String gpsColor, String accessToken, String mode}) async {
    assert(route != null);
    assert(gpsColor != null);
    assert(accessToken != null);
    assert(mode != null);
    final Map<String, Object> args = <String, dynamic>{
      "currentRoute": route,
      "gpsColor": gpsColor,
      "accessToken": accessToken,
      "mode": mode,
    };
    _routeProgressSubscription = _streamRouteProgress.listen(_onProgressData);
    await _methodChannel.invokeMethod('startNavigation', args);
  }

  ///Ends Navigation and Closes the Navigation View
  Future<bool> finishNavigation() async {
    var success = await _methodChannel.invokeMethod('finishNavigation', null);
    return success;
  }

  void _onProgressData(bool arrived) {
    if (_routeProgressNotifier != null) {
      _routeProgressNotifier(arrived);
    }

    if(arrived) {
      _routeProgressSubscription.cancel();
    }
  }

  Stream<bool> get _streamRouteProgress {
    if (_onRouteProgress == null) {
      _onRouteProgress = _routeProgressEventchannel
          .receiveBroadcastStream()
          .map((dynamic event) => _parseArrivalState(event));
    }
    return _onRouteProgress;
  }

  bool _parseArrivalState(bool state) {
    return state;
  }
}

typedef void OnNavigationFinished();

class NavigationView extends StatefulWidget {
  final String route;
  final String gpsColor;
  final String accessToken;
  final String mode;
  final OnNavigationFinished onNavigationFinished;

  NavigationView({@required this.route, @required this.gpsColor, @required this.accessToken, @required this.mode, @required this.onNavigationFinished,});

  _NavigationViewState createState() => _NavigationViewState();
}

class _NavigationViewState extends State<NavigationView> {
  Map<String, Object> args;

  @override
  initState() {
    args = <String, dynamic>{
      "currentRoute": widget.route,
      "gpsColor": widget.gpsColor,
      "accessToken": widget.accessToken,
      "mode": widget.mode,
    };
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return SizedBox(
      height: 350,
      width: 350,
      child: UiKitView(
          viewType: "FlutterMapboxNavigationView",
          creationParams: args,
          creationParamsCodec: StandardMessageCodec()),
    );
  }
}