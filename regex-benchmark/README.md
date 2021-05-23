# JVM Regex input type microbenchmark

## Use case

Input is byte arrays. Benchmark accounts for byte decoding too.

## tl;dr

* Java regex is faster than Re2j
* Gatling's chunks UTF-8 decoder is faster than summing arrays and decoding String

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 5 -f 1 -t 1`

## Figures

Here are the results on my machine:

* OS X 11.3.1
* Intel Core i7 2,7 GHz

* Hotspot 1.8.0_265

```
Benchmark                     Mode  Cnt     Score     Error  Units
Re2jBenchmark.parseGatling   thrpt    5  5857.030 ± 209.367  ops/s
Re2jBenchmark.parseString    thrpt    5  5899.502 ± 465.293  ops/s
RegexBenchmark.parseGatling  thrpt    5  8499.030 ± 304.571  ops/s
RegexBenchmark.parseString   thrpt    5  8508.865 ± 274.854  ops/s
```

* Hotspot 11.0.11

```
Benchmark                     Mode  Cnt     Score      Error  Units
Re2jBenchmark.parseGatling   thrpt    5  6202.381 ± 1629.981  ops/s
Re2jBenchmark.parseString    thrpt    5  6201.677 ±  789.966  ops/s
RegexBenchmark.parseGatling  thrpt    5  8261.909 ±  169.661  ops/s
RegexBenchmark.parseString   thrpt    5  7950.688 ±  715.793  ops/s
```

* Hotspot 16.0.1

```
Benchmark                     Mode  Cnt     Score      Error  Units
Re2jBenchmark.parseGatling   thrpt    5  6590.846 ±  477.347  ops/s
Re2jBenchmark.parseString    thrpt    5  5107.540 ±   33.092  ops/s
RegexBenchmark.parseGatling  thrpt    5  9499.798 ± 1705.814  ops/s
RegexBenchmark.parseString   thrpt    5  7431.564 ± 1502.187  ops/s
```
