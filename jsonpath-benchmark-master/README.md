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
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   20  170899,724 ± 11157,228  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   20  126329,481 ±  8580,265  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   20  100677,793 ±  5423,951  ops/s
GatlingJoddBenchmark.parseString              thrpt   20   98804,930 ±  2647,410  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   20   97664,189 ±  6372,184  ops/s
GatlingJacksonBenchmark.parseString           thrpt   20   74388,457 ±  6799,661  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   20   56146,845 ± 13827,850  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   20   55683,243 ± 11910,780  ops/s
GatlingGsonBenchmark.parseStream              thrpt   20   51109,222 ±  2707,531  ops/s
JaywayJacksonBenchmark.parseString            thrpt   20   49749,131 ±  9594,332  ops/s
GatlingGsonBenchmark.parseString              thrpt   20   49382,793 ±   839,113  ops/s
```
