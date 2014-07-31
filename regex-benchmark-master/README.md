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

Benchmark                                Mode Thr     Count  Sec         Mean   Mean error    Units
RegexBenchmark.parseCharBuffer          thrpt  16        20    1     5188,818      495,537    ops/s
RegexBenchmark.parseFastCharSequence    thrpt  16        20    1     3890,151      393,322    ops/s
RegexBenchmark.parseString              thrpt  16        20    1     3819,451      405,928    ops/s