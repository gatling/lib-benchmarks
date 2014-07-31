package io.nremond

import org.apache.commons.math3.distribution.ExponentialDistribution
import java.util.Random
import org.openjdk.jmh.annotations.GenerateMicroBenchmark
import com.tdunning.math.stats.{ArrayDigest, AVLTreeDigest}
import org.HdrHistogram.Histogram

object PercentileBenchmark {

  val Percentile: Int = 95
  val PercentilePercent: Double = Percentile / 100.0
  val MaxValue = 60000
  val TDigestCompression = 100d
  val HdrHistogramDigits = 3

  val Random = new Random
  def computeExponentialDistributionWithHiccups(size: Int, mean: Int, hiccupsPercentage: Int, hiccupsMin: Int, hiccupsMax: Int): Seq[Double] = {

    val exponentialDistribution = new ExponentialDistribution(mean)
    val hiccupsWitdh = hiccupsMax - hiccupsMin

    (1 to size).map { i =>
      if (i % size < hiccupsPercentage)
        Random.nextInt(hiccupsMin) + hiccupsWitdh
      else
        exponentialDistribution.sample
    }
  }

  val samples_10k = computeExponentialDistributionWithHiccups(10000, 1000, 1, 3000, MaxValue)
  val samples_100k = computeExponentialDistributionWithHiccups(10000, 1000, 1, 3000, MaxValue)
  val samples_10M = computeExponentialDistributionWithHiccups(10000, 1000, 1, 3000, MaxValue)

  val samplesLong_10k = computeExponentialDistributionWithHiccups(10000, 1000, 1, 3000, MaxValue).map(_.toLong)
  val samplesLong_100k = computeExponentialDistributionWithHiccups(10000, 1000, 1, 3000, MaxValue).map(_.toLong)
  val samplesLong_10M = computeExponentialDistributionWithHiccups(10000, 1000, 1, 3000, MaxValue).map(_.toLong)

  def computePercentile(percent: Double, sample: Seq[Double]) = {
    val sorted = sample.sorted
    sorted(math.floor(sorted.length * percent).toInt)
  }

  def main(args: Array[String]) {

    println("realPercentile_10k=" + computePercentile(PercentilePercent, samples_10k))
    println("realPercentile_100k=" + computePercentile(PercentilePercent, samples_100k))
    println("realPercentile_10M=" + computePercentile(PercentilePercent, samples_10M))

    val benchmark = new PercentileBenchmark
    println("arrayDigest32_10k=" + benchmark.arrayDigest32_10k)
    println("arrayDigest32_100k=" + benchmark.arrayDigest32_100k)
    println("arrayDigest32_10M=" + benchmark.arrayDigest32_10M)
    println("arrayDigest64_10k=" + benchmark.arrayDigest64_10k)
    println("arrayDigest64_100k=" + benchmark.arrayDigest64_100k)
    println("arrayDigest64_10M=" + benchmark.arrayDigest32_10M)
    println("avlTreeDigest_10k=" + benchmark.avlTreeDigest_10k)
    println("avlTreeDigest_100k=" + benchmark.avlTreeDigest_100k)
    println("avlTreeDigest_10M=" + benchmark.avlTreeDigest_10M)
    println("gatlingPercentile_10k=" + benchmark.gatlingPercentile_10k)
    println("gatlingPercentile_100k=" + benchmark.gatlingPercentile_100k)
    println("gatlingPercentile_10M=" + benchmark.gatlingPercentile_10M)
    println("histogramPercentile_10k=" + benchmark.histogramPercentile_10k)
    println("histogramPercentile_100k=" + benchmark.histogramPercentile_100k)
    println("histogramPercentile_10M=" + benchmark.histogramPercentile_10M)
  }
}

class PercentileBenchmark {

  import PercentileBenchmark._

  final def arrayDigest(sample: Seq[Double], percentile: Double, pageSize: Int): Double = {
    val digest = new ArrayDigest(32, pageSize)
    sample.foreach(digest.add)
    digest.quantile(percentile)
  }

  @GenerateMicroBenchmark
  def arrayDigest32_10k(): Double = arrayDigest(samples_10k, PercentilePercent, 32)

  @GenerateMicroBenchmark
  def arrayDigest32_100k(): Double = arrayDigest(samples_100k, PercentilePercent, 32)

  @GenerateMicroBenchmark
  def arrayDigest32_10M(): Double = arrayDigest(samples_10M, PercentilePercent, 32)

  @GenerateMicroBenchmark
  def arrayDigest64_10k(): Double = arrayDigest(samples_10k, PercentilePercent, 64)

  @GenerateMicroBenchmark
  def arrayDigest64_100k(): Double = arrayDigest(samples_100k, PercentilePercent, 64)

  @GenerateMicroBenchmark
  def arrayDigest64_10M(): Double = arrayDigest(samples_10M, PercentilePercent, 64)

  final def avlTreeDigest(sample: Seq[Double], percentile: Double): Double = {
    val digest = new AVLTreeDigest(TDigestCompression)
    sample.foreach(digest.add)
    digest.quantile(percentile)
  }

  @GenerateMicroBenchmark
  def avlTreeDigest_10k(): Double = avlTreeDigest(samples_10k, PercentilePercent)

  @GenerateMicroBenchmark
  def avlTreeDigest_100k(): Double = avlTreeDigest(samples_100k, PercentilePercent)

  @GenerateMicroBenchmark
  def avlTreeDigest_10M(): Double = avlTreeDigest(samples_10M, PercentilePercent)

  final def gatlingPercentile(sample: Seq[Long], percentile: Int): Long = {
    val digest = new Metrics(100)
    sample.foreach(digest.update)
    digest.getQuantile(percentile)
  }

  @GenerateMicroBenchmark
  def gatlingPercentile_10k(): Long = gatlingPercentile(samplesLong_10k, Percentile)

  @GenerateMicroBenchmark
  def gatlingPercentile_100k(): Long = gatlingPercentile(samplesLong_100k, Percentile)

  @GenerateMicroBenchmark
  def gatlingPercentile_10M(): Long = gatlingPercentile(samplesLong_10M, Percentile)

  final def histogramPercentile(sample: Seq[Long], percentile: Int): Long = {
    val histogram = new Histogram(MaxValue, HdrHistogramDigits)
    sample.foreach(histogram.recordValue)
    histogram.getHistogramData.getValueAtPercentile(percentile)
  }

  @GenerateMicroBenchmark
  def histogramPercentile_10k(): Long = histogramPercentile(samplesLong_10k, Percentile)

  @GenerateMicroBenchmark
  def histogramPercentile_100k(): Long = histogramPercentile(samplesLong_100k, Percentile)

  @GenerateMicroBenchmark
  def histogramPercentile_10M(): Long = histogramPercentile(samplesLong_10M, Percentile)
}
