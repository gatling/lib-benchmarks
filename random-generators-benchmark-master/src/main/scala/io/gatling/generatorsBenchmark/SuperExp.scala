package io.gatling.generatorsBenchmark

import java.util.concurrent.ThreadLocalRandom

import org.openjdk.jmh.annotations.GenerateMicroBenchmark

class SuperExp {

  @GenerateMicroBenchmark
  def commonsMath() = {
    val rnd = ThreadLocalRandom.current
    var u: Double = .0
    do {
      u = rnd.nextDouble
    } while (u == 0d)
    -Math.log(u) / Global.RATE
  }
}
