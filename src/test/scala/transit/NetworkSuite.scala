package com.bcm.transit

import org.scalatest.FunSuite
import TransitSystem._

class NetworkSuite extends FunSuite {

  trait TestNetwork {
    add("Alexandria - Fairfax")
    val n = networks.find(_.graph.contains("Fairfax")).get
  }

  test("adding two new cities stores them as Spokes") {
    new TestNetwork {
      assert(n.graph("Fairfax").isInstanceOf[Spoke])
      assert(n.graph("Alexandria").isInstanceOf[Spoke])
    }
  }

  test("adding two existing Spoke cities does not modify their class") {
    new TestNetwork {
      add("Alexandria - Fairfax")
      assert(n.graph("Fairfax").isInstanceOf[Spoke])
      assert(n.graph("Alexandria").isInstanceOf[Spoke])
    }
  }

  test("connecting a city to an existing city makes the existing city a Hub") {
    add("Fairfax - Tysons")
    val n = networks.find(_.graph.contains("Fairfax")).get
    assert(n.graph("Fairfax").isInstanceOf[Hub])
  }

  test("size matches the amount of cities belonging to the network") {
    val n = networks.find(_.graph.contains("Fairfax")).get
    assert(n.size == 3)
  }

  test("edge matches the amount of routes available in the network") {
    val n = networks.find(_.graph.contains("Fairfax")).get
    assert(n.edges == 4)
  }

  test("routes returns a set with 1 city if receiving a Spoke") {
    val n = networks.find(_.graph.contains("Tysons")).get
    assert(n.routes("Tysons").size == 1)
  }

  test("routes returns a set of 2 or more cities if receiving a Hub") {
    val n = networks.find(_.graph.contains("Fairfax")).get
    assert(n.routes("Fairfax").size > 1)
  }

  test("combine returns a new network when combining two networks") {
    val n = networks.find(_.graph.contains("Fairfax")).get
    add("Ashburn - Sterling")
    add("Sterling - Tysons")
    val m = networks.find(_.graph.contains("Fairfax")).get
    assert(n != m)
    assert(n.size != m.size)
  }

}
