part of mile_navigation_lib;

class RouteFinished {
  double percentTraveled;
  double distanceTraveled;
  double time;
  int rating;

  RouteFinished({
    this.percentTraveled,
    this.distanceTraveled,
    this.time,
    this.rating,
  });
}