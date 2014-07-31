package io.gatling.generatorsBenchmark

import org.openjdk.jmh.annotations.{GenerateMicroBenchmark, Scope, State}
import org.apache.commons.math3.distribution.PoissonDistribution
import org.uncommons.maths.random._

@State(Scope.Thread)
class PoissonThreadLocalTest {

  val CommonsMathGenerator = new PoissonDistribution(Global.MEAN)

  val MersenneTwisterRNGGenerator = new PoissonGenerator(Global.RATE, new MersenneTwisterRNG)

  val CellularAutomatonRNGGenerator = new PoissonGenerator(Global.RATE, new CellularAutomatonRNG)

  val UnsafeMersenneTwisterRNGGenerator = new PoissonGenerator(Global.RATE, new UnsafeMersenneTwisterRNG)

  val UnsafeCellularAutomatonRNGGenerator = new PoissonGenerator(Global.RATE, new UnsafeCellularAutomatonRNG)

  @GenerateMicroBenchmark
  def commonsMath() = CommonsMathGenerator.sample()

  @GenerateMicroBenchmark
  def uncommonsMath_MersenneTwisterRNG() = MersenneTwisterRNGGenerator.nextValue()

  @GenerateMicroBenchmark
  def uncommonsMath_CellularAutomatonRNG() = CellularAutomatonRNGGenerator.nextValue()

  @GenerateMicroBenchmark
  def unsafe_uncommonsMath_MersenneTwisterRNG() = UnsafeMersenneTwisterRNGGenerator.nextValue()

  @GenerateMicroBenchmark
  def unsafe_uncommonsMath_CellularAutomatonRNG() = UnsafeCellularAutomatonRNGGenerator.nextValue()
}
