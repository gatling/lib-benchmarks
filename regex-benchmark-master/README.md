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
Re2jBenchmark.parseCharBuffer         thrpt   20  3789,525 ± 267,544  ops/s
Re2jBenchmark.parseString             thrpt   20  3436,392 ± 352,982  ops/s
RegexBenchmark.parseFastCharSequence  thrpt   20  3301,876 ± 278,736  ops/s
Re2jBenchmark.parseFastCharSequence   thrpt   20  3268,785 ± 353,488  ops/s
RegexBenchmark.parseString            thrpt   20  3088,014 ± 406,660  ops/s
RegexBenchmark.parseCharBuffer        thrpt   20  3029,533 ± 240,580  ops/s
RegexBenchmark.parseScanner           thrpt   20  1144,058 ± 101,752  ops/s
