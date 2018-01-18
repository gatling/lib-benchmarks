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
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   10  81438,487 ±  7884,274  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   10  72347,670 ±  5625,713  ops/s
GatlingGsonBenchmark.parseStream              thrpt   10  43644,572 ±   554,244  ops/s
GatlingGsonBenchmark.parseString              thrpt   10  36032,845 ±   591,480  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   10  65844,929 ±  1051,313  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   10  59293,301 ±   727,075  ops/s
GatlingJacksonBenchmark.parseString           thrpt   10  45993,126 ±  2109,235  ops/s
GatlingJoddBenchmark.parseChars               thrpt   10  64554,183 ±  4926,144  ops/s
GatlingJoddBenchmark.parseString              thrpt   10  59505,390 ±  5951,782  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   10  38539,059 ± 13083,278  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   10  38746,109 ±  6300,326  ops/s
JaywayJacksonBenchmark.parseString            thrpt   10  33526,414 ±  1266,529  ops/s
```

* Hotspot 9.0.1

```
Benchmark                                      Mode  Cnt      Score      Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   10  83257,982 ± 1271,641  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   10  74985,646 ± 1035,638  ops/s
GatlingGsonBenchmark.parseStream              thrpt   10  36742,415 ± 1193,219  ops/s
GatlingGsonBenchmark.parseString              thrpt   10  36542,813 ± 1075,717  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   10  56436,790 ±  449,192  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   10  53866,194 ± 1262,136  ops/s
GatlingJacksonBenchmark.parseString           thrpt   10  44201,083 ±  600,122  ops/s
GatlingJoddBenchmark.parseChars               thrpt   10  61574,597 ±  937,379  ops/s
GatlingJoddBenchmark.parseString              thrpt   10  58914,208 ± 1075,230  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   10  35359,728 ± 6533,524  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   10  32537,431 ± 7261,401  ops/s
JaywayJacksonBenchmark.parseString            thrpt   10  27689,200 ± 2694,700  ops/s
```
