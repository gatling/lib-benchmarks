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
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   20  161158,035 ±  8824,995  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   20  132423,682 ±  5447,258  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   20   93098,199 ±  4217,913  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   20   93695,175 ±  4991,920  ops/s
GatlingJoddBenchmark.parseString              thrpt   20   90681,642 ±  2938,727  ops/s
GatlingJacksonBenchmark.parseString           thrpt   20   72935,696 ±  2919,385  ops/s
GatlingGsonBenchmark.parseString              thrpt   20   60324,283 ±  1752,718  ops/s
GatlingGsonBenchmark.parseStream              thrpt   20   59077,786 ±  6316,518  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   20   54009,770 ± 11523,706  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   20   53816,269 ± 14157,799  ops/s
JaywayJacksonBenchmark.parseString            thrpt   20   45660,441 ± 11741,545  ops/s
```

* Hotspot 9


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
