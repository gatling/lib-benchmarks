# JsonPath implementations microbenchmark for the JVM

## Goal

Compare performance of Gatling and Jayway's JsonPath implementations, combined with different parsers.

We're really comparing raw performance with the most common use case of US_ASCII payloads.
Comparing features and support for less common encodings is out fo scope.

Tested parsers are:
* Jackson
* Boon
* Jodd
* Gson

## Test Case

JSON payloads are split into 1.500 byte (1 MTU) arrays to emulate wire behavior.

The test mixes different payload sizes and paths.

We test different strategies (when supported by the JSON parser):
* directly parsing a stream,
* merging the bytes into one single array,
* merging the bytes into a String.

JsonPath paths are precompiled as both Gatling (in Gatling itself, no in the JsonPath impl) and Jayway caches them.

## tl;dr

* Gatling is almost 2x faster than Jayway
* Jodd is faster than Jackson for reasonable payload sizes. For very large payloads, Jackson streaming parsing wins.
* String char stealing is a win on Java 8
* Results displayed on Jayway's website are completely flawed, as they only have path caching enabled for their side, not for Gatling.
* Gson falls hehind in terms of performance

## How to

Build with `mvn clean package`

Run with `java -jar target/microbenchmarks.jar ".*" -wi 2 -i 10 -f 1 -t 2`

## Results

On my machine:

* OS X 10.11.6
* Intel Core i7 2,7 GHz

## Mixed samples (all but ctim)

* Hotspot 1.8.0_152

