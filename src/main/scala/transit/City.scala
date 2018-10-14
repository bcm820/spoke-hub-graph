package com.bcm.transit

/**
  * A City can be implemented as a Hub or Spoke.
  */
trait City {
  val name: String
}

/**
  * A Hub is a City with routes to multiple cities.
  * Routes are represented by references to the destination's names.
  */
case class Hub(name: String, refs: Set[String]) extends City

/**
  * A Spoke is a City with a route to a single city.
  * A route is represented by a reference to the destination's name.
  */
case class Spoke(name: String, ref: String) extends City
