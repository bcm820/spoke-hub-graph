package com.bcm.transit

import scalajs.js.annotation.{JSExportTopLevel, JSExport}
import scalajs.js
import js.JSConverters._

object Conversions {

  /**
    * Returns a Scala set as a Scala.js transpiled Javascript array.
    */
  @JSExportTopLevel("toJS")
  def toJS(xs: Set[Any]): js.Array[Any] = xs.toSeq.toJSArray

  /**
    * Returns a Scala iterable as a Scala.js transpiled Javascript array.
    */
  @JSExportTopLevel("toJS")
  def toJS(xs: Iterable[Any]): js.Array[Any] = xs.toSeq.toJSArray

  /**
    * Returns a Scala list as a Scala.js transpiled Javascript array.
    */
  @JSExportTopLevel("toJS")
  def toJS(xs: List[Any]): js.Array[Any] = xs.toJSArray

  /**
    * Returns a Set[String] converted from a Set[City].
    */
  @JSExportTopLevel("show")
  def show(xs: Set[City]): Set[String] = xs.map(_.name)

}
