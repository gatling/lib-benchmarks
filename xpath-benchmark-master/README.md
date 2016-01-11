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

```
Benchmark                                   Mode  Cnt      Score       Error  Units
SaxonBenchmark.parseByInputStream          thrpt   20  44484.272 ±  5531.686  ops/s
SaxonBenchmark.parseByFastStringReader     thrpt   20  43485.904 ±  8064.646  ops/s
SaxonBenchmark.parseByFastCharArrayReader  thrpt   20  42844.276 ±  8881.574  ops/s
SaxonBenchmark.parseByString               thrpt   20  42536.179 ±  7429.866  ops/s
SaxonBenchmark.parseByInputStreamReader    thrpt   20  41259.098 ±  8097.612  ops/s
JaxenBenchmark.parseByFastCharArrayReader  thrpt   20  35389.838 ±  3271.170  ops/s
JaxenBenchmark.parseByFastStringReader     thrpt   20  32632.778 ±  3149.830  ops/s
JaxenBenchmark.parseByInputStream          thrpt   20  32463.902 ±  4973.274  ops/s
JaxenBenchmark.parseByString               thrpt   20  32067.337 ±  7173.274  ops/s
JaxenBenchmark.parseByInputStreamReader    thrpt   20  29764.003 ± 10303.690  ops/s
XalanBenchmark.parseByFastCharArrayReader  thrpt   20  26335.898 ±  4437.636  ops/s
JdkBenchmark.parseByFastCharArrayReader    thrpt   20  26271.308 ±  4700.657  ops/s
XalanBenchmark.parseByString               thrpt   20  26121.188 ±  4609.526  ops/s
XalanBenchmark.parseByFastStringReader     thrpt   20  25789.210 ±  4054.097  ops/s
XalanBenchmark.parseByInputStreamReader    thrpt   20  25777.591 ±  5091.522  ops/s
JdkBenchmark.parseByFastStringReader       thrpt   20  25670.606 ±  5493.885  ops/s
JdkBenchmark.parseByInputStreamReader      thrpt   20  25132.670 ±  3795.600  ops/s
JdkBenchmark.parseByInputStream            thrpt   20  24680.044 ±  5023.568  ops/s
JdkBenchmark.parseByString                 thrpt   20  24804.053 ±  5354.838  ops/s
XalanBenchmark.parseByInputStream          thrpt   20  23770.958 ±  5388.873  ops/s
```
