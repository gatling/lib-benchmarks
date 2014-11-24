random-generators-benchmark
===========================

How to
------

Run with `mvn clean package` then `java -jar target/microbenchmarks.jar ".*ThreadLocalTest.*" -wi 5 -i 5 -f 1 -t 5`

```
Results
-------

Benchmark                                                                                        Mode   Samples         Mean   Mean error    Units
i.g.generatorsBenchmark.ExponentialThreadLocalTest.unsafe_uncommonsMath_XORShiftRNG             thrpt         5   114192,722    13557,747   ops/ms
i.g.generatorsBenchmark.ExponentialThreadLocalTest.unsafe_uncommonsMath_MersenneTwisterRNG      thrpt         5   103089,986    15945,502   ops/ms
i.g.generatorsBenchmark.ExponentialThreadLocalTest.unsafe_uncommonsMath_CellularAutomatonRNG    thrpt         5    90144,821     9747,392   ops/ms
i.g.generatorsBenchmark.ExponentialThreadLocalTest.commonsMath                                  thrpt         5    67667,008     4153,914   ops/ms
i.g.generatorsBenchmark.ExponentialThreadLocalTest.uncommonsMath_CellularAutomatonRNG           thrpt         5    53509,963     4550,235   ops/ms
i.g.generatorsBenchmark.ExponentialThreadLocalTest.uncommonsMath_MersenneTwisterRNG             thrpt         5    51133,115     4134,377   ops/ms

i.g.generatorsBenchmark.PoissonThreadLocalTest.commonsMath                                      thrpt         5    13041,530      902,941   ops/ms
i.g.generatorsBenchmark.PoissonThreadLocalTest.uncommonsMath_CellularAutomatonRNG               thrpt         5    54846,435     2411,245   ops/ms
i.g.generatorsBenchmark.PoissonThreadLocalTest.uncommonsMath_MersenneTwisterRNG                 thrpt         5    44330,968     1398,101   ops/ms
i.g.generatorsBenchmark.PoissonThreadLocalTest.unsafe_uncommonsMath_CellularAutomatonRNG        thrpt         5    85469,173     7040,945   ops/ms
i.g.generatorsBenchmark.PoissonThreadLocalTest.unsafe_uncommonsMath_MersenneTwisterRNG          thrpt         5    81212,661     9925,990   ops/ms
```