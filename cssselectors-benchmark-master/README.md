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

Sample size:

* 0: 21
* 62 kB
* 90 kB
* 208 kB

Here are the results on my machine:

* OS X 10.11.8
* Intel Core i7 2,7 GHz

* Hotspot 1.8.0_152

```
Benchmark                                             (sample)   Mode  Cnt      Score      Error  Units
JoddBenchmark.parsePrecompiledRoundRobin                     0  thrpt   20  11674,510 ± 2973,482  ops/s
JoddBenchmark.parsePrecompiledRoundRobin                     1  thrpt   20   4015,245 ±  188,523  ops/s
JoddBenchmark.parsePrecompiledRoundRobin                     2  thrpt   20   3158,591 ±  165,150  ops/s
JoddBenchmark.parsePrecompiledRoundRobin                     3  thrpt   20   1636,002 ±   69,332  ops/s
JoddBenchmark.parsePrecompiledRoundRobinWithCharCopy         0  thrpt   20  10381,215 ± 1306,107  ops/s
JoddBenchmark.parsePrecompiledRoundRobinWithCharCopy         1  thrpt   20   3509,597 ±  210,851  ops/s
JoddBenchmark.parsePrecompiledRoundRobinWithCharCopy         2  thrpt   20   3131,579 ±  154,441  ops/s
JoddBenchmark.parsePrecompiledRoundRobinWithCharCopy         3  thrpt   20   1623,171 ±  113,734  ops/s
JsoupBenchmark.parseStreamPrecompiledRoundRobin              0  thrpt   20   8925,306 ±  748,362  ops/s
JsoupBenchmark.parseStreamPrecompiledRoundRobin              1  thrpt   20   2776,929 ±  244,454  ops/s
JsoupBenchmark.parseStreamPrecompiledRoundRobin              2  thrpt   20   2056,131 ±  175,461  ops/s
JsoupBenchmark.parseStreamPrecompiledRoundRobin              3  thrpt   20   1257,150 ±   78,203  ops/s
JsoupBenchmark.parseStringPrecompiledRoundRobin              0  thrpt   20   8680,953 ±  542,569  ops/s
JsoupBenchmark.parseStringPrecompiledRoundRobin              1  thrpt   20   2661,547 ±  260,941  ops/s
JsoupBenchmark.parseStringPrecompiledRoundRobin              2  thrpt   20   2362,275 ±  113,049  ops/s
JsoupBenchmark.parseStringPrecompiledRoundRobin              3  thrpt   20   1198,349 ±   75,283  ops/s
```
