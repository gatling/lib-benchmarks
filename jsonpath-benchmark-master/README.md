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

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 1 -t 2`

## Results

On my machine:

* OS X 10.11.6
* Intel Core i7 2,7 GHz

## Mixed samples (all but ctim)

* Hotspot 1.8.0_152

```
Benchmark                                      Mode  Cnt      Score       Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   10  80853,876 ±  6291,685  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   10  71090,708 ±  5821,054  ops/s
GatlingGsonBenchmark.parseStream              thrpt   10  40026,154 ±  4323,134  ops/s
GatlingGsonBenchmark.parseString              thrpt   10  25100,214 ± 11799,070  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   10  55220,895 ±  8389,827  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   10  57987,895 ±  5801,920  ops/s
GatlingJacksonBenchmark.parseString           thrpt   10  46396,821 ±  2883,295  ops/s
GatlingJoddBenchmark.parseChars               thrpt   10  56538,900 ±  4321,514  ops/s
GatlingJoddBenchmark.parseString              thrpt   10  59449,246 ±  2093,954  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   10  40157,248 ±  3525,551  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   10  38889,575 ±  6282,904  ops/s
JaywayJacksonBenchmark.parseString            thrpt   10  33223,033 ±   974,104  ops/s
```

* Hotspot 9.0.1

```
Benchmark                                      Mode  Cnt      Score      Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   10  81700,309 ± 2818,302  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   10  68346,497 ± 1242,220  ops/s
GatlingGsonBenchmark.parseStream              thrpt   10  36514,021 ± 1241,700  ops/s
GatlingGsonBenchmark.parseString              thrpt   10  35901,389 ± 1272,903  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   10  51207,813 ±  622,924  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   10  52852,711 ± 2244,013  ops/s
GatlingJacksonBenchmark.parseString           thrpt   10  42654,964 ± 1002,359  ops/s
GatlingJoddBenchmark.parseChars               thrpt   10  55821,850 ± 2096,564  ops/s
GatlingJoddBenchmark.parseString              thrpt   10  52922,847 ± 1283,206  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   10  33399,178 ± 8513,829  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   10  31576,760 ± 6895,259  ops/s
JaywayJacksonBenchmark.parseString            thrpt   10  26068,378 ± 2984,721  ops/s
```
