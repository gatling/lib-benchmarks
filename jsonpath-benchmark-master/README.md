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
* Hotspot 1.8.0_92
* Intel Core i7 2,7 GHz

## Mixed samples (all but ctim)

```
Benchmark                                      Mode  Cnt       Score       Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   20  167881.520 ± 12711.566  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   20  137721.616 ±  6226.733  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   20  104647.384 ±  8054.949  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   20  101119.816 ±  6531.190  ops/s
GatlingJoddBenchmark.parseString              thrpt   20  100136.823 ±  5540.100  ops/s
GatlingJacksonBenchmark.parseString           thrpt   20   76796.960 ±  3229.228  ops/s
GatlingGsonBenchmark.parseStream              thrpt   20   55374.483 ±  1893.856  ops/s
GatlingGsonBenchmark.parseString              thrpt   20   55235.131 ±  3894.822  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   20   54101.148 ± 12818.663  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   20   52875.429 ± 14888.563  ops/s
JaywayJacksonBenchmark.parseString            thrpt   20   51363.638 ±  9740.279  ops/s
```
