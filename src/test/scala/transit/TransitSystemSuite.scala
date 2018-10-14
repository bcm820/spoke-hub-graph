package com.bcm.transit

import org.scalatest.FunSuite
import TransitSystem._

class TransitSystemSuite extends FunSuite {

  test("adding two new cities stores them in a new network") {
    add("Alexandria - Fairfax")
    val n = networks.find(_.graph.contains("Fairfax")).get
    assert(n.size == 2)
  }

  test("connecting a city to an existing city adds it to the same network") {
    add("Fairfax - Tysons")
    val n = networks.find(_.graph.contains("Alexandria")).get
    assert(n.graph.contains("Tysons"))
  }

  test("add throws IllegalArgumentException if a dash is missing") {
    intercept[IllegalArgumentException](add("Ashburn Sterling"))
  }

  test("generate adds two random cities to a new or existing network") {
    generate
    val n = networks.find(_.graph.contains("Alexandria")).get
    assert(networks.size > 1 || n.size > 3)
  }

  test("generate returns a list of two random city names") {
    val cs = generate
    assert(cs.forall(_.isInstanceOf[String]))
    assert(cs.forall(_.isInstanceOf[String]))
  }

  test("generate returns a list of two non-equal strings") {
    val List(c1, c2) = generate
    assert(c1 != c2)
  }

}
