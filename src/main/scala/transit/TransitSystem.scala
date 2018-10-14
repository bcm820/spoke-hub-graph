package com.bcm.transit

import scalajs.js.annotation.{JSExportTopLevel, JSExport}
import util.Random
import common._

/**
  * TransitSystem exposes a list of traversable networks of Hub or Spoke cities,
  * and provides an API for adding routes between each City (and adding new cities).
  * Two networks can be combined by adding a new route from one of each of their cities.
  */
@JSExportTopLevel("TransitSystem")
object TransitSystem {

  /**
    * Stores a list of traversable networks.
    */
  @JSExport
  var networks = Set[Network]()

  /*
   * Adds one or two interconnected cities to a set of networks,
   * either to a related network if a match is found, or to a new one.
   */
  private def add(c1: String, c2: String): Unit =
    networks.partition(n => n.graph.contains(c1) || n.graph.contains(c2)) match {
      case (related, unrelated) => {
        val net = Network(newId, Map(c1 -> Spoke(c1, c2), c2 -> Spoke(c2, c1)))
        if (related.isEmpty) networks = networks + net
        else networks = unrelated + related.reduce(_.combine(_)).combine(net)
      }
    }

  /*
   * Returns a random id for each network.
   */
  private def newId: String = {
    val id = s"${(new Random).nextInt(256)}.${(new Random)
      .nextInt(256)}.${(new Random).nextInt(256)}"
    if (networks.map(_.id).contains(id)) newId
    else id
  }

  /**
    * Parses an input string formatted with a delimiter between two cities,
    * adding one or both cities to the same network.
    * Throws `IllegalArgumentException` if no delimiter is used.
    * Note: The UI prevents users from entering a string without a delimiter.
    */
  @JSExport
  def add(desc: String): Unit = {
    require(desc.contains("-"), "No delimiter used in input string for `add`.")
    val Array(c1, c2) = desc.split("-").map(_.trim)
    add(c1, c2)
  }

  /**
    * Generates a random route from a list of cities belonging to Northern Virginia.
    * Note: Parens signals to the Scala.js transpiler that this is a function.
    */
  @JSExport
  def generate(): List[String] = {
    val c1 = NOVA_CITIES((new Random).nextInt(NOVA_CITIES.size))
    val c2 = NOVA_CITIES((new Random).nextInt(NOVA_CITIES.size))
    if (c1 == c2) generate
    else {
      add(c1, c2)
      List(c1, c2)
    }
  }

  /**
    * Used only for manually testing Scala code prior to transpiling.
    * Transpiled code will not use main module initializer.
    */
  def main(args: Array[String]): Unit = {
    import Conversions._
    add("Leesburg", "Ashburn")
    add("Ashburn", "Sterling")
    add("Sterling", "Herndon")
    add("Herndon", "Chantilly")
    add("Chantilly", "South Riding")
    add("South Riding", "Leesburg")
    val n = networks.find(_.graph.contains("Leesburg")).get
    println(n.eachJump("Leesburg").map(show))
  }

}
