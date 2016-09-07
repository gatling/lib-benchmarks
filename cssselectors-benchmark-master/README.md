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

```
Benchmark                                         Mode  Cnt     Score     Error  Units
JoddBenchmark.parseCharsPrecompiledRoundRobin    thrpt   20  6970,968 ± 290,554  ops/s
JoddBenchmark.parseStringPrecompiledRoundRobin   thrpt   20  6535,438 ± 209,340  ops/s
JsoupBenchmark.parseStringPrecompiledRoundRobin  thrpt   20  4297,211 ± 193,820  ops/s
JsoupBenchmark.parseStreamPrecompiledRoundRobin  thrpt   20  4134,318 ± 211,835  ops/s
```
