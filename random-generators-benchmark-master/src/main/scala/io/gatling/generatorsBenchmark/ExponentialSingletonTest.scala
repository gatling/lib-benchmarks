package io.gatling.generatorsBenchmark

import org.apache.commons.math3.distribution.ExponentialDistribution
import org.uncommons.maths.random.{CellularAutomatonRNG, MersenneTwisterRNG, ExponentialGenerator}
import org.openjdk.jmh.annotations.GenerateMicroBenchmark

object SingletonTest {

  val CommonsMathGenerator = new ExponentialDistribution(Global.MEAN)

  val MersenneTwisterRNGGenerator = new ExponentialGenerator(Global.RATE, new MersenneTwisterRNG)

  val CellularAutomatonRNGGenerator = new ExponentialGenerator(Global.RATE, new CellularAutomatonRNG)
}

class SingletonTest {

  import SingletonTest._

  @GenerateMicroBenchmark
  def commonsMath() = CommonsMathGenerator.sample()

  @GenerateMicroBenchmark
  def uncommonsMath_MersenneTwisterRNG() = MersenneTwisterRNGGenerator.nextValue()

  @GenerateMicroBenchmark
  def uncommonsMath_CellularAutomatonRNG() = CellularAutomatonRNGGenerator.nextValue()
}
