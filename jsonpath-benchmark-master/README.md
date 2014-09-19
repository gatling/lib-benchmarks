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
* Hotspot 1.7.0_60
* Intel Core i7 2,7 GHz

## Mixed samples (all but ctim)

Benchmark                                        Mode  Samples       Score  Score error  Units
GatlingBoonFastLazyChopBenchmark.parseChars     thrpt       20  184289,310     5269,533  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream    thrpt       20  148869,384     2775,139  ops/s
GatlingJacksonBenchmark.parseStream             thrpt       20  105902,449     3332,773  ops/s
GatlingJacksonBenchmark.parseBytes              thrpt       20  101674,842     5143,923  ops/s
GatlingJoddBenchmark.parseString                thrpt       20   96152,298     6603,967  ops/s
GatlingJacksonBenchmark.parseString             thrpt       20   85305,160     3822,595  ops/s
JaywayJacksonBenchmark.parseBytes               thrpt       20   62418,382    17195,339  ops/s
JaywayJacksonBenchmark.parseStream              thrpt       20   67342,448    17061,375  ops/s
JaywayJacksonBenchmark.parseString              thrpt       20   58170,730    13918,157  ops/s

## 20k sample only

Benchmark                                        Mode  Samples      Score  Score error  Units
GatlingBoonFastLazyChopBenchmark.parseChars     thrpt       20  47206,747     2820,638  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream    thrpt       20  40514,236     1587,758  ops/s
GatlingJacksonBenchmark.parseBytes              thrpt       20  31601,104     2345,976  ops/s
GatlingJoddBenchmark.parseString                thrpt       20  31586,681     1499,591  ops/s
GatlingJacksonBenchmark.parseStream             thrpt       20  31826,711     1388,085  ops/s
GatlingJacksonBenchmark.parseString             thrpt       20  27038,277      483,240  ops/s
JaywayJacksonBenchmark.parseBytes               thrpt       20  20676,328     1476,609  ops/s
JaywayJacksonBenchmark.parseStream              thrpt       20  20596,809     1943,183  ops/s
JaywayJacksonBenchmark.parseString              thrpt       20  17863,194     1094,422  ops/s

## Goessner sample only

Benchmark                                        Mode  Samples        Score  Score error  Units
GatlingBoonFastLazyChopBenchmark.parseChars     thrpt       20  1105703,871    67837,778  ops/s
GatlingJoddBenchmark.parseString                thrpt       20   802629,303    33630,235  ops/s
GatlingJacksonBenchmark.parseStream             thrpt       20   767863,298    28280,391  ops/s
GatlingJacksonBenchmark.parseBytes              thrpt       20   760720,580    27704,993  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream    thrpt       20   706117,120    12323,651  ops/s
GatlingJacksonBenchmark.parseString             thrpt       20   589345,772    26923,026  ops/s
JaywayJacksonBenchmark.parseBytes               thrpt       20   277781,276    88454,873  ops/s
JaywayJacksonBenchmark.parseStream              thrpt       20   258522,487    98043,685  ops/s
JaywayJacksonBenchmark.parseString              thrpt       20   223120,663    89493,964  ops/s

## ctim sample only

Benchmark                                        Mode  Samples    Score  Score error  Units
GatlingJacksonBenchmark.parseStream             thrpt       20  574,645       22,270  ops/s
GatlingJacksonBenchmark.parseBytes              thrpt       20  541,050       19,288  ops/s
JaywayJacksonBenchmark.parseStream              thrpt       20  533,120       32,594  ops/s
JaywayJacksonBenchmark.parseBytes               thrpt       20  508,366       27,050  ops/s
GatlingBoonFastLazyChopBenchmark.parseStream    thrpt       20  438,639       30,258  ops/s
GatlingBoonFastLazyChopBenchmark.parseChars     thrpt       20  348,732       23,221  ops/s
GatlingJacksonBenchmark.parseString             thrpt       20  286,069       13,453  ops/s
GatlingJoddBenchmark.parseString                thrpt       20  280,345       13,446  ops/s
JaywayJacksonBenchmark.parseString              thrpt       20  269,781        9,688  ops/s
