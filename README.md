## Spoke-Hub Multi-Network Model

This is a weekend project written in [Scala](src/main/scala/), transpiled via [Scala.js](src/main/scala-js) (non-readable), and integrated into a React SPA written in [Javascript](src/main/js).

### [Demo available here](https://bcmendoza.github.io/spoke-hub-graph/)

---

### Instructions

Enter a route description into the text input (i.e. "Earth - Mars"). Each location will be assigned to a network in the transit system.

If one of the locations mentioned already exists in the system, the new location will be added to the same network and linked to the existing location. Otherwise, a new network will be added to the system containing two new connected cities.

For convenience, a `Generate` button is provided to easily add cities from a stored list of Northern Virginia cities.

Hovering over each city will highlight its network and hide other networks. Other information shown include:

- Whether the city is reachable without traveling a route more than once (denoted by a `∞` symbol)
- The city's direct neighbors, reachable in one trip.

Clicking each city will select it, revealing a list of trips required to reach all other cities in its network.

---

### Details

I created two case classes, `Hub` and `Spoke`, as `City` implementations. A `Hub` is a city which connects to a set of cities, whereas a `Spoke` only connects to one city. These representations form the foundation for how the application is modeled.

While the majority of the code handles data immutably, the application's state is stored and updated in the `TransitSystem` object by a single mutable variable, `networks`, the set of networks being modified with each new input.

---

### Inspiration

From [Wikipedia](https://en.wikipedia.org/wiki/Spoke%E2%80%93hub_distribution_paradigm):

> The spoke-hub distribution paradigm is a form of transport topology optimization in which traffic planners organize routes as a series of "spokes" that connect outlying points to a central "hub".
