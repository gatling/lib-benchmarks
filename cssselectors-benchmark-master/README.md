# Css selectors microbenchmark for the JVM

## Use case

Input is byte arrays.

If the parser can't guess encoding by itself, we try to decode in the most efficient possible way, but encoding time is accounted for.

## tl;dr

Jodd is more that twice faster than Jsoup

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 2 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.9.5
* Hotspot 1.7.0_65
* Intel Core i7 2,7 GHz

Benchmark                                          Mode  Samples     Score     Error  Units
JoddBenchmark.parseCharsPrecompiledRoundRobin     thrpt       20  5578,533 ± 275,616  ops/s
JoddBenchmark.parseStringPrecompiledRoundRobin    thrpt       20  5311,143 ± 285,078  ops/s
JsoupBenchmark.parseStringRoundRobin              thrpt       20  2363,258 ± 462,125  ops/s
JsoupBenchmark.parseStreamRoundRobin              thrpt       20  2323,909 ± 577,873  ops/s
