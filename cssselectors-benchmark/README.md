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
JoddBenchmark.parseCharCopy                  0  thrpt   10  7602,170 ± 426,430  ops/s
JoddBenchmark.parseCharCopy                  1  thrpt   10  2293,539 ± 119,860  ops/s
JoddBenchmark.parseCharCopy                  2  thrpt   10  1875,176 ± 120,986  ops/s
JoddBenchmark.parseCharCopy                  3  thrpt   10  1014,676 ±  71,370  ops/s
JoddBenchmark.parseJava8CharDirect           0  thrpt   10  6814,176 ± 920,668  ops/s
JoddBenchmark.parseJava8CharDirect           1  thrpt   10  2208,485 ± 161,425  ops/s
JoddBenchmark.parseJava8CharDirect           2  thrpt   10  1938,298 ± 122,618  ops/s
JoddBenchmark.parseJava8CharDirect           3  thrpt   10  1060,934 ±  86,567  ops/s
JoddBenchmark.parseJava8CharStealing         0  thrpt   10  7449,596 ± 511,578  ops/s
JoddBenchmark.parseJava8CharStealing         1  thrpt   10  2270,645 ± 142,098  ops/s
JoddBenchmark.parseJava8CharStealing         2  thrpt   10  1994,885 ± 102,658  ops/s
JoddBenchmark.parseJava8CharStealing         3  thrpt   10  1067,349 ±  61,572  ops/s
JsoupBenchmark.parseInputStream              0  thrpt   10  5865,700 ± 265,706  ops/s
JsoupBenchmark.parseInputStream              1  thrpt   10  1829,575 ± 163,042  ops/s
JsoupBenchmark.parseInputStream              2  thrpt   10  1291,425 ±  90,803  ops/s
JsoupBenchmark.parseInputStream              3  thrpt   10   836,827 ±  52,197  ops/s
JsoupBenchmark.parseString                   0  thrpt   10  6356,313 ± 284,043  ops/s
JsoupBenchmark.parseString                   1  thrpt   10  1803,461 ± 103,913  ops/s
JsoupBenchmark.parseString                   2  thrpt   10  1423,622 ±  93,793  ops/s
JsoupBenchmark.parseString                   3  thrpt   10   808,842 ±  39,898  ops/s
```

* Hotspot 9.0.4

```
Benchmark                             (sample)   Mode  Cnt     Score     Error  Units
JoddBenchmark.parseCharCopy                  0  thrpt   10  6593,351 ± 155,707  ops/s
JoddBenchmark.parseCharCopy                  1  thrpt   10  1866,367 ± 178,507  ops/s
JoddBenchmark.parseCharCopy                  2  thrpt   10  1667,855 ± 155,705  ops/s
JoddBenchmark.parseCharCopy                  3  thrpt   10   919,625 ±  40,457  ops/s
JoddBenchmark.parseJava8CharDirect           0  thrpt   10  6883,502 ± 261,179  ops/s
JoddBenchmark.parseJava8CharDirect           1  thrpt   10  2011,932 ±  78,692  ops/s
JoddBenchmark.parseJava8CharDirect           2  thrpt   10  1853,802 ± 111,963  ops/s
JoddBenchmark.parseJava8CharDirect           3  thrpt   10  1015,729 ±  22,436  ops/s
JoddBenchmark.parseJava8CharStealing         0  thrpt   10  6651,487 ±  74,787  ops/s
JoddBenchmark.parseJava8CharStealing         1  thrpt   10  2058,189 ±  67,958  ops/s
JoddBenchmark.parseJava8CharStealing         2  thrpt   10  1808,361 ±  48,483  ops/s
JoddBenchmark.parseJava8CharStealing         3  thrpt   10   988,039 ±  31,291  ops/s
JsoupBenchmark.parseInputStream              0  thrpt   10  4454,507 ± 185,641  ops/s
JsoupBenchmark.parseInputStream              1  thrpt   10  1485,116 ± 125,248  ops/s
JsoupBenchmark.parseInputStream              2  thrpt   10  1121,662 ±  28,358  ops/s
JsoupBenchmark.parseInputStream              3  thrpt   10   761,868 ±  17,561  ops/s
JsoupBenchmark.parseString                   0  thrpt   10  4907,430 ±  74,819  ops/s
JsoupBenchmark.parseString                   1  thrpt   10  1573,181 ±  49,155  ops/s
JsoupBenchmark.parseString                   2  thrpt   10  1252,711 ±  34,559  ops/s
JsoupBenchmark.parseString                   3  thrpt   10   694,041 ±  24,655  ops/s
```
