package io.gatling.generatorsBenchmark

import org.apache.commons.math3.distribution.ExponentialDistribution
import org.uncommons.maths.random.{XORShiftRNG, CellularAutomatonRNG, MersenneTwisterRNG, ExponentialGenerator}
import org.openjdk.jmh.annotations.GenerateMicroBenchmark

object ExponentialSingletonTest {

  val CommonsMathGenerator = new ExponentialDistribution(Global.MEAN)

  val MersenneTwisterRNGGenerator = new ExponentialGenerator(Global.RATE, new MersenneTwisterRNG)

  val CellularAutomatonRNGGenerator = new ExponentialGenerator(Global.RATE, new CellularAutomatonRNG)

  val XORShiftRNGGenerator = new ExponentialGenerator(Global.RATE, new XORShiftRNG)
}

class ExponentialSingletonTest {

  import ExponentialSingletonTest._

  @GenerateMicroBenchmark
  def commonsMath() = CommonsMathGenerator.sample()

  @GenerateMicroBenchmark
  def uncommonsMath_MersenneTwisterRNG() = MersenneTwisterRNGGenerator.nextValue()

  @GenerateMicroBenchmark
  def uncommonsMath_CellularAutomatonRNG() = CellularAutomatonRNGGenerator.nextValue()

  @GenerateMicroBenchmark
  def uncommonsMath_XORShiftRNG() = XORShiftRNGGenerator.nextValue()
}