```
Benchmark                                                                                (path)   Mode  Cnt       Score       Error  Units
CtimBenchmark.gatling_jackson_stream                  $.performances[?(@.eventId == 339420805)]  thrpt   10     361,510 ±    29,929  ops/s
CtimBenchmark.jayway_jackson_stream                   $.performances[?(@.eventId == 339420805)]  thrpt   10     359,247 ±    33,750  ops/s
CtimBenchmark.gatling_jodd_string                     $.performances[?(@.eventId == 339420805)]  thrpt   10     222,366 ±    10,306  ops/s
CtimBenchmark.gatling_jodd_chars                      $.performances[?(@.eventId == 339420805)]  thrpt   10     214,406 ±     9,591  ops/s

GoessnerBenchmark.gatling_jodd_chars                                     $.store.book[2].author  thrpt   10  691905,718 ± 49661,610  ops/s
GoessnerBenchmark.gatling_jodd_string                                    $.store.book[2].author  thrpt   10  666894,022 ± 53781,785  ops/s
GoessnerBenchmark.gatling_jackson_stream                                 $.store.book[2].author  thrpt   10  449706,238 ± 26624,610  ops/s
GoessnerBenchmark.jayway_jackson_stream                                  $.store.book[2].author  thrpt   10  339272,649 ± 27741,399  ops/s

GoessnerBenchmark.gatling_jodd_string                                                 $..author  thrpt   10  696179,183 ± 49083,305  ops/s
GoessnerBenchmark.gatling_jodd_chars                                                  $..author  thrpt   10  689243,137 ± 29633,178  ops/s
GoessnerBenchmark.gatling_jackson_stream                                              $..author  thrpt   10  471002,259 ± 23313,378  ops/s
GoessnerBenchmark.jayway_jackson_stream                                               $..author  thrpt   10  224710,406 ± 33575,218  ops/s

GoessnerBenchmark.gatling_jodd_chars                                                  $.store.*  thrpt   10  716462,316 ± 41178,303  ops/s
GoessnerBenchmark.gatling_jodd_string                                                 $.store.*  thrpt   10  716788,719 ± 54414,965  ops/s
GoessnerBenchmark.gatling_jackson_stream                                              $.store.*  thrpt   10  464056,774 ± 24101,976  ops/s
GoessnerBenchmark.jayway_jackson_stream                                               $.store.*  thrpt   10  402515,912 ± 26253,349  ops/s

GoessnerBenchmark.gatling_jodd_chars                                           $..book[2].title  thrpt   10  725319,031 ± 34149,445  ops/s
GoessnerBenchmark.gatling_jodd_string                                          $..book[2].title  thrpt   10  754908,451 ± 28221,410  ops/s
GoessnerBenchmark.gatling_jackson_stream                                       $..book[2].title  thrpt   10  430898,682 ± 21003,529  ops/s
GoessnerBenchmark.jayway_jackson_stream                                        $..book[2].title  thrpt   10  243229,836 ± 16937,642  ops/s

GoessnerBenchmark.gatling_jodd_string                                                      $..*  thrpt   10  727748,549 ± 28693,801  ops/s
GoessnerBenchmark.gatling_jodd_chars                                                       $..*  thrpt   10  661918,534 ± 34739,219  ops/s
GoessnerBenchmark.gatling_jackson_stream                                                   $..*  thrpt   10  417010,968 ± 43359,052  ops/s
GoessnerBenchmark.jayway_jackson_stream                                                    $..*  thrpt   10  162307,152 ± 15474,768  ops/s

GoessnerBenchmark.gatling_jodd_string         $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  744802,424 ± 35575,593  ops/s
GoessnerBenchmark.gatling_jodd_chars          $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  672680,786 ± 26271,279  ops/s
GoessnerBenchmark.gatling_jackson_stream      $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  462328,177 ± 23672,630  ops/s
GoessnerBenchmark.jayway_jackson_stream       $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  111774,743 ±  8713,608  ops/s

TwentyKBenchmark.gatling_jodd_chars                                            $..friends..name  thrpt   10   31032,810 ±  1968,179  ops/s
TwentyKBenchmark.gatling_jodd_string                                           $..friends..name  thrpt   10   30893,496 ±  1543,162  ops/s
TwentyKBenchmark.gatling_jackson_stream                                        $..friends..name  thrpt   10   17208,600 ±   918,750  ops/s
TwentyKBenchmark.jayway_jackson_stream                                         $..friends..name  thrpt   10    9115,453 ±  1660,753  ops/s

TwitterBenchmark.gatling_jodd_string                                             $.completed_in  thrpt   10   61439,419 ±  2165,309  ops/s
TwitterBenchmark.gatling_jodd_chars                                              $.completed_in  thrpt   10   59675,834 ±  2578,713  ops/s
TwitterBenchmark.gatling_jackson_stream                                          $.completed_in  thrpt   10   40422,112 ±  2303,300  ops/s
TwitterBenchmark.jayway_jackson_stream                                           $.completed_in  thrpt   10   40033,987 ±  2697,736  ops/s

WebXmlBenchmark.gatling_jodd_chars                $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  191308,414 ± 10472,964  ops/s
WebXmlBenchmark.gatling_jodd_string               $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  183604,549 ±  7216,906  ops/s
WebXmlBenchmark.gatling_jackson_stream            $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  152383,865 ± 19208,084  ops/s
WebXmlBenchmark.jayway_jackson_stream             $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  145736,682 ±  7073,220  ops/s
```

* Hotspot 9.0.4

