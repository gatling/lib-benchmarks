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

* OS X 10.11.8
* Intel Core i7 2,7 GHz

* Hotspot 1.8.0_152

```
Benchmark                                              Mode  Cnt     Score     Error  Units
JoddBenchmark.parsePrecompiledRoundRobin              thrpt   20  6462,973 ± 427,175  ops/s
JsoupBenchmark.parseStreamPrecompiledRoundRobin       thrpt   20  4602,047 ± 209,666  ops/s
JsoupBenchmark.parseStringPrecompiledRoundRobin       thrpt   20  4743,252 ± 247,841  ops/s
```

* Hotspot 9

```
Benchmark                                              Mode  Cnt     Score     Error  Units
JoddBenchmark.parsePrecompiledRoundRobin              thrpt   20  5960,708 ± 335,282  ops/s
JsoupBenchmark.parseStreamPrecompiledRoundRobin       thrpt   20  3400,561 ± 319,592  ops/s
JsoupBenchmark.parseStringPrecompiledRoundRobin       thrpt   20  3264,869 ± 235,020  ops/s
```
