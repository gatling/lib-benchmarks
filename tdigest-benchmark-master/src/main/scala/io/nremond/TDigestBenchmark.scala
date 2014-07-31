package io.nremond

import org.openjdk.jmh.annotations.{ GenerateMicroBenchmark, Scope, State }

import com.tdunning.math.stats.{AVLTreeDigest, TDigest}
import org.HdrHistogram.Histogram
import java.util.Random

object TDigestBenchmark {

  val Random = new Random
  def computeUniformDistribution(size: Int, min: Double, max: Double): Seq[Double] = {
    val width = max - min
    (1 to 10000).map(_ => Random.nextDouble * width + min)
  }
  
  val samples_10k_50_500 = computeUniformDistribution(10000, 50.0, 500.0)
  val samples_10k_50_4000 = computeUniformDistribution(10000, 50.0, 4000.0)
  val samples_100k_50_4000 = computeUniformDistribution(100000, 50.0, 4000.0)
  val samples_10M_50_4000 = computeUniformDistribution(10000000, 50.0, 4000.0)

  val samplesLong_10k_50_500 = samples_10k_50_500.map(_.toLong)
  val samplesLong_10k_50_4000 = samples_10k_50_4000.map(_.toLong)
  val samplesLong_100k_50_4000 = samples_100k_50_4000.map(_.toLong)
  val samplesLong_10M_50_4000 = samples_10M_50_4000.map(_.toLong)

  def main(args: Array[String]) {
    val histogram = new Histogram(4000, 3)
    samplesLong_100k_50_4000.foreach(histogram.recordValue)
    println(histogram.getHistogramData.getValueAtPercentile(95))
  }
}

@State(Scope.Benchmark)
class TDigestBenchmark {

import TDigestBenchmark._

  @GenerateMicroBenchmark
  def arrayDigest_10k_50_500(): Double = {
    val digest = TDigest.createArrayDigest(100.0d)
    samples_10k_50_500.foreach(digest.add)
    digest.quantile(0.95)
  }

  @GenerateMicroBenchmark
  def arrayDigest_10k_50_4000(): Double = {
    val digest = TDigest.createArrayDigest(100.0d)
    samples_10k_50_4000.foreach(digest.add)
    digest.quantile(0.95)
  }

  @GenerateMicroBenchmark
  def arrayDigest_100k_50_4000(): Double = {
    val digest = TDigest.createArrayDigest(100.0d)
    samples_100k_50_4000.foreach(digest.add)
    digest.quantile(0.95)
  }

  @GenerateMicroBenchmark
  def arrayDigest_10M_50_4000(): Double = {
    val digest = TDigest.createArrayDigest(100.0d)
    samples_100k_50_4000.foreach(digest.add)
    digest.quantile(0.95)
  }

  @GenerateMicroBenchmark
  def avlTreeDigest_10k_50_500(): Double = {
    val digest = new AVLTreeDigest(100.0d)
    samples_10k_50_500.foreach(digest.add)
    digest.quantile(0.95)
  }

  @GenerateMicroBenchmark
  def avlTreeDigest_10k_50_4000(): Double = {
    val digest = new AVLTreeDigest(100.0d)
    samples_10k_50_4000.foreach(digest.add)
    digest.quantile(0.95)
  }

  @GenerateMicroBenchmark
  def avlTreeDigest_100k_50_4000(): Double = {
    val digest = new AVLTreeDigest(100.0d)
    samples_100k_50_4000.foreach(digest.add)
    digest.quantile(0.95)
  }

  @GenerateMicroBenchmark
  def avlTreeDigest_10M_50_4000(): Double = {
    val digest = new AVLTreeDigest(100.0d)
    samples_100k_50_4000.foreach(digest.add)
    digest.quantile(0.95)
  }

  @GenerateMicroBenchmark
  def gatlingPercentile_10k_50_500(): Long = {
    val digest = new Metrics(100)
    samplesLong_10k_50_500.foreach(digest.update)
    digest.getQuantile(95)
  }

  @GenerateMicroBenchmark
  def gatlingPercentile_10k_50_4000(): Long = {
    val digest = new Metrics(100)
    samplesLong_10k_50_4000.foreach(digest.update)
    digest.getQuantile(95)
  }

  @GenerateMicroBenchmark
  def gatlingPercentile_100k_50_4000(): Double = {
    val digest = new Metrics(100)
    samplesLong_100k_50_4000.foreach(digest.update)
    digest.getQuantile(95)
  }

  @GenerateMicroBenchmark
  def gatlingPercentile_10M_50_4000(): Double = {
    val digest = new Metrics(100)
    samplesLong_100k_50_4000.foreach(digest.update)
    digest.getQuantile(95)
  }

  @GenerateMicroBenchmark
  def histogramPercentile_10k_50_500(): Long = {
    val histogram = new Histogram(500, 3)
    samplesLong_10k_50_500.foreach(histogram.recordValue)
    histogram.getHistogramData.getValueAtPercentile(95)
  }

  @GenerateMicroBenchmark
  def histogramPercentile_10k_50_4000(): Long = {
    val histogram = new Histogram(4000, 3)
    samplesLong_10k_50_4000.foreach(histogram.recordValue)
    histogram.getHistogramData.getValueAtPercentile(95)
  }

  @GenerateMicroBenchmark
  def histogramPercentile_100k_50_4000(): Double = {
    val histogram = new Histogram(4000, 3)
    samplesLong_100k_50_4000.foreach(histogram.recordValue)
    histogram.getHistogramData.getValueAtPercentile(95)
  }

  @GenerateMicroBenchmark
  def histogramPercentile_10M_50_4000(): Double = {
    val histogram = new Histogram(4000, 3)
    samplesLong_100k_50_4000.foreach(histogram.recordValue)
    histogram.getHistogramData.getValueAtPercentile(95)
  }
}
