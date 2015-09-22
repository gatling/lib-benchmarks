package io.gatling.lens

import monocle.macros.GenLens

import org.openjdk.jmh.annotations.Benchmark
import com.softwaremill.quicklens._

case class Foo(bar: Bar, baz: Baz)
case class Bar(count: Int)
case class Baz(s: String)

object Lens {

  val foo = Foo(Bar(0), Baz("BAZ"))

  val _bar = GenLens[Foo](_.bar)
  val _count    = GenLens[Bar](_.count)
}

class Lens {

  import Lens._

  @Benchmark
  def bareScala =
    foo.copy(bar = foo.bar.copy(count = foo.bar.count + 1))

  @Benchmark
  def withQuicklens =
    modify(foo)(_.bar.count).using(_ + 1)

  @Benchmark
  def withMonocle =
    (_bar ^|-> _count).modify(_ + 1)(foo)
}
