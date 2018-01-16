# Css selectors microbenchmark for the JVM

## Use case

Input is byte array chunks whose size is an MTU (1500B).

If the parser can't guess encoding by itself, we try to decode in the most efficient possible way, but encoding time is accounted for.

## tl;dr

Jodd is faster than Jsoup.

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

* OS X 10.11.6
* Intel Core i7 2,7 GHz

* Hotspot 1.8.0_152

```
Benchmark                             (sample)   Mode  Cnt      Score     Error  Units
JoddBenchmark.parseCharCopy                  0  thrpt   20  13185,835 ± 732,470  ops/s
JoddBenchmark.parseCharCopy                  1  thrpt   20   3787,276 ± 165,733  ops/s
JoddBenchmark.parseCharCopy                  2  thrpt   20   2892,155 ± 152,103  ops/s
JoddBenchmark.parseCharCopy                  3  thrpt   20   1482,901 ±  68,248  ops/s
JoddBenchmark.parseJava8CharStealing         0  thrpt   20  11077,426 ± 649,026  ops/s
JoddBenchmark.parseJava8CharStealing         1  thrpt   20   3316,631 ± 129,985  ops/s
JoddBenchmark.parseJava8CharStealing         2  thrpt   20   2817,390 ± 130,302  ops/s
JoddBenchmark.parseJava8CharStealing         3  thrpt   20   1528,541 ±  84,229  ops/s
JsoupBenchmark.parseInputStream              0  thrpt   20   8053,948 ± 815,965  ops/s
JsoupBenchmark.parseInputStream              1  thrpt   20   2402,689 ± 168,066  ops/s
JsoupBenchmark.parseInputStream              2  thrpt   20   1756,881 ± 162,083  ops/s
JsoupBenchmark.parseInputStream              3  thrpt   20   1052,766 ±  88,588  ops/s
JsoupBenchmark.parseString                   0  thrpt   20   7473,940 ± 362,771  ops/s
JsoupBenchmark.parseString                   1  thrpt   20   2442,028 ± 244,413  ops/s
JsoupBenchmark.parseString                   2  thrpt   20   1904,280 ± 194,676  ops/s
JsoupBenchmark.parseString                   3  thrpt   20    980,001 ± 115,361  ops/s
```

* Hotspot 9.0.1

```
Benchmark                             (sample)   Mode  Cnt      Score     Error  Units
JoddBenchmark.parseCharCopy                  0  thrpt   20  12418,191 ± 689,242  ops/s
JoddBenchmark.parseCharCopy                  1  thrpt   20   3463,798 ± 148,219  ops/s
JoddBenchmark.parseCharCopy                  2  thrpt   20   2681,608 ±  92,629  ops/s
JoddBenchmark.parseCharCopy                  3  thrpt   20   1416,964 ±  83,809  ops/s
JoddBenchmark.parseJava8CharStealing         0  thrpt   20  10352,379 ± 458,083  ops/s
JoddBenchmark.parseJava8CharStealing         1  thrpt   20   3013,021 ± 192,909  ops/s
JoddBenchmark.parseJava8CharStealing         2  thrpt   20   2740,327 ± 167,910  ops/s
JoddBenchmark.parseJava8CharStealing         3  thrpt   20   1412,721 ± 105,567  ops/s
JsoupBenchmark.parseInputStream              0  thrpt   20   7103,697 ± 823,435  ops/s
JsoupBenchmark.parseInputStream              1  thrpt   20   1970,944 ± 254,808  ops/s
JsoupBenchmark.parseInputStream              2  thrpt   20   1629,100 ± 228,170  ops/s
JsoupBenchmark.parseInputStream              3  thrpt   20    924,673 ± 134,286  ops/s
JsoupBenchmark.parseString                   0  thrpt   20   6777,509 ± 428,201  ops/s
JsoupBenchmark.parseString                   1  thrpt   20   2055,526 ± 293,226  ops/s
JsoupBenchmark.parseString                   2  thrpt   20   1698,861 ± 117,976  ops/s
JsoupBenchmark.parseString                   3  thrpt   20    870,886 ± 184,209  ops/s
```
