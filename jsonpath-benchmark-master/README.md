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
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   20  159690,198 ± 14888,345  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   20  138554,445 ±  5429,010  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   20  101789,076 ±  3822,680  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   20   99051,635 ±  4331,016  ops/s
GatlingJoddBenchmark.parseString              thrpt   20   89691,914 ±  2720,971  ops/s
GatlingJacksonBenchmark.parseString           thrpt   20   73114,256 ±  3323,935  ops/s
GatlingGsonBenchmark.parseStream              thrpt   20   63774,358 ±  1206,335  ops/s
GatlingGsonBenchmark.parseString              thrpt   20   59395,978 ±  1909,143  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   20   56902,510 ± 13442,586  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   20   53195,302 ± 13796,420  ops/s
JaywayJacksonBenchmark.parseString            thrpt   20   46515,968 ± 13186,301  ops/s
```

* Hotspot 9

Throughput is worse on String/chars based as we can not longer steal the char array.

But then, why this 15% drop on Jackson's streaming test?!

```
Benchmark                                      Mode  Cnt       Score       Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   20  135162,644 ± 13695,767  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   20  121025,611 ± 16461,271  ops/s
GatlingJoddBenchmark.parseString              thrpt   20   79494,498 ±  2112,468  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   20   73609,232 ± 10003,623  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   20   75065,992 ± 13137,789  ops/s
GatlingJacksonBenchmark.parseString           thrpt   20   65268,017 ±  1699,223  ops/s
GatlingGsonBenchmark.parseString              thrpt   20   60019,026 ±  3364,905  ops/s
GatlingGsonBenchmark.parseStream              thrpt   20   55509,738 ±  2937,773  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   20   44890,883 ± 11052,027  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   20   43444,705 ± 10837,909  ops/s
JaywayJacksonBenchmark.parseString            thrpt   20   38342,228 ± 11085,434  ops/s
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
