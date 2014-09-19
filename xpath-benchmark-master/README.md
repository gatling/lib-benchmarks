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

Benchmark                                    Mode  Samples          Score  Score error  Units
VtdXmlBenchmark.parseByBytes                thrpt       20      53985,858     4169,190  ops/s
SaxonBenchmark.parseByString                thrpt       20      36755,979     9084,400  ops/s
SaxonBenchmark.parseByInputStreamReader     thrpt       20      36165,319     9001,755  ops/s
SaxonBenchmark.parseByInputStream           thrpt       20      34885,661     9729,814  ops/s
JaxenBenchmark.parseByInputStream           thrpt       20      25473,011     8909,831  ops/s
JaxenBenchmark.parseByInputStreamReader     thrpt       20      23755,716     9669,081  ops/s
JaxenBenchmark.parseByString                thrpt       20      23278,671     9939,924  ops/s
XalanBenchmark.parseByInputStream           thrpt       20      18551,164     5613,638  ops/s
XalanBenchmark.parseByString                thrpt       20      17537,998     5641,955  ops/s
XalanBenchmark.parseByInputStreamReader     thrpt       20      17267,983     6547,776  ops/s
