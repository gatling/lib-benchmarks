package io.gatling.generatorsBenchmark

import org.openjdk.jmh.annotations.{GenerateMicroBenchmark, Scope, State}
import org.apache.commons.math3.distribution.ExponentialDistribution
import org.uncommons.maths.random._

@State(Scope.Thread)
class ExponentialThreadLocalTest {

  val CommonsMathGenerator = new ExponentialDistribution(Global.MEAN)

  val MersenneTwisterRNGGenerator = new ExponentialGenerator(Global.RATE, new MersenneTwisterRNG)

  val CellularAutomatonRNGGenerator = new ExponentialGenerator(Global.RATE, new CellularAutomatonRNG)

  val UnsafeMersenneTwisterRNGGenerator = new ExponentialGenerator(Global.RATE, new UnsafeMersenneTwisterRNG)

  val UnsafeCellularAutomatonRNGGenerator = new ExponentialGenerator(Global.RATE, new UnsafeCellularAutomatonRNG)

  val UnsafeXORShiftRNGGenerator = new ExponentialGenerator(Global.RATE, new UnsafeXORShiftRNG)

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

  @GenerateMicroBenchmark
  def unsafe_uncommonsMath_XORShiftRNG() = UnsafeXORShiftRNGGenerator.nextValue()
}
