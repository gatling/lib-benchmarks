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

Benchmark                                      Mode  Samples          Score  Score error  Units
VtdXmlBenchmark.parseByBytes                  thrpt       20      56950,205     4400,655  ops/s
SaxonBenchmark.parseByInputStreamReader       thrpt       20      37263,022     9487,951  ops/s
SaxonBenchmark.parseByString                  thrpt       20      36692,406    11232,677  ops/s
SaxonBenchmark.parseByInputStream             thrpt       20      34663,237     9490,812  ops/s
SaxonBenchmark.parseByFastCharArrayReader     thrpt       20      34653,910     8960,351  ops/s
SaxonBenchmark.parseByFastStringReader        thrpt       20      33344,311     9117,041  ops/s
JaxenBenchmark.parseByFastCharArrayReader     thrpt       20      27318,785     9950,762  ops/s
JaxenBenchmark.parseByInputStreamReader       thrpt       20      23464,951     9035,356  ops/s
JaxenBenchmark.parseByString                  thrpt       20      22576,293     9646,487  ops/s
JaxenBenchmark.parseByFastStringReader        thrpt       20      19368,277     8664,141  ops/s
XalanBenchmark.parseByFastStringReader        thrpt       20      18999,798     6441,667  ops/s
XalanBenchmark.parseByInputStream             thrpt       20      18653,993     6360,934  ops/s
XalanBenchmark.parseByFastCharArrayReader     thrpt       20      18421,654     6110,553  ops/s
JaxenBenchmark.parseByInputStream             thrpt       20      18232,522     9619,912  ops/s
XalanBenchmark.parseByString                  thrpt       20      17871,921     5977,102  ops/s
XalanBenchmark.parseByInputStreamReader       thrpt       20      16581,856     5889,130  ops/s
