random-generators-benchmark
===========================

How to
------

Run with `mvn clean package` then `java -jar target/microbenchmarks.jar ".*ThreadLocalTest.*" -wi 5 -i 5 -f 1 -t 5`

```
Results
-------

Benchmark                                                                                        Mode   Samples         Mean   Mean error    Units
i.g.generatorsBenchmark.ExponentialThreadLocalTest.commonsMath                                  thrpt         5    55698,806     5389,257   ops/ms
i.g.generatorsBenchmark.ExponentialThreadLocalTest.uncommonsMath_CellularAutomatonRNG           thrpt         5    57949,644     4277,660   ops/ms
i.g.generatorsBenchmark.ExponentialThreadLocalTest.uncommonsMath_MersenneTwisterRNG             thrpt         5    63113,399     5213,445   ops/ms
i.g.generatorsBenchmark.ExponentialThreadLocalTest.unsafe_uncommonsMath_CellularAutomatonRNG    thrpt         5    94183,073     3550,085   ops/ms
i.g.generatorsBenchmark.ExponentialThreadLocalTest.unsafe_uncommonsMath_MersenneTwisterRNG      thrpt         5   101205,440    12049,582   ops/ms

i.g.generatorsBenchmark.PoissonThreadLocalTest.commonsMath                                      thrpt         5    13388,083     1256,455   ops/ms
i.g.generatorsBenchmark.PoissonThreadLocalTest.uncommonsMath_CellularAutomatonRNG               thrpt         5    50807,071      585,568   ops/ms
i.g.generatorsBenchmark.PoissonThreadLocalTest.uncommonsMath_MersenneTwisterRNG                 thrpt         5    44526,815      556,187   ops/ms
i.g.generatorsBenchmark.PoissonThreadLocalTest.unsafe_uncommonsMath_CellularAutomatonRNG        thrpt         5    79538,612     2308,965   ops/ms
i.g.generatorsBenchmark.PoissonThreadLocalTest.unsafe_uncommonsMath_MersenneTwisterRNG          thrpt         5    75613,509     2372,291   ops/ms
```