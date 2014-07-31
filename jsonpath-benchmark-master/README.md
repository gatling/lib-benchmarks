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

Benchmark                                                 Mode   Samples        Score  Score error    Units
GatlingBoonFastLazyChopBenchmark.parseChars              thrpt       160    21874,882      495,279    ops/s
GatlingBoonFastLazyChopBenchmark.parseStream             thrpt       160    17412,713      441,075    ops/s
GatlingBoonFastChopBenchmark.parseChars                  thrpt       160    16359,011      510,775    ops/s
GatlingBoonCharArrayBenchmark.parseChars                 thrpt       160    14511,369      184,925    ops/s
GatlingBoonFastChopBenchmark.parseStream                 thrpt       160    13455,530      231,304    ops/s
GatlingJacksonBenchmark.parseStream                      thrpt       160    13194,622      242,971    ops/s
GatlingJacksonBenchmark.parseBytes                       thrpt       160    12700,469      231,775    ops/s
GatlingBoonCharArrayBenchmark.parseStream                thrpt       160    10546,809      459,368    ops/s
GatlingBoonReaderCharacterSourceBenchmark.parseStream    thrpt       160    10187,768       94,574    ops/s
GatlingJacksonBenchmark.parseString                      thrpt       160    10222,547      176,766    ops/s
JaywayJacksonBenchmark.parseString                       thrpt       160     6888,191      550,413    ops/s
JaywayJacksonBenchmark.parseBytes                        thrpt       160     7471,968      704,450    ops/s
JaywayJacksonBenchmark.parseStream                       thrpt       160     6703,087      677,972    ops/s

## 20k sample only

Benchmark                                                 Mode   Samples        Score  Score error    Units
GatlingBoonFastLazyChopBenchmark.parseChars              thrpt       160     5956,583       60,750    ops/s
GatlingBoonFastLazyChopBenchmark.parseStream             thrpt       160     4838,434       56,550    ops/s
GatlingBoonCharArrayBenchmark.parseChars                 thrpt       160     4619,816       64,971    ops/s
GatlingBoonFastChopBenchmark.parseChars                  thrpt       160     4433,334       76,683    ops/s
GatlingJacksonBenchmark.parseStream                      thrpt       160     4292,995       42,877    ops/s
GatlingJacksonBenchmark.parseBytes                       thrpt       160     4016,089       88,581    ops/s
GatlingBoonCharArrayBenchmark.parseStream                thrpt       160     3958,917       43,986    ops/s
GatlingBoonFastChopBenchmark.parseStream                 thrpt       160     3844,165       63,868    ops/s
GatlingJacksonBenchmark.parseString                      thrpt       160     3317,613       63,449    ops/s
GatlingBoonReaderCharacterSourceBenchmark.parseStream    thrpt       160     3155,368       36,961    ops/s
JaywayJacksonBenchmark.parseBytes                        thrpt       160     2644,440       48,158    ops/s
JaywayJacksonBenchmark.parseStream                       thrpt       160     2650,719       84,001    ops/s
JaywayJacksonBenchmark.parseString                       thrpt       160     2326,867       45,328    ops/s

## Goessner sample only

Benchmark                                                 Mode   Samples        Score  Score error    Units
GatlingBoonFastLazyChopBenchmark.parseChars              thrpt       160   151081,255     1166,517    ops/s
GatlingBoonCharArrayBenchmark.parseChars                 thrpt       160   123532,606     1672,413    ops/s
GatlingBoonFastChopBenchmark.parseChars                  thrpt       160   123250,196      631,587    ops/s
GatlingJacksonBenchmark.parseStream                      thrpt       160    99950,126      959,769    ops/s
GatlingJacksonBenchmark.parseBytes                       thrpt       160    96559,380     1503,335    ops/s
GatlingBoonFastLazyChopBenchmark.parseStream             thrpt       160    82665,705     1004,288    ops/s
GatlingJacksonBenchmark.parseString                      thrpt       160    74386,403     1787,032    ops/s
GatlingBoonCharArrayBenchmark.parseStream                thrpt       160    74076,573     1977,031    ops/s
GatlingBoonFastChopBenchmark.parseStream                 thrpt       160    74032,647      197,013    ops/s
GatlingBoonReaderCharacterSourceBenchmark.parseStream    thrpt       160    45720,274     1123,649    ops/s
JaywayJacksonBenchmark.parseBytes                        thrpt       160    35590,592     3778,002    ops/s
JaywayJacksonBenchmark.parseStream                       thrpt       160    37671,594     3164,796    ops/s
JaywayJacksonBenchmark.parseString                       thrpt       160    30583,976     2949,273    ops/s

## ctim sample only

Benchmark                                                 Mode   Samples        Score  Score error    Units
GatlingJacksonBenchmark.parseStream                      thrpt       160       87,135        0,693    ops/s
JaywayJacksonBenchmark.parseStream                       thrpt       160       78,892        0,978    ops/s
GatlingBoonReaderCharacterSourceBenchmark.parseStream    thrpt       160       78,297        0,718    ops/s
JaywayJacksonBenchmark.parseBytes                        thrpt       160       75,450        0,906    ops/s
GatlingJacksonBenchmark.parseBytes                       thrpt       160       78,198        0,657    ops/s
GatlingBoonFastLazyChopBenchmark.parseStream             thrpt       160       58,982        1,449    ops/s
GatlingBoonCharArrayBenchmark.parseStream                thrpt       160       58,922        1,116    ops/s
GatlingBoonFastChopBenchmark.parseStream                 thrpt       160       50,592        1,757    ops/s
GatlingBoonFastLazyChopBenchmark.parseChars              thrpt       160       43,925        0,865    ops/s
GatlingBoonCharArrayBenchmark.parseChars                 thrpt       160       41,765        1,218    ops/s
GatlingBoonFastChopBenchmark.parseChars                  thrpt       160       40,712        0,780    ops/s
JaywayJacksonBenchmark.parseString                       thrpt       160       38,571        0,829    ops/s
GatlingJacksonBenchmark.parseString                      thrpt       160       37,719        0,724    ops/s
