# JsonPath implementations microbenchmark for the JVM

## Goal

Compare performance of Gatling and Jayway's JsonPath implementations, combined with different parsers.

We're really comparing raw performance with the most common use case of US_ASCII payloads.
Comparing features and support for less common encodings is out fo scope.

Tested parsers are:
* Jackson
* Boon
* Jodd
* Gson

## Test Case

JSON payloads are split into 1.500 byte (1 MTU) arrays to emulate wire behavior.

The test mixes different payload sizes and paths.

We test different strategies (when supported by the JSON parser):
* directly parsing a stream,
* merging the bytes into one single array,
* merging the bytes into a String.

JsonPath paths are precompiled as both Gatling (in Gatling itself, no in the JsonPath impl) and Jayway caches them.

## tl;dr

* Gatling is almost 2x faster than Jayway (when both running on Jackson)
* The combo (Gatling + Boon) is more than 3x faster than (Jayway + Jackson)
* Boon lazy value loading is very well suited for JsonPath where you only want some branches of the JSON AST
* Results displayed on Jayway's website are completely flawed, as they only have path caching enabled for their side, not for Gatling.
* Jodd parser is really good, on par with Jackson on this use case (kudos Igor!)
* Gson falls way behind, Jackson is more than 2x faster (and make sure to cache `java.lang.reflect.Type` instances)

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 2 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.11.5
* Intel Core i7 2,7 GHz

## Mixed samples (all but ctim)

* Hotspot 1.8.0_144

```
Benchmark                                      Mode  Cnt       Score       Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   20  151475,884 ± 12983,119  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   20  127272,668 ± 10199,822  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   20  109715,738 ±  9110,354  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   20  107841,619 ±  4272,380  ops/s
GatlingJoddBenchmark.parseString              thrpt   20  107155,837 ±  2585,741  ops/s
GatlingJacksonBenchmark.parseString           thrpt   20   74964,176 ±  9246,998  ops/s
GatlingGsonBenchmark.parseStream              thrpt   20   72497,038 ±  6740,172  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   20   68721,749 ± 13416,786  ops/s
GatlingGsonBenchmark.parseString              thrpt   20   68481,498 ±  3619,926  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   20   69123,963 ± 12896,124  ops/s
JaywayJacksonBenchmark.parseString            thrpt   20   55148,091 ± 15866,026  ops/s
```

* Hotspot 9.0.1

Throughput is worse on String/chars based as we can not longer steal the char array.

But then, why this 15% drop on Jackson's streaming test?!

```
Benchmark                                      Mode  Cnt       Score       Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   20  138965,479 ± 13924,613  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   20  127286,336 ± 12986,275  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   20   91922,131 ±  9655,717  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   20   89301,599 ±  9195,296  ops/s
GatlingJoddBenchmark.parseString              thrpt   20   86636,260 ±  2313,392  ops/s
GatlingJacksonBenchmark.parseString           thrpt   20   74020,932 ±  2237,801  ops/s
GatlingGsonBenchmark.parseStream              thrpt   20   62359,981 ±  5025,541  ops/s
GatlingGsonBenchmark.parseString              thrpt   20   55755,679 ±  1375,446  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   20   53805,613 ± 12440,712  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   20   50562,505 ± 13264,107  ops/s
JaywayJacksonBenchmark.parseString            thrpt   20   43371,383 ± 15300,995  ops/s
```

* Hotspot 9 w/ `-XX:-CompactStrings`

Throughput is worse without Strings compaction, as expected.

```
Benchmark                                      Mode  Cnt       Score       Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   20  125507,384 ± 18338,106  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   20  123915,206 ± 11880,111  ops/s
GatlingJoddBenchmark.parseString              thrpt   20   76416,971 ±  4761,733  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   20   72043,731 ±  5701,158  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   20   65098,372 ±  9593,472  ops/s
GatlingJacksonBenchmark.parseString           thrpt   20   58436,080 ±  3427,715  ops/s
GatlingGsonBenchmark.parseStream              thrpt   20   48980,904 ±  2616,674  ops/s
GatlingGsonBenchmark.parseString              thrpt   20   44696,314 ±  2040,235  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   20   40605,518 ± 10190,595  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   20   40596,989 ± 11919,857  ops/s
JaywayJacksonBenchmark.parseString            thrpt   20   35631,530 ± 11168,617  ops/s
```
