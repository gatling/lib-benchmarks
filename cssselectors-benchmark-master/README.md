# Css selectors microbenchmark for the JVM

## Use case

Input is byte arrays.

If the parser can't guess encoding by itself, we try to decode in the most efficient possible way, but encoding time is accounted for.

## tl;dr


## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 2 -t 8`

## Figures

Here are the results on my machine:

* OS X 10.9
* Hotspot 1.7.0_60-ea
* Intel Core i7 2,7 GHz

Against Jodd 3.5.2

Benchmark                                          Mode   Samples        Score  Score error    Units
JoddBenchmark.parseCharsPrecompiledRoundRobin     thrpt        20     4904,432      266,984    ops/s
JoddBenchmark.parseStringPrecompiledRoundRobin    thrpt        20     4844,750      154,117    ops/s
JsoupBenchmark.parseStreamRoundRobin              thrpt        20     3009,824      239,242    ops/s
JsoupBenchmark.parseStringRoundRobin              thrpt        20     3127,410      261,808    ops/s

Against Jodd 3.5.3-SNAPSHOT

Benchmark                                          Mode   Samples        Score  Score error    Units
JoddBenchmark.parseCharsPrecompiledRoundRobin     thrpt        20     6428,508      312,095    ops/s
JoddBenchmark.parseStringPrecompiledRoundRobin    thrpt        20     6317,515      174,063    ops/s
JsoupBenchmark.parseStreamRoundRobin              thrpt        20     3035,378      382,555    ops/s
JsoupBenchmark.parseStringRoundRobin              thrpt        20     2973,758      394,045    ops/s
