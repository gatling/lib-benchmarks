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
* Gson falls way behind, Jackson is more than 2x faster

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 2 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.11.5
* Hotspot 1.8.0_77
* Intel Core i7 2,7 GHz

## Mixed samples (all but ctim)

```
Benchmark                                      Mode  Cnt       Score       Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   20  166980.840 ± 10030.153  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   20  131656.599 ±  4366.885  ops/s
GatlingJoddBenchmark.parseString              thrpt   20   92743.379 ±  3776.058  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   20   92057.607 ±  3289.082  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   20   89523.231 ±  4704.703  ops/s
GatlingJacksonBenchmark.parseString           thrpt   20   70755.612 ±  7998.238  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   20   52415.892 ± 12670.350  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   20   51573.923 ± 14242.533  ops/s
GatlingGsonBenchmark.parseStream              thrpt   20   49122.814 ±  1107.523  ops/s
JaywayJacksonBenchmark.parseString            thrpt   20   49969.903 ±  9855.656  ops/s
GatlingGsonBenchmark.parseString              thrpt   20   48235.869 ±  3264.287  ops/s
```
