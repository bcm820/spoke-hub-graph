package com.bcm.transit

import scalajs.js.annotation.JSExport

/**
  * A Network represents a group of interconnected cities,
  * modeled by mapping each City name to its instance.
  * The map allows for traversal between cities via reference
  * since each City stores an adjacency list of its neighbors.
  */
case class Network(@JSExport id: String, graph: Map[String, City]) {

  /**
    * Returns an iterable of cities belonging to this Network.
    */
  @JSExport
  def cities: Iterable[String] = graph.keys

  /**
    * The amount of cities belonging to this Network.
    */
  @JSExport
  val size: Int = graph.keys.size

  /**
    * The total amount of routes available in this Network.
    */
  @JSExport
  val edges: Int = graph.values.map(routes(_).size).reduce(_ + _)

  /**
    * An accessor method for a city's neighbors.
    * Returns a set, even if only one ref is found (i.e. Spoke).
    */
  def routes(city: City): Set[City] = city match {
    case Hub(_, rs)  => rs.map(graph(_))
    case Spoke(_, r) => Set(graph(r))
  }

  /**
    * An accessor method that receives a City's name.
    */
  @JSExport
  def routes(city: String): Set[City] = routes(graph(city))

  /**
    * Returns a new network from combined collections of cities.
    */
  def combine(that: Network) = {
    val newId = if (size > that.size) id else that.id
    Network(newId, combineGraph(that))
  }

  /**
    * Returns an graph representation of a network of cities.
    */
  def combineGraph(that: Network): Map[String, City] = {
    val overlap = graph.keySet & that.graph.keySet
    if (overlap.isEmpty) graph ++ that.graph
    else
      graph ++ that.graph ++ (overlap
        .map(k => (k, combineCity(graph(k), that.graph(k))))
        .toMap)
  }

  /**
    * Returns a new City with combined refs from two cities.
    */
  def combineCity(c1: City, c2: City): City = c1 match {
    case Hub(n, rs) =>
      c2 match {
        case Spoke(_, r) => Hub(n, rs + r)
        case Hub(_, rs2) => Hub(n, rs | rs2)
      }
    case Spoke(n, r) =>
      c2 match {
        case Spoke(_, r2) if (r == r2) => Spoke(n, r)
        case Spoke(_, r2) => Hub(n, Set(r, r2))
        case Hub(_, rs)   => Hub(n, rs + r)
      }
  }

  /**
    * Returns a set of cities reached after a given number of jumps.
    * The default behavior is to only return cities reached by an exact jump amount.
    * This can be configured to include cities reached from previous jumps (see `inRangeOf`).
    * The given number of jumps can exceed the outer limit of the network, returning an empty set.
    */
  @JSExport
  def fromJumps(origin: String, maxJumps: Int, allJumps: Boolean = false): Set[City] = {
    def jump(currCity: City, currJump: Int, cityAcc: Set[City]): Set[City] = {
      val nextCities =
        if (currJump == 2) routes(currCity).diff(cityAcc + graph(origin))
        else routes(currCity).diff(cityAcc)
      if (currJump == maxJumps || nextCities.isEmpty)
        if (allJumps) cityAcc | nextCities else nextCities
      else nextCities.flatMap(c => jump(c, currJump + 1, cityAcc + c))
    }
    if (maxJumps < 1) Set()
    else jump(graph(origin), 1, Set())
  }

  /**
    * Returns a list of Set[City] representing cities reached at each jump,
    * from first jump until all reachable cities are found.
    */
  @JSExport
  def eachJump(city: String): List[Set[City]] = {
    def jump(jumps: Int, reached: List[Set[City]]): List[Set[City]] = {
      val next = fromJumps(city, jumps)
      if (next.isEmpty) reached.reverse
      else jump(jumps + 1, next :: reached)
    }
    jump(2, List(fromJumps(city, 1)))
  }

  /**
    * Returns a set of all cities reachable within a given range (`jumps`).
    */
  @JSExport
  def inRangeOf(city: String, jumps: Int): Set[City] = fromJumps(city, jumps, true)

  /**
    * Tests whether a given city is reachable from another given city.
    * Note: Does not include itself as reachable; use isLoopable to test.
    */
  @JSExport
  def isReachable(c1: String, c2: String): Boolean = routes(graph(c1))(graph(c2))

  /**
    * Tests whether a given city is reachable without traveling a route twice.
    */
  @JSExport
  def isLoopable(city: String): Boolean = fromJumps(city, size, true)(graph(city))

}
