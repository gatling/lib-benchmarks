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

Benchmark                                     Mode  Samples      Score       Error  Units
SaxonBenchmark.parseByString                 thrpt       20  42270,390 ± 10096,629  ops/s
SaxonBenchmark.parseByInputStreamReader      thrpt       20  40930,556 ±  9612,037  ops/s
SaxonBenchmark.parseByFastStringReader       thrpt       20  38905,475 ± 11402,333  ops/s
SaxonBenchmark.parseByInputStream            thrpt       20  38699,390 ±  9059,772  ops/s
SaxonBenchmark.parseByFastCharArrayReader    thrpt       20  38492,946 ± 10788,040  ops/s
XalanBenchmark.parseByString                 thrpt       20  23470,851 ±  5523,252  ops/s
JaxenBenchmark.parseByInputStream            thrpt       20  23454,737 ±  9183,471  ops/s
XalanBenchmark.parseByFastStringReader       thrpt       20  22971,374 ±  5193,801  ops/s
JaxenBenchmark.parseByString                 thrpt       20  22476,674 ±  9991,924  ops/s
JaxenBenchmark.parseByFastStringReader       thrpt       20  22230,058 ±  9551,464  ops/s
XalanBenchmark.parseByInputStream            thrpt       20  21816,979 ±  6408,673  ops/s
XalanBenchmark.parseByInputStreamReader      thrpt       20  21774,737 ±  6175,904  ops/s
XalanBenchmark.parseByFastCharArrayReader    thrpt       20  21345,784 ±  6801,953  ops/s
JaxenBenchmark.parseByFastCharArrayReader    thrpt       20  21306,759 ±  8954,565  ops/s
JaxenBenchmark.parseByInputStreamReader      thrpt       20  20240,123 ±  9708,106  ops/s
