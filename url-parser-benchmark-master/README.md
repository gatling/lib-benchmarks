# URI vs URL vs forked URL microbenchmark

## Use case

URI implements RFC2616, so it's very strict, for example regarding encoding query characters.

This benchmark investigate alternatives:

* `java.net.URL`
* forked `java.net.URL` without the connection/download features

## tl;dr

The forked `java.net.URL` implementation is 3 times faster.

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 5 -f 1 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.9
* Hotspot 1.7.0_60
* Intel Core i7 2,7 GHz

Benchmark                             Mode   Samples         Mean   Mean error    Units
UrlParserBenchmark.parseSimpleURL    thrpt         5    15895,810      476,049   ops/ms
UrlParserBenchmark.parseURI          thrpt         5     5411,562      937,267   ops/ms
UrlParserBenchmark.parseURL          thrpt         5     4928,183     1650,725   ops/ms