```
Benchmark                                                                            (path)   Mode  Cnt       Score        Error  Units
CtimBenchmark.gatling_jackson_stream              $.performances[?(@.eventId == 339420805)]  thrpt   10     354,024 ±     45,399  ops/s
CtimBenchmark.jayway_jackson_stream               $.performances[?(@.eventId == 339420805)]  thrpt   10     333,946 ±     30,491  ops/s
CtimBenchmark.gatling_jodd_string                 $.performances[?(@.eventId == 339420805)]  thrpt   10     216,525 ±     11,421  ops/s
CtimBenchmark.gatling_jodd_chars                  $.performances[?(@.eventId == 339420805)]  thrpt   10     203,213 ±     15,297  ops/s

GoessnerBenchmark.gatling_jodd_chars                                 $.store.book[2].author  thrpt   10  733983,707 ±  34890,387  ops/s
GoessnerBenchmark.gatling_jodd_string                                $.store.book[2].author  thrpt   10  585357,810 ±  41109,172  ops/s
GoessnerBenchmark.gatling_jackson_stream                             $.store.book[2].author  thrpt   10  423378,362 ±  31952,443  ops/s
GoessnerBenchmark.jayway_jackson_stream                              $.store.book[2].author  thrpt   10  339037,930 ±  24272,879  ops/s

GoessnerBenchmark.gatling_jodd_chars                                              $..author  thrpt   10  742067,672 ±  42413,531  ops/s
GoessnerBenchmark.gatling_jodd_string                                             $..author  thrpt   10  701407,270 ± 119973,030  ops/s
GoessnerBenchmark.gatling_jackson_stream                                          $..author  thrpt   10  465044,548 ±  28104,691  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $..author  thrpt   10  194745,470 ±  37392,967  ops/s

GoessnerBenchmark.gatling_jodd_chars                                              $.store.*  thrpt   10  647489,585 ±  28723,090  ops/s
GoessnerBenchmark.gatling_jodd_string                                             $.store.*  thrpt   10  583138,684 ±  81132,672  ops/s
GoessnerBenchmark.gatling_jackson_stream                                          $.store.*  thrpt   10  478474,600 ±  33401,201  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $.store.*  thrpt   10  330517,643 ±  50588,397  ops/s

GoessnerBenchmark.gatling_jodd_chars                                       $..book[2].title  thrpt   10  656053,232 ±  28619,392  ops/s
GoessnerBenchmark.gatling_jodd_string                                      $..book[2].title  thrpt   10  653454,385 ±  34945,319  ops/s
GoessnerBenchmark.gatling_jackson_stream                                   $..book[2].title  thrpt   10  469397,021 ±  26089,789  ops/s
GoessnerBenchmark.jayway_jackson_stream                                    $..book[2].title  thrpt   10  220573,595 ±  26888,843  ops/s

GoessnerBenchmark.gatling_jodd_chars                                                   $..*  thrpt   10  640054,009 ± 105507,051  ops/s
GoessnerBenchmark.gatling_jodd_string                                                  $..*  thrpt   10  631110,716 ± 112846,223  ops/s
GoessnerBenchmark.gatling_jackson_stream                                               $..*  thrpt   10  466027,314 ±  26735,561  ops/s
GoessnerBenchmark.jayway_jackson_stream                                                $..*  thrpt   10  156547,804 ±  18208,922  ops/s

GoessnerBenchmark.gatling_jodd_chars      $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  609236,879 ±  46209,801  ops/s
GoessnerBenchmark.gatling_jodd_string     $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  628940,130 ± 127008,231  ops/s
GoessnerBenchmark.gatling_jackson_stream  $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  443634,332 ±  24294,973  ops/s
GoessnerBenchmark.jayway_jackson_stream   $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10   98398,898 ±   8078,545  ops/s

TwentyKBenchmark.gatling_jodd_chars                                        $..friends..name  thrpt   10   29614,591 ±   3263,716  ops/s
TwentyKBenchmark.gatling_jodd_string                                       $..friends..name  thrpt   10   27965,930 ±   3587,219  ops/s
TwentyKBenchmark.gatling_jackson_stream                                    $..friends..name  thrpt   10   17605,663 ±    908,210  ops/s
TwentyKBenchmark.jayway_jackson_stream                                     $..friends..name  thrpt   10    9098,773 ±   1655,304  ops/s

TwitterBenchmark.gatling_jodd_chars                                          $.completed_in  thrpt   10   61585,019 ±   2384,782  ops/s
TwitterBenchmark.gatling_jodd_string                                         $.completed_in  thrpt   10   58037,131 ±   2319,104  ops/s
TwitterBenchmark.gatling_jackson_stream                                      $.completed_in  thrpt   10   41711,413 ±   2818,801  ops/s
TwitterBenchmark.jayway_jackson_stream                                       $.completed_in  thrpt   10   38604,361 ±   2546,624  ops/s

WebXmlBenchmark.gatling_jodd_chars            $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  195069,360 ±   8260,588  ops/s
WebXmlBenchmark.gatling_jodd_string           $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  174453,063 ±   6506,634  ops/s
WebXmlBenchmark.gatling_jackson_stream        $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  159509,621 ±  10473,019  ops/s
WebXmlBenchmark.jayway_jackson_stream         $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  151407,670 ±   7925,366  ops/s
```
