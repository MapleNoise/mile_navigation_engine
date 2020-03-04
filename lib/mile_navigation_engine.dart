part of mile_navigation_lib;

/// Turn-By-Turn Navigation Provider
class MileNavigationEngine {

  factory MileNavigationEngine({ValueSetter<bool> onNavigationFinished, ValueSetter<String> onActivePOI}) {
    if (_instance == null) {
      final MethodChannel methodChannel = const MethodChannel('flutter_mapbox_navigation');
      final EventChannel eventChannel = const EventChannel('flutter_mapbox_navigation/arrival');
      final EventChannel activePOIChannel = const EventChannel('flutter_mapbox_navigation/active_poi');
      _instance = MileNavigationEngine.private(methodChannel, eventChannel, onNavigationFinished, activePOIChannel, onActivePOI);
    }
    return _instance;
  }

  @visibleForTesting
  MileNavigationEngine.
  private(this._methodChannel, this._eventchannel, this._routeProgressNotifier, this._activePOIchannel, this._onActivePOINotifier);

  static MileNavigationEngine _instance;

  final MethodChannel _methodChannel;

  final EventChannel _eventchannel;
  final ValueSetter<bool> _routeProgressNotifier;
  Stream<bool> _onRouteProgress;
  StreamSubscription<bool> _routeProgressSubscription;

  final EventChannel _activePOIchannel;
  final ValueSetter<String> _onActivePOINotifier;
  Stream<String> _onActivePOIProgress;
  StreamSubscription<String> _onActivePOISubscription;

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
    _onActivePOISubscription = _streamActivePOIProgress.listen(_onActivePOI);

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
      _onActivePOISubscription.cancel();
    }
  }

  Stream<bool> get _streamRouteProgress {
    if (_onRouteProgress == null) {
      _onRouteProgress = _eventchannel.receiveBroadcastStream(["finish_navigation"]).map((dynamic event) => _parseArrivalState(event));
    }
    return _onRouteProgress;
  }

  bool _parseArrivalState(bool state) {
    return state;
  }

  void _onActivePOI(String result) {
    if (_onActivePOINotifier != null) {
      _onActivePOINotifier(result);
    }
  }

  Stream<String> get _streamActivePOIProgress {
    if (_onActivePOIProgress == null) {
      _onActivePOIProgress = _activePOIchannel.receiveBroadcastStream(["active_poi"]).map((dynamic event) => _parseOnActivePOIState(event));
    }
    return _onActivePOIProgress;
  }

  String _parseOnActivePOIState(String state) {
    return state;
  }
}

typedef void OnNavigationFinished();
typedef void OnActivePOI();

class NavigationView extends StatefulWidget {
  final String route;
  final String gpsColor;
  final String accessToken;
  final String mode;
  final OnNavigationFinished onNavigationFinished;
  final OnActivePOI onActivePOI;

  NavigationView({
    @required this.route,
    @required this.gpsColor,
    @required this.accessToken,
    @required this.mode,
    @required this.onNavigationFinished,
    @required this.onActivePOI,
  });

  _NavigationViewState createState() => _NavigationViewState();
}

class _NavigationViewState extends State<NavigationView> {
  Map<String, Object> args;
  final Completer<MileNavigationEngine> _controller = Completer<MileNavigationEngine>();

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

  Future<void> onPlatformViewCreated(int id) async {
    final MileNavigationEngine navigationEngine = MileNavigationEngine(
      onNavigationFinished: (arrived) {
        widget.onNavigationFinished();
        print("onNavigationFinished");
      },
      onActivePOI: (result) {
        widget.onActivePOI();
        print("Android POI is playing");
      },
    );
    _controller.complete(navigationEngine);
  }

  @override
  Widget build(BuildContext context) {

    final Map<String, dynamic> creationParams = <String, dynamic>{
      'route': widget.route,
      'gpsColor': widget.gpsColor,
      'accessToken': widget.accessToken,
      'mode': widget.mode,
    };

    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: 'navigation_view',
        onPlatformViewCreated: onPlatformViewCreated,
        creationParams: creationParams,
        creationParamsCodec: const StandardMessageCodec(),
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return UiKitView(
        viewType: 'navigation_view',
        onPlatformViewCreated: onPlatformViewCreated,
        creationParams: creationParams,
        creationParamsCodec: const StandardMessageCodec(),
      );
    }


    /*return SizedBox(
      height: 350,
      width: 350,
      child: UiKitView(
          viewType: "FlutterMapboxNavigationView",
          creationParams: args,
          creationParamsCodec: StandardMessageCodec()),
    );*/
  }
}