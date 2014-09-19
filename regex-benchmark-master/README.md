# JVM Regex input type microbenchmark

## Use case

Input is byte arrays. Benchamrk accounts for byte decoding too.

## tl;dr

It's much more efficient to provide a CharBuffer than a String.

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 2 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.9
* Hotspot 1.7.0_45
* Intel Core i7 2,7 GHz

Benchmark                                Mode  Samples     Score  Score error  Units
RegexBenchmark.parseFastCharSequence    thrpt       20  3338,679      242,787  ops/s
RegexBenchmark.parseString              thrpt       20  3129,795      240,864  ops/s
RegexBenchmark.parseCharBuffer          thrpt       20  2949,361      184,103  ops/s
RegexBenchmark.parseScanner             thrpt       20  1188,431       29,098  ops/s
