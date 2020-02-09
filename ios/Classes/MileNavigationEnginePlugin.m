#import "MileNavigationEnginePlugin.h"
#if __has_include(<mile_navigation_engine/mile_navigation_engine-Swift.h>)
#import <mile_navigation_engine/mile_navigation_engine-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "mile_navigation_engine-Swift.h"
#endif

@implementation MileNavigationEnginePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMileNavigationEnginePlugin registerWithRegistrar:registrar];
}
@end
