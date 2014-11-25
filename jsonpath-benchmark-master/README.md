# JsonPath implementations microbenchmark for the JVM

## Use case

Input is arrays of 2kb byte arrays, similar to what you'd get with TCP chunks.

If the parser can't guess encoding by itself, we try to decode in the most efficient possible way, but encoding time is accounted for.

Paths are precompiled as it's the use case for Gatling (they are cached)

## tl;dr

* Gatling implementation is faster than Jayway. On reasonable file sizes where JSON parsing is not the overwhelming bottleneck, it can be 40% faster (both running w/ Jackson)
* For reasonable sizes (~ less than 1Mb):
  * it's more efficient to build the full byte array and decode it than use an InputStream over the chunks.
  * Boon parsers are faster and the FastLazyChop one is the fastest of all
* With huge files (~ more than 1 Mb) :
  * it's more efficient to use a stream. Note that only encodings included in the JSON RFC (UTF8, UTF16... but not ISO) are supported, as the parsers have then to handle decoding on the fly.
  * Jackson streaming parser is 11% faster than Boon.

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 2 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.9
* Hotspot 1.7.0_65
* Intel Core i7 2,7 GHz

## Mixed samples (all but ctim)

Benchmark                                        Mode  Samples       Score       Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars     thrpt       20  186118,674 ±  8357,490  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream    thrpt       20  149394,796 ±  2365,071  ops/s
GatlingJacksonBenchmark.parseStream             thrpt       20  109691,835 ±  5683,142  ops/s
GatlingJacksonBenchmark.parseBytes              thrpt       20  106180,395 ±  5654,098  ops/s
GatlingJoddBenchmark.parseString                thrpt       20   93009,909 ± 13214,936  ops/s
GatlingJacksonBenchmark.parseString             thrpt       20   87243,259 ±  4497,959  ops/s
JaywayJacksonBenchmark.parseStream              thrpt       20   57914,202 ± 20378,925  ops/s
JaywayJacksonBenchmark.parseBytes               thrpt       20   56370,944 ± 20854,934  ops/s
JaywayJacksonBenchmark.parseString              thrpt       20   49825,720 ± 15457,201  ops/s

## 20k sample only

Benchmark                                        Mode  Samples      Score      Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars     thrpt       20  49240,095 ± 3879,274  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream    thrpt       20  41307,542 ± 1561,146  ops/s
GatlingJacksonBenchmark.parseStream             thrpt       20  34963,083 ± 1011,595  ops/s
GatlingJacksonBenchmark.parseBytes              thrpt       20  34243,066 ±  763,881  ops/s
GatlingJoddBenchmark.parseString                thrpt       20  33115,194 ±  713,440  ops/s
GatlingJacksonBenchmark.parseString             thrpt       20  27478,888 ±  652,178  ops/s
JaywayJacksonBenchmark.parseBytes               thrpt       20  18510,730 ±  910,777  ops/s
JaywayJacksonBenchmark.parseStream              thrpt       20  18420,036 ± 1096,330  ops/s
JaywayJacksonBenchmark.parseString              thrpt       20  14079,054 ± 1562,821  ops/s

## Goessner sample only

Benchmark                                        Mode  Samples        Score        Error  Units
GatlingBoonFastLazyChopBenchmark.parseChars     thrpt       20  1102470,690 ± 102015,222  ops/s
GatlingJoddBenchmark.parseString                thrpt       20   856405,758 ±  17234,631  ops/s
GatlingJacksonBenchmark.parseBytes              thrpt       20   770826,821 ±  40780,630  ops/s
GatlingJacksonBenchmark.parseStream             thrpt       20   765414,049 ±  34291,891  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream    thrpt       20   651946,978 ±  20706,239  ops/s
GatlingJacksonBenchmark.parseString             thrpt       20   628141,745 ±  12684,409  ops/s
JaywayJacksonBenchmark.parseStream              thrpt       20   387636,226 ±  56901,300  ops/s
JaywayJacksonBenchmark.parseBytes               thrpt       20   386902,239 ±  55626,744  ops/s
JaywayJacksonBenchmark.parseString              thrpt       20   354256,512 ±  34778,270  ops/s

## ctim sample only

Benchmark                                        Mode  Samples    Score    Error  Units
GatlingJacksonBenchmark.parseStream             thrpt       20  694,064 ± 20,722  ops/s
JaywayJacksonBenchmark.parseStream              thrpt       20  674,110 ± 21,421  ops/s
GatlingJacksonBenchmark.parseBytes              thrpt       20  649,871 ± 20,108  ops/s
JaywayJacksonBenchmark.parseBytes               thrpt       20  641,111 ± 19,798  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream    thrpt       20  458,567 ± 31,940  ops/s
GatlingBoonFastLazyChopBenchmark.parseChars     thrpt       20  349,869 ±  9,805  ops/s
GatlingJacksonBenchmark.parseString             thrpt       20  318,317 ± 10,975  ops/s
GatlingJoddBenchmark.parseString                thrpt       20  315,294 ± 10,633  ops/s
JaywayJacksonBenchmark.parseString              thrpt       20  300,458 ±  9,581  ops/s
