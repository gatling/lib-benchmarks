# JVM Regex input type microbenchmark

## Use case

Input is byte arrays. Benchamrk accounts for byte decoding too.

## tl;dr

It's much more efficient to provide a CharBuffer than a String.

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 5 -f 1 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.9
* Hotspot 1.7.0_45
* Intel Core i7 2,7 GHz

Benchmark                                        Mode  Samples     Score  Score error  Units
RegexBenchmark.parseCharBuffer          thrpt       20  5555,027      194,975  ops/s
RegexBenchmark.parseFastCharSequence    thrpt       20  4306,553      271,581  ops/s
RegexBenchmark.parseString              thrpt       20  4258,894      282,915  ops/s