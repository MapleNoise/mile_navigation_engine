part of mile_navigation_lib;

typedef void OnNavigationFinished(bool);
typedef void OnActivePOI(String);
typedef void OnNavigationStarted(bool);

class MileNavigationController extends ChangeNotifier {

  final MethodChannel _channel;

  final OnNavigationFinished onNavigationFinished;
  final OnActivePOI onActivePOI;
  final OnNavigationStarted onNavigationStarted;

  final int _id;

  static Future<MileNavigationController> init(int id, OnNavigationFinished onNavigationFinished, OnActivePOI onActivePOI, OnNavigationStarted onNavigationStarted) async {
    assert(id != null);
    final MethodChannel methodChannel = MethodChannel('flutter_mapbox_navigation_$id');
    await methodChannel.invokeMethod('map#waitForMap');
    return MileNavigationController._(
        id,
        methodChannel,
        onNavigationFinished,
        onActivePOI,
        onNavigationStarted,
    );
  }

  MileNavigationController._(this._id, MethodChannel channel,  this.onNavigationFinished, this.onActivePOI, this.onNavigationStarted) :
        assert(_id != null),
        assert(channel != null), _channel = channel {

    _channel.setMethodCallHandler(_handleMethodCall);
  }

  Future<dynamic> _handleMethodCall(MethodCall call) async {
    switch (call.method) {
      case 'onNavigationFinished':
        final bool isFinished = call.arguments['isFinished'];
        if (onNavigationFinished != null) {
          onNavigationFinished(isFinished);
        }
        break;
      case 'onActivePOI':
        final String poi = call.arguments['poi'];
        if (onActivePOI != null) {
          onActivePOI(poi);
        }
        break;
      case 'onNavigationStarted':
        final bool isHidden = call.arguments['isStarted'];
        if (onNavigationStarted != null) {
          onNavigationStarted(isHidden);
        }
        break;
      default:
        throw MissingPluginException();
    }
  }
}
