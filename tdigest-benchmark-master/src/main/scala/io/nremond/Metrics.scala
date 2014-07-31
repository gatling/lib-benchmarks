package io.nremond

import scala.annotation.tailrec
import scala.collection.mutable

class Metrics(bucketWidth: Int) {

  var count = 0L
  var max = 0L
  var min = Long.MaxValue
  private val buckets = mutable.HashMap.empty[Long, Long].withDefaultValue(0L)

  def update(value: Long) {
    count += 1
    max = max.max(value)
    min = min.min(value)

    val bucket = value / bucketWidth
    val newCount = buckets(bucket) + 1L
    buckets += (bucket -> newCount)
  }

  def reset() {
    count = 0L
    max = 0L
    min = Long.MaxValue
    buckets.clear()
  }

  def getQuantile(quantile: Int): Long = {
    if (buckets.isEmpty)
      0L
    else {
      val limit = (count * (quantile.toDouble / bucketWidth)).toLong

        @tailrec
        def getQuantileRec(buckets: List[(Long, Long)], count: Long): Long = buckets match {
          case (bucketTime, bucketCount) :: tail =>
            val newCount = count + bucketCount
            if (newCount >= limit)
              max.min((bucketTime * bucketWidth) + bucketWidth)
            else
              getQuantileRec(tail, newCount)

          case Nil => max
        }

      getQuantileRec(buckets.toList.sorted, 0L)
    }
  }
}