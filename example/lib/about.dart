import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class About extends StatefulWidget {

  static const routeName = '/about';

  @override
  _AboutState createState() => _AboutState();
}


class _AboutState extends State<About> {

  @override
  void initState(){
    super.initState();
  }

  Future<bool> _onWillPop() async {
    SystemNavigator.pop();
    return true;
  }

  @override
  Widget build(BuildContext context) {

    return new WillPopScope(
      onWillPop: _onWillPop,
      child: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.amber,
          centerTitle: true,
          iconTheme: IconThemeData(
            color: Colors.black,
          ),
          title: Text(
            "About",
            style: TextStyle(
              fontWeight: FontWeight.w600,
              color: Colors.black,
            ),
          ),
        ),
        body: Container(),
      ),
    );
  }
}
