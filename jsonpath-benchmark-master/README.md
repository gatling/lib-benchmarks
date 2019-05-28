# JsonPath implementations microbenchmark for the JVM

## Goal

Compare performance of Gatling and Jayway's JsonPath implementations.

We're really comparing raw performance with the most common use case of US_ASCII payloads.
Comparing features and support for less common encodings is out fo scope.

## Test Case

JSON payloads are split into 1.500 byte (1 MTU) arrays to emulate wire behavior.

The test mixes different payload sizes and paths:

* CtimBenchmark:     1.8MB
* GoessnerBenchmark: 789B
* TwentyKBenchmark:  32KB
* TwitterBenchmark:  13KB
* WebXmlBenchmark:   3KB

JsonPath paths are precompiled as both Gatling (in Gatling itself, no in the JsonPath impl) and Jayway caches them.

## Conclusions

* Gatling is faster than Jayway for recursive scans (`..`) and filters (`[?(@...)]`).
* Performance is similar for direct access.
* JDK8 performs better than JDK11. Reason is to be determined (GC?)
* For large files, performance is dominated by JSON parsing

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 1 -t 2`

## Results

On my machine:

* OS X 10.13.6
* Intel Core i7 2,6 GHz

## Mixed samples

* Hotspot 1.8.0_202

```
Benchmark                                                                            (path)   Mode  Cnt       Score       Error  Units

// gatling 13% faster
CtimBenchmark.gatling_jackson_stream              $.performances[?(@.eventId == 339420805)]  thrpt    5     505.189 ±    13.639  ops/s
CtimBenchmark.jayway_jackson_stream               $.performances[?(@.eventId == 339420805)]  thrpt    5     448.663 ±    40.487  ops/s

// gatling 10% faster
GoessnerBenchmark.gatling_jackson_stream                             $.store.book[2].author  thrpt    5  527989.375 ± 64851.723  ops/s
GoessnerBenchmark.jayway_jackson_stream                              $.store.book[2].author  thrpt    5  480686.629 ± 11402.935  ops/s

// gatling 61% faster
GoessnerBenchmark.gatling_jackson_stream                                          $..author  thrpt    5  470528.810 ± 30583.940  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $..author  thrpt    5  292025.739 ±  8777.116  ops/s

// gatling 15% faster
GoessnerBenchmark.gatling_jackson_stream                                          $.store.*  thrpt    5  562436.563 ±  2674.118  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $.store.*  thrpt    5  488381.965 ± 29422.985  ops/s

// gatling 73% faster
GoessnerBenchmark.gatling_jackson_stream                                   $..book[2].title  thrpt    5  520517.015 ±  2119.312  ops/s
GoessnerBenchmark.jayway_jackson_stream                                    $..book[2].title  thrpt    5  300604.897 ± 11991.236  ops/s

// gatling 75% faster
GoessnerBenchmark.gatling_jackson_stream                                               $..*  thrpt    5  351163.503 ± 18013.764  ops/s
GoessnerBenchmark.jayway_jackson_stream                                                $..*  thrpt    5  200535.991 ± 18139.288  ops/s

// gatling 247% faster
GoessnerBenchmark.gatling_jackson_stream  $.store.book[?(@.price < 10 && @.price >4)].title  thrpt    5  491351.481 ±  8999.909  ops/s
GoessnerBenchmark.jayway_jackson_stream   $.store.book[?(@.price < 10 && @.price >4)].title  thrpt    5  141764.432 ±  1693.995  ops/s

// gatling 76% faster
TwentyKBenchmark.gatling_jackson_stream                                    $..friends..name  thrpt    5   19563.575 ±   712.547  ops/s
TwentyKBenchmark.jayway_jackson_stream                                     $..friends..name  thrpt    5   11104.586 ±   347.686  ops/s

// similar
TwitterBenchmark.gatling_jackson_stream                                      $.completed_in  thrpt    5   49433.009 ±  1565.856  ops/s
TwitterBenchmark.jayway_jackson_stream                                       $.completed_in  thrpt    5   46316.120 ±  2311.086  ops/s

// similar
WebXmlBenchmark.gatling_jackson_stream        $.web-app.servlet[0].init-param.dataStoreName  thrpt    5  199057.054 ±  3709.752  ops/s
WebXmlBenchmark.jayway_jackson_stream         $.web-app.servlet[0].init-param.dataStoreName  thrpt    5  184834.879 ±  1459.202  ops/s
```

* Hotspot 11.0.3+7

Results are worse than with JDK8 (GC?).

```
Benchmark                                                                            (path)   Mode  Cnt       Score        Error  Units
// similar
CtimBenchmark.gatling_jackson_stream              $.performances[?(@.eventId == 339420805)]  thrpt    5     393.004 ±    108.099  ops/s
CtimBenchmark.jayway_jackson_stream               $.performances[?(@.eventId == 339420805)]  thrpt    5     393.141 ±     50.457  ops/s

// similar
GoessnerBenchmark.gatling_jackson_stream                             $.store.book[2].author  thrpt    5  410119.724 ±  42382.168  ops/s
GoessnerBenchmark.jayway_jackson_stream                              $.store.book[2].author  thrpt    5  425648.780 ±  83228.727  ops/s

// gatling 38% faster
GoessnerBenchmark.gatling_jackson_stream                                          $..author  thrpt    5  352659.752 ±  86434.862  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $..author  thrpt    5  255144.176 ±  21782.976  ops/s

// similar
GoessnerBenchmark.gatling_jackson_stream                                          $.store.*  thrpt    5  425947.861 ± 101067.502  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $.store.*  thrpt    5  447531.452 ±   8386.430  ops/s

// gatling 56% faster
GoessnerBenchmark.gatling_jackson_stream                                   $..book[2].title  thrpt    5  418912.651 ±  15248.427  ops/s
GoessnerBenchmark.jayway_jackson_stream                                    $..book[2].title  thrpt    5  268991.864 ±  11250.162  ops/s

// gatling 47% faster
GoessnerBenchmark.gatling_jackson_stream                                               $..*  thrpt    5  276912.547 ±  53225.778  ops/s
GoessnerBenchmark.jayway_jackson_stream                                                $..*  thrpt    5  188141.032 ±   9023.257  ops/s

// gatling 271% faster
GoessnerBenchmark.gatling_jackson_stream  $.store.book[?(@.price < 10 && @.price >4)].title  thrpt    5  409971.623 ±   7201.892  ops/s
GoessnerBenchmark.jayway_jackson_stream   $.store.book[?(@.price < 10 && @.price >4)].title  thrpt    5  110517.296 ±   5875.567  ops/s

// gatling 60% faster
TwentyKBenchmark.gatling_jackson_stream                                    $..friends..name  thrpt    5   16553.518 ±    390.952  ops/s
TwentyKBenchmark.jayway_jackson_stream                                     $..friends..name  thrpt    5   10337.847 ±    113.349  ops/s

// similar
TwitterBenchmark.gatling_jackson_stream                                      $.completed_in  thrpt    5   42697.268 ±  13303.019  ops/s
TwitterBenchmark.jayway_jackson_stream                                       $.completed_in  thrpt    5   43068.142 ±   3925.761  ops/s

// similar
WebXmlBenchmark.gatling_jackson_stream        $.web-app.servlet[0].init-param.dataStoreName  thrpt    5  155589.971 ±   8917.118  ops/s
WebXmlBenchmark.jayway_jackson_stream         $.web-app.servlet[0].init-param.dataStoreName  thrpt    5  158214.118 ±  21314.872  ops/s

```
