# Css selectors microbenchmark for the JVM

## Use case

Input is byte arrays.

If the parser can't guess encoding by itself, we try to decode in the most efficient possible way, but encoding time is accounted for.

## tl;dr

Jodd is more that twice faster than Jsoup

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 2 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.11.1
* Hotspot 1.8.0_66
* Intel Core i7 2,7 GHz

Benchmark                                        Mode  Cnt     Score     Error  Units
JoddBenchmark.parseStringPrecompiledRoundRobin  thrpt   20  6763.483 ± 193.008  ops/s
JoddBenchmark.parseCharsPrecompiledRoundRobin   thrpt   20  6470.666 ± 526.220  ops/s
JsoupBenchmark.parseStringPrecompiledRoundRobin thrpt   20  4106.197 ± 220.625  ops/s
JsoupBenchmark.parseStreamPrecompiledRoundRobin thrpt   20  3837.760 ± 171.702  ops/s
