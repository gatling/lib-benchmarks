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
Benchmark                                      Mode  Cnt      Score      Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   10  80468,046 ± 7355,644  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   10  75888,512 ± 4650,257  ops/s
GatlingGsonBenchmark.parseStream              thrpt   10  44631,026 ± 3225,640  ops/s
GatlingGsonBenchmark.parseString              thrpt   10  42903,901 ± 2925,265  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   10  67605,305 ±  571,420  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   10  65232,374 ±  965,258  ops/s
GatlingJacksonBenchmark.parseString           thrpt   10  50172,777 ± 1065,348  ops/s
GatlingJoddBenchmark.parseChars               thrpt   10  66158,637 ± 1677,050  ops/s
GatlingJoddBenchmark.parseString              thrpt   10  65220,236 ± 2333,181  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   10  37302,421 ± 5504,294  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   10  37718,040 ± 4406,431  ops/s
JaywayJacksonBenchmark.parseString            thrpt   10  32718,206 ± 1878,710  ops/s
```

* Hotspot 9.0.4

```
Benchmark                                      Mode  Cnt      Score      Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars   thrpt   10  85775,111 ± 1795,096  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream  thrpt   10  78253,038 ±  682,372  ops/s
GatlingGsonBenchmark.parseStream              thrpt   10  38075,738 ±  989,597  ops/s
GatlingGsonBenchmark.parseString              thrpt   10  35669,645 ± 1735,368  ops/s
GatlingJacksonBenchmark.parseBytes            thrpt   10  53895,170 ± 2362,698  ops/s
GatlingJacksonBenchmark.parseStream           thrpt   10  54464,900 ± 1017,869  ops/s
GatlingJacksonBenchmark.parseString           thrpt   10  44484,847 ±  701,679  ops/s
GatlingJoddBenchmark.parseChars               thrpt   10  61153,146 ±  636,044  ops/s
GatlingJoddBenchmark.parseString              thrpt   10  59836,125 ±  900,065  ops/s
JaywayJacksonBenchmark.parseBytes             thrpt   10  34767,931 ± 4067,293  ops/s
JaywayJacksonBenchmark.parseStream            thrpt   10  33894,588 ± 3975,318  ops/s
JaywayJacksonBenchmark.parseString            thrpt   10  28869,052 ± 1325,071  ops/s
```
