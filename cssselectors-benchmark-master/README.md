# Css selectors microbenchmark for the JVM

## Use case

Input is byte array chunks whose size is an MTU (1500B).

If the parser can't guess encoding by itself, we try to decode in the most efficient possible way, but encoding time is accounted for.

## tl;dr

* Jodd is faster than Jsoup
* Unsafe based String char stealing on Java 8 doesn't help for such large payloads, it's best to directly decode binary payloads as char arrays.
* Jsoup perf is worse on Java 9

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 1 -t 2`

## Inputs

Sample size:

* 0: 21kB
* 1: 62 kB
* 2: 90 kB
* 3: 208 kB

## Results

On my machine:

* OS X 10.11.6
* Intel Core i7 2,7 GHz

* Hotspot 1.8.0_152

```
Benchmark                             (sample)   Mode  Cnt     Score      Error  Units
JoddBenchmark.parseCharCopy                  0  thrpt   10  6921,153 ± 473,762  ops/s
JoddBenchmark.parseCharCopy                  1  thrpt   10  2067,890 ±  296,707  ops/s
JoddBenchmark.parseCharCopy                  2  thrpt   10  1813,691 ±  222,156  ops/s
JoddBenchmark.parseCharCopy                  3  thrpt   10   974,880 ±   79,430  ops/s
JoddBenchmark.parseJava8CharDirect           0  thrpt   10  7785,972 ±  456,733  ops/s
JoddBenchmark.parseJava8CharDirect           1  thrpt   10  2303,590 ±  181,806  ops/s
JoddBenchmark.parseJava8CharDirect           2  thrpt   10  2019,688 ±  128,986  ops/s
JoddBenchmark.parseJava8CharDirect           3  thrpt   10  1063,761 ±  120,238  ops/s
JoddBenchmark.parseJava8CharStealing         0  thrpt   10  7617,431 ±  333,155  ops/s
JoddBenchmark.parseJava8CharStealing         1  thrpt   10  2314,005 ±  178,728  ops/s
JoddBenchmark.parseJava8CharStealing         2  thrpt   10  1994,047 ±  122,149  ops/s
JoddBenchmark.parseJava8CharStealing         3  thrpt   10  1045,304 ±   70,357  ops/s
JsoupBenchmark.parseInputStream              0  thrpt   10  5467,323 ±  366,465  ops/s
JsoupBenchmark.parseInputStream              1  thrpt   10  1711,234 ±  162,718  ops/s
JsoupBenchmark.parseInputStream              2  thrpt   10  1211,918 ±   73,561  ops/s
JsoupBenchmark.parseInputStream              3  thrpt   10   824,525 ±   57,874  ops/s
JsoupBenchmark.parseString                   0  thrpt   10  5781,451 ±  297,572  ops/s
JsoupBenchmark.parseString                   1  thrpt   10  1691,199 ±  165,585  ops/s
JsoupBenchmark.parseString                   2  thrpt   10  1329,760 ±   84,677  ops/s
JsoupBenchmark.parseString                   3  thrpt   10   783,537 ±   35,747  ops/s
```

* Hotspot 9.0.1

```
Benchmark                             (sample)   Mode  Cnt     Score     Error  Units
JoddBenchmark.parseCharCopy                  0  thrpt   10  6953,051 ± 147,808  ops/s
JoddBenchmark.parseCharCopy                  1  thrpt   10  2142,818 ±  47,455  ops/s
JoddBenchmark.parseCharCopy                  2  thrpt   10  1824,656 ±  53,191  ops/s
JoddBenchmark.parseCharCopy                  3  thrpt   10  1010,807 ±  32,047  ops/s
JoddBenchmark.parseJava8CharDirect           0  thrpt   10  6602,971 ± 281,799  ops/s
JoddBenchmark.parseJava8CharDirect           1  thrpt   10  2060,563 ±  75,228  ops/s
JoddBenchmark.parseJava8CharDirect           2  thrpt   10  1710,651 ±  89,290  ops/s
JoddBenchmark.parseJava8CharDirect           3  thrpt   10  1028,793 ±  18,218  ops/s
JoddBenchmark.parseJava8CharStealing         0  thrpt   10  6902,114 ± 222,657  ops/s
JoddBenchmark.parseJava8CharStealing         1  thrpt   10  2053,606 ±  60,965  ops/s
JoddBenchmark.parseJava8CharStealing         2  thrpt   10  1825,691 ±  99,056  ops/s
JoddBenchmark.parseJava8CharStealing         3  thrpt   10   995,919 ±  51,279  ops/s
JsoupBenchmark.parseInputStream              0  thrpt   10  5026,164 ± 248,086  ops/s
JsoupBenchmark.parseInputStream              1  thrpt   10  1445,581 ±  69,266  ops/s
JsoupBenchmark.parseInputStream              2  thrpt   10  1152,667 ±  52,952  ops/s
JsoupBenchmark.parseInputStream              3  thrpt   10   718,969 ±  21,563  ops/s
JsoupBenchmark.parseString                   0  thrpt   10  4773,896 ± 188,097  ops/s
JsoupBenchmark.parseString                   1  thrpt   10  1553,884 ±  62,492  ops/s
JsoupBenchmark.parseString                   2  thrpt   10  1190,102 ±  45,828  ops/s
JsoupBenchmark.parseString                   3  thrpt   10   697,228 ±  30,108  ops/s
```
