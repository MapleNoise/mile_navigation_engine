import 'dart:io';

import 'package:flutter/material.dart';
import 'package:android_intent/android_intent.dart';
import 'package:flutter/services.dart';

import 'package:mile_navigation_engine/mile_navigation_lib.dart';
import 'package:mile_navigation_engine_example/about.dart';
import 'package:mile_navigation_engine_example/navigation.dart';

void main() {
  runApp(
      MaterialApp(
        initialRoute: MyApp.routeName,
        routes: {
          MyApp.routeName : (context) => MyApp(),
          About.routeName : (context) => About(),
        },
      )
  );
}

class MyApp extends StatefulWidget {
  static String routeName = "/";
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  //For iOS ONLY
  static const platform = const MethodChannel('com.mile.mile_navigation_engine_example/view_controller');

  @override
  void initState() {
    super.initState();
  }


  @override
  Widget build(BuildContext context) {

    return Scaffold(
        appBar: AppBar(
          title: const Text('Mile Navigation Plugin'),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            InkWell(
                onTap: () {
                  Navigator.push(context,
                    MaterialPageRoute(
                      builder: (context) => Navigation(mode: NavigationMode.NAVIGATE_IN_ROUTE),
                    ),
                  );
                },
                child: Container(
                  height: 36,
                  width: 200,
                  color: Colors.blue,
                  child: Center(
                    child: Text(
                      "Go To navigation widget",
                      style: TextStyle(
                        color: Colors.white,
                      ),
                    ),
                  ),
                )
            ),
            Container(height: 20,),
            InkWell(
                onTap: () {
                  Navigator.push(context,
                    MaterialPageRoute(
                      builder: (context) => Navigation(mode: NavigationMode.NAVIGATE_TO_POI),
                    ),
                  );
                },
                child: Container(
                  height: 36,
                  width: 200,
                  color: Colors.blue,
                  child: Center(
                    child: Text(
                      "Go navigation to poi widget",
                      style: TextStyle(
                        color: Colors.white,
                      ),
                    ),
                  ),
                )
            ),
          ],
        ),
    );
  }
}
