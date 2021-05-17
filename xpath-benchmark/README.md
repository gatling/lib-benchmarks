# Java XPath microbenchmark

## Use case

Input is byte arrays. Benchmark accounts for byte decoding too.

## tl;dr

* Saxon is good.
* It can be interesting to let the InputSource resolve the decoding (to investigate)

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 1 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.11.1
* Hotspot 1.8.0_66
* Intel Core i7 2,7 GHz

```
Benchmark                                   Mode  Cnt      Score      Error  Units
SaxonBenchmark.parseByFastStringReader     thrpt   20  53263.223 ±  685.128  ops/s
SaxonBenchmark.parseByInputStream          thrpt   20  53795.796 ±  584.690  ops/s
SaxonBenchmark.parseByString               thrpt   20  49590.015 ± 2367.045  ops/s
JdkBenchmark.parseByFastStringReader       thrpt   20  30276.117 ±  645.953  ops/s
JdkBenchmark.parseByString                 thrpt   20  29056.883 ±  834.469  ops/s
JdkBenchmark.parseByInputStream            thrpt   20  28384.036 ±  901.152  ops/s
```
