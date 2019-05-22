# JsonPath implementations microbenchmark for the JVM

## Goal

Compare performance of Gatling and Jayway's JsonPath implementations, combined with different parsers.

We're really comparing raw performance with the most common use case of US_ASCII payloads.
Comparing features and support for less common encodings is out fo scope.

Tested parsers are:
* Jackson
* Jodd

## Test Case

JSON payloads are split into 1.500 byte (1 MTU) arrays to emulate wire behavior.

The test mixes different payload sizes and paths.

We test different strategies (when supported by the JSON parser):
* directly parsing a stream,
* decoding bytes into a String.

JsonPath paths are precompiled as both Gatling (in Gatling itself, no in the JsonPath impl) and Jayway caches them.

## tl;dr

* Gatling is similar to Jayway, except for a few use cases (filters, by index array access) where Gatling performs better
* Jodd and Jackson have similar performance, except for large payloads where Jackson outperform
* JDK8 performs better than JDK11. Reason is to be determined. GC, String implementation?

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

// dominated by JSON parsing, Jackson better than Jodd, most likely because of String allocation
CtimBenchmark.jayway_jackson_stream               $.performances[?(@.eventId == 339420805)]  thrpt   10     456.196 ±    11.010  ops/s
CtimBenchmark.gatling_jackson_stream              $.performances[?(@.eventId == 339420805)]  thrpt   10     442.928 ±    13.556  ops/s
CtimBenchmark.gatling_jodd_string                 $.performances[?(@.eventId == 339420805)]  thrpt   10     361.969 ±    10.836  ops/s

// similar results
GoessnerBenchmark.gatling_jodd_string                                $.store.book[2].author  thrpt   10  507233.068 ± 10734.205  ops/s
GoessnerBenchmark.gatling_jackson_stream                             $.store.book[2].author  thrpt   10  474999.209 ± 19932.169  ops/s
GoessnerBenchmark.jayway_jackson_stream                              $.store.book[2].author  thrpt   10  468828.861 ± 24527.430  ops/s

// similar results
GoessnerBenchmark.gatling_jackson_stream                                          $..author  thrpt   10  300601.360 ±  4678.124  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $..author  thrpt   10  293218.541 ±  9414.386  ops/s
GoessnerBenchmark.gatling_jodd_string                                             $..author  thrpt   10  286532.078 ± 23214.373  ops/s

// similar results
GoessnerBenchmark.gatling_jackson_stream                                          $.store.*  thrpt   10  498768.244 ± 14916.307  ops/s
GoessnerBenchmark.gatling_jodd_string                                             $.store.*  thrpt   10  486626.899 ± 53008.570  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $.store.*  thrpt   10  446481.115 ± 20206.546  ops/s

// gatling 33% better
GoessnerBenchmark.gatling_jodd_string                                      $..book[2].title  thrpt   10  420631.446 ± 15749.064  ops/s
GoessnerBenchmark.gatling_jackson_stream                                   $..book[2].title  thrpt   10  396802.575 ±  3897.879  ops/s
GoessnerBenchmark.jayway_jackson_stream                                    $..book[2].title  thrpt   10  297525.773 ±  7954.900  ops/s

// similar results
GoessnerBenchmark.jayway_jackson_stream                                                $..*  thrpt   10  201647.100 ±  8454.091  ops/s
GoessnerBenchmark.gatling_jodd_string                                                  $..*  thrpt   10  184232.727 ±  3593.640  ops/s
GoessnerBenchmark.gatling_jackson_stream                                               $..*  thrpt   10  176329.219 ±  6910.913  ops/s

// gatling 140% better
GoessnerBenchmark.gatling_jackson_stream  $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  427094.293 ±  9074.752  ops/s
GoessnerBenchmark.gatling_jodd_string     $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  438094.537 ± 24028.652  ops/s
GoessnerBenchmark.jayway_jackson_stream   $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  140390.669 ± 14040.174  ops/s

// similar results
TwentyKBenchmark.gatling_jackson_stream                                    $..friends..name  thrpt   10   12553.096 ±   821.777  ops/s
TwentyKBenchmark.gatling_jodd_string                                       $..friends..name  thrpt   10   11994.616 ±    74.352  ops/s
TwentyKBenchmark.jayway_jackson_stream                                     $..friends..name  thrpt   10   11003.782 ±   191.819  ops/s

// similar results
TwitterBenchmark.jayway_jackson_stream                                       $.completed_in  thrpt   10   47820.221 ±   164.021  ops/s
TwitterBenchmark.gatling_jackson_stream                                      $.completed_in  thrpt   10   47295.387 ±   162.081  ops/s
TwitterBenchmark.gatling_jodd_string                                         $.completed_in  thrpt   10   45620.967 ±   172.273  ops/s

// similar results
WebXmlBenchmark.gatling_jodd_string           $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  184221,011 ±  6620,294  ops/s
WebXmlBenchmark.gatling_jackson_stream        $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  154668,254 ± 12824,994  ops/s
WebXmlBenchmark.jayway_jackson_stream         $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  140516,770 ±  3131,067  ops/s

