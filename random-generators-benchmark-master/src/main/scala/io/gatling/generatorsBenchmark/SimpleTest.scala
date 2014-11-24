package io.gatling.generatorsBenchmark

import java.util.concurrent.ThreadLocalRandom

import org.openjdk.jmh.annotations.GenerateMicroBenchmark
import org.uncommons.maths.random.UnsafeXORShiftRNG

object SimpleTest {

  val TheXORShiftRNG = new ThreadLocal[UnsafeXORShiftRNG] {
    override def initialValue = new UnsafeXORShiftRNG
  }
}

class SimpleTest {

  @GenerateMicroBenchmark
  def threadLocalRandom(): Long = ThreadLocalRandom.current.nextLong

  @GenerateMicroBenchmark
  def xorShiftRNG(): Long = SimpleTest.TheXORShiftRNG.get.nextLong
}
