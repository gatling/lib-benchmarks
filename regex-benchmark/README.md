# JVM Regex input type microbenchmark

## Use case

Input is byte arrays. Benchmark accounts for byte decoding too.

## tl;dr

* Re2j is faster than Java regex
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
Re2jBenchmark.parseGatling   thrpt    5  2123.864 ± 345.421  ops/s
Re2jBenchmark.parseString    thrpt    5  1460.959 ±  94.530  ops/s
RegexBenchmark.parseGatling  thrpt    5  1593.741 ±  48.431  ops/s
RegexBenchmark.parseString   thrpt    5  1096.875 ± 205.551  ops/s
```

* Hotspot 11.0.11

```
Benchmark                     Mode  Cnt     Score     Error  Units
Re2jBenchmark.parseGatling   thrpt    5  2182.066 ± 169.769  ops/s
Re2jBenchmark.parseString    thrpt    5  1282.212 ± 196.925  ops/s
RegexBenchmark.parseGatling  thrpt    5  1410.529 ±  77.144  ops/s
RegexBenchmark.parseString   thrpt    5  1006.955 ±  29.149  ops/s
```

* Hotspot 16.0.1

```
Benchmark                     Mode  Cnt     Score     Error  Units
Re2jBenchmark.parseGatling   thrpt    5  2407.383 ± 348.302  ops/s
Re2jBenchmark.parseString    thrpt    5  1320.450 ± 157.699  ops/s
RegexBenchmark.parseGatling  thrpt    5  1486.626 ±  31.614  ops/s
RegexBenchmark.parseString   thrpt    5   960.175 ±  91.793  ops/s
```