// similar results
WebXmlBenchmark.gatling_jackson_stream        $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  183678.266 ±   386.914  ops/s
WebXmlBenchmark.jayway_jackson_stream         $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  183539.463 ±  1136.671  ops/s
WebXmlBenchmark.gatling_jodd_string           $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  168639.098 ±   383.430  ops/s
```

* Hotspot 11.0.3+7

Results are worse than with JDK8 (GC?, String allocation?).

```
Benchmark                                                                            (path)   Mode  Cnt       Score        Error  Units
// dominated by JSON parsing, Jackson better than Jodd, most likely because of String allocation
CtimBenchmark.gatling_jackson_stream              $.performances[?(@.eventId == 339420805)]  thrpt    5     394.585 ±    20.908  ops/s
CtimBenchmark.jayway_jackson_stream               $.performances[?(@.eventId == 339420805)]  thrpt    5     378.211 ±    64.963  ops/s
CtimBenchmark.gatling_jodd_string                 $.performances[?(@.eventId == 339420805)]  thrpt    5     294.379 ±    17.183  ops/s

// similar results
GoessnerBenchmark.gatling_jodd_string                                $.store.book[2].author  thrpt    5  451985.954 ± 36525.462  ops/s
GoessnerBenchmark.gatling_jackson_stream                             $.store.book[2].author  thrpt    5  423529.687 ± 32993.764  ops/s
GoessnerBenchmark.jayway_jackson_stream                              $.store.book[2].author  thrpt    5  413804.399 ± 42874.608  ops/s

// similar results
GoessnerBenchmark.gatling_jackson_stream                                          $..author  thrpt    5  283342.852 ±  2257.061  ops/s
GoessnerBenchmark.gatling_jodd_string                                             $..author  thrpt    5  270790.628 ±  7861.329  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $..author  thrpt    5  264673.810 ± 25364.156  ops/s

// similar results
GoessnerBenchmark.gatling_jackson_stream                                          $.store.*  thrpt    5  467237.581 ±  3035.177  ops/s
GoessnerBenchmark.gatling_jodd_string                                             $.store.*  thrpt    5  445651.094 ± 12176.139  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $.store.*  thrpt    5  435290.973 ± 15531.466  ops/s

// gatling 33% better
GoessnerBenchmark.gatling_jackson_stream                                   $..book[2].title  thrpt    5  375572.015 ±  1219.925  ops/s
GoessnerBenchmark.gatling_jodd_string                                      $..book[2].title  thrpt    5  363898.090 ± 34612.622  ops/s
GoessnerBenchmark.jayway_jackson_stream                                    $..book[2].title  thrpt    5  267835.646 ± 14383.985  ops/s

// similar results
GoessnerBenchmark.jayway_jackson_stream                                                $..*  thrpt    5  190686.796 ± 12785.271  ops/s
GoessnerBenchmark.gatling_jackson_stream                                               $..*  thrpt    5  174167.576 ±  1526.501  ops/s
GoessnerBenchmark.gatling_jodd_string                                                  $..*  thrpt    5  162306.865 ± 11455.377  ops/s

// gatling 140% better
GoessnerBenchmark.gatling_jackson_stream  $.store.book[?(@.price < 10 && @.price >4)].title  thrpt    5  400370.009 ±  2152.909  ops/s
GoessnerBenchmark.gatling_jodd_string     $.store.book[?(@.price < 10 && @.price >4)].title  thrpt    5  389878.450 ± 11818.187  ops/s
GoessnerBenchmark.jayway_jackson_stream   $.store.book[?(@.price < 10 && @.price >4)].title  thrpt    5  108962.332 ±  3265.105  ops/s

// similar results
TwentyKBenchmark.gatling_jackson_stream                                    $..friends..name  thrpt    5   11790.663 ±   363.460  ops/s
TwentyKBenchmark.gatling_jodd_string                                       $..friends..name  thrpt    5   10806.656 ±   409.855  ops/s
TwentyKBenchmark.jayway_jackson_stream                                     $..friends..name  thrpt    5   10374.586 ±   271.009  ops/s

// similar results
TwitterBenchmark.gatling_jackson_stream                                      $.completed_in  thrpt    5   46165.206 ±  1561.599  ops/s
TwitterBenchmark.jayway_jackson_stream                                       $.completed_in  thrpt    5   44966.061 ±  1597.176  ops/s
TwitterBenchmark.gatling_jodd_string                                         $.completed_in  thrpt    5   37094.975 ±  1348.457  ops/s

// similar results
WebXmlBenchmark.gatling_jackson_stream        $.web-app.servlet[0].init-param.dataStoreName  thrpt    5  164415.122 ±  2499.406  ops/s
WebXmlBenchmark.jayway_jackson_stream         $.web-app.servlet[0].init-param.dataStoreName  thrpt    5  159165.807 ± 12312.677  ops/s
WebXmlBenchmark.gatling_jodd_string           $.web-app.servlet[0].init-param.dataStoreName  thrpt    5  145358.353 ±  8140.955  ops/s

```
