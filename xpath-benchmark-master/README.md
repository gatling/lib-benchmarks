# Java XPath microbenchmark

## Use case

Input is byte arrays. Benchmark accounts for byte decoding too.

## tl;dr

* Vtd roxx.
* Saxon is good.
* It can be interesting to let the InputSource resolve the decoding (to investigate)

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 2 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.9
* Hotspot 1.7.0_45
* Intel Core i7 2,7 GHz

Benchmark                                    Mode   Samples         Mean   Mean error    Units
VtdXmlBenchmark.parseByBytes                thrpt        20    64855,393     2344,320    ops/s
SaxonBenchmark.parseByString                thrpt        20    42345,536    11354,780    ops/s
SaxonBenchmark.parseByInputStream           thrpt        20    42175,149    11389,455    ops/s
SaxonBenchmark.parseByInputStreamReader     thrpt        20    40165,160    11613,026    ops/s
JaxenBenchmark.parseByInputStream           thrpt        20    26964,309    10986,125    ops/s
JaxenBenchmark.parseByInputStreamReader     thrpt        20    25912,111    10957,024    ops/s
JaxenBenchmark.parseByString                thrpt        20    24237,243    10583,853    ops/s
XalanBenchmark.parseByString                thrpt        20    21032,152     6372,248    ops/s
XalanBenchmark.parseByInputStream           thrpt        20    19611,578     6720,900    ops/s
XalanBenchmark.parseByInputStreamReader     thrpt        20    18451,115     7194,605    ops/s
