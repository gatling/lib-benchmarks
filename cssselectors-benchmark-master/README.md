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
Benchmark                             (sample)   Mode  Cnt     Score     Error  Units
JoddBenchmark.parseCharCopy                  0  thrpt   10  6869,905 ± 400,670  ops/s
JoddBenchmark.parseCharCopy                  1  thrpt   10  2169,820 ± 198,049  ops/s
JoddBenchmark.parseCharCopy                  2  thrpt   10  1897,677 ± 112,638  ops/s
JoddBenchmark.parseCharCopy                  3  thrpt   10   973,235 ±  65,263  ops/s
JoddBenchmark.parseJava8CharDirect           0  thrpt   10  7002,087 ± 508,897  ops/s
JoddBenchmark.parseJava8CharDirect           1  thrpt   10  2216,921 ± 161,260  ops/s
JoddBenchmark.parseJava8CharDirect           2  thrpt   10  2006,876 ± 155,461  ops/s
JoddBenchmark.parseJava8CharDirect           3  thrpt   10  1050,916 ±  93,564  ops/s
JoddBenchmark.parseJava8CharStealing         0  thrpt   10  7412,964 ± 449,814  ops/s
JoddBenchmark.parseJava8CharStealing         1  thrpt   10  2256,037 ± 164,466  ops/s
JoddBenchmark.parseJava8CharStealing         2  thrpt   10  2087,241 ± 142,441  ops/s
JoddBenchmark.parseJava8CharStealing         3  thrpt   10  1125,922 ±  73,804  ops/s
JsoupBenchmark.parseInputStream              0  thrpt   10  6077,993 ± 226,430  ops/s
JsoupBenchmark.parseInputStream              1  thrpt   10  1870,415 ± 119,059  ops/s
JsoupBenchmark.parseInputStream              2  thrpt   10  1310,585 ±  60,602  ops/s
JsoupBenchmark.parseInputStream              3  thrpt   10   845,028 ±  75,926  ops/s
JsoupBenchmark.parseString                   0  thrpt   10  6096,232 ± 291,794  ops/s
JsoupBenchmark.parseString                   1  thrpt   10  1856,219 ± 158,545  ops/s
JsoupBenchmark.parseString                   2  thrpt   10  1570,459 ±  49,091  ops/s
JsoupBenchmark.parseString                   3  thrpt   10   815,986 ±  31,431  ops/s
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
