package io.gatling.generatorsBenchmark

import org.apache.commons.math3.distribution.PoissonDistribution
import org.uncommons.maths.random.{CellularAutomatonRNG, MersenneTwisterRNG, PoissonGenerator}
import org.openjdk.jmh.annotations.GenerateMicroBenchmark

object PoissonSingletonTest {

  val CommonsMathGenerator = new PoissonDistribution(Global.MEAN)

  val MersenneTwisterRNGGenerator = new PoissonGenerator(Global.RATE, new MersenneTwisterRNG)

  val CellularAutomatonRNGGenerator = new PoissonGenerator(Global.RATE, new CellularAutomatonRNG)
}

class PoissonSingletonTest {

  import PoissonSingletonTest._

  @GenerateMicroBenchmark
  def commonsMath() = CommonsMathGenerator.sample()

  @GenerateMicroBenchmark
  def uncommonsMath_MersenneTwisterRNG() = MersenneTwisterRNGGenerator.nextValue()

  @GenerateMicroBenchmark
  def uncommonsMath_CellularAutomatonRNG() = CellularAutomatonRNGGenerator.nextValue()
}
