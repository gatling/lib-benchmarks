# JVM Regex input type microbenchmark

## Use case

Input is byte arrays. Benchamrk accounts for byte decoding too.

## tl;dr

It's much more efficient to provide a CharBuffer than a String.

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 5 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.9
* Hotspot 1.8.0_40
* Intel Core i7 2,7 GHz

Benchmark                              Mode  Cnt     Score    Error  Units
Re2jBenchmark.parseCharBuffer         thrpt   50  4130,729 ± 87,706  ops/s
Re2jBenchmark.parseFastCharSequence   thrpt   50  3876,769 ± 40,541  ops/s
RegexBenchmark.parseString            thrpt   50  3850,166 ± 31,308  ops/s
RegexBenchmark.parseFastCharSequence  thrpt   50  3846,621 ± 34,504  ops/s
Re2jBenchmark.parseString             thrpt   50  3801,638 ± 69,664  ops/s
RegexBenchmark.parseCharBuffer        thrpt   50  3413,565 ± 31,918  ops/s
RegexBenchmark.parseScanner           thrpt   50  1248,149 ± 16,308  ops/s
