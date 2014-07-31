tdigest-benchmark
=================

Gatling currently uses its own very crude inaccurate yet fast mechanism for live percentile computation and TDigest's ArrayDigest for offline percentile computation.

This benchmark compares:

* TDigest vs HdrHistogram vs Gatling own mechanism in terms of raw performance

* TDigest uses a compression factor of 100.0
* HdrHistogram uses a precision of 3 digits
* Samples uses a exponential distribution around 1000 (1 sec in millis) + 1% of hiccups uniformly distributed between 3000 (3 sec) and 60.000 (60 sec, default request timeout)
* Computing 95th percentile

How to
------

Run with `mvn clean package` then `java -jar target/microbenchmarks.jar ".*PercentileBenchmark.*" -wi 5 -i 5 -f 1`

TL;DR
-----

* HdrHistogram is wicked fast, about 9 times faster than Gatling and 38 times faster than TDigest
* Gatling naive implementation is still 4 times faster than TDigest but precision is not that great
* TDigest precision is excellent, HdrHistogram is decent but not as good (can be off by 60ms)

We should be using HdrHistogram for real time computing and TDigest for offline computing.

Results
-------

Benchmark                                            Mode   Samples         Mean   Mean error    Units

i.n.PercentileBenchmark.arrayDigest32_10k           thrpt         5        0,441        0,008   ops/ms
i.n.PercentileBenchmark.arrayDigest32_100k          thrpt         5        0,425        0,023   ops/ms
i.n.PercentileBenchmark.arrayDigest32_10M           thrpt         5        0,447        0,013   ops/ms

i.n.PercentileBenchmark.arrayDigest64_10k           thrpt         5        0,403        0,004   ops/ms
i.n.PercentileBenchmark.arrayDigest64_100k          thrpt         5        0,401        0,013   ops/ms
i.n.PercentileBenchmark.arrayDigest64_10M           thrpt         5        0,404        0,012   ops/ms

i.n.PercentileBenchmark.avlTreeDigest_10k           thrpt         5        0,371        0,008   ops/ms
i.n.PercentileBenchmark.avlTreeDigest_100k          thrpt         5        0,358        0,006   ops/ms
i.n.PercentileBenchmark.avlTreeDigest_10M           thrpt         5        0,364        0,011   ops/ms

i.n.PercentileBenchmark.gatlingPercentile_10k       thrpt         5        1,465        0,166   ops/ms
i.n.PercentileBenchmark.gatlingPercentile_100k      thrpt         5        1,508        0,140   ops/ms
i.n.PercentileBenchmark.gatlingPercentile_10M       thrpt         5        1,447        0,183   ops/ms

i.n.PercentileBenchmark.histogramPercentile_10k     thrpt         5       13,754        0,212   ops/ms
i.n.PercentileBenchmark.histogramPercentile_100k    thrpt         5       13,362        0,577   ops/ms
i.n.PercentileBenchmark.histogramPercentile_10M     thrpt         5       13,559        0,483   ops/ms

Values

realPercentile_10k=3075.7079800500755
realPercentile_100k=3021.7342453960464
realPercentile_10M=3060.8996191702

arrayDigest32_10k=3070.1498167466134
arrayDigest32_100k=3018.605763526236
arrayDigest32_10M=3065.3469714217886

arrayDigest64_10k=3071.0808665180652
arrayDigest64_100k=3019.8624121505754
arrayDigest64_10M=3065.3469714217886

avlTreeDigest_10k=3074.1522566943213
avlTreeDigest_100k=3018.8776355182335
avlTreeDigest_10M=3065.067337892659

gatlingPercentile_10k=3100
gatlingPercentile_100k=3100
gatlingPercentile_10M=3100

histogramPercentile_10k=3070
histogramPercentile_100k=3092
histogramPercentile_10M=3022
