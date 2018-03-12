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
Benchmark                                                                            (path)   Mode  Cnt       Score       Error  Units
CtimBenchmark.gatling_jackson_stream              $.performances[?(@.eventId == 339420805)]  thrpt   10     345,081 ±    27,478  ops/s
CtimBenchmark.jayway_jackson_stream               $.performances[?(@.eventId == 339420805)]  thrpt   10     329,632 ±    28,370  ops/s
CtimBenchmark.gatling_jodd_chars                  $.performances[?(@.eventId == 339420805)]  thrpt   10     198,416 ±    11,017  ops/s
CtimBenchmark.gatling_jodd_string                 $.performances[?(@.eventId == 339420805)]  thrpt   10     196,365 ±    27,333  ops/s

GoessnerBenchmark.gatling_jodd_string                                $.store.book[2].author  thrpt   10  701588,472 ± 35203,835  ops/s
GoessnerBenchmark.gatling_jodd_chars                                 $.store.book[2].author  thrpt   10  689193,367 ± 35837,252  ops/s
GoessnerBenchmark.gatling_jackson_stream                             $.store.book[2].author  thrpt   10  414459,557 ± 27699,858  ops/s
GoessnerBenchmark.jayway_jackson_stream                              $.store.book[2].author  thrpt   10  373234,955 ± 23291,245  ops/s

GoessnerBenchmark.gatling_jodd_string                                             $..author  thrpt   10  702129,792 ± 62996,119  ops/s
GoessnerBenchmark.gatling_jodd_chars                                              $..author  thrpt   10  687225,855 ± 27172,887  ops/s
GoessnerBenchmark.gatling_jackson_stream                                          $..author  thrpt   10  422431,440 ± 38796,446  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $..author  thrpt   10  231165,919 ±  6054,463  ops/s

GoessnerBenchmark.gatling_jodd_string                                             $.store.*  thrpt   10  716982,627 ± 38710,619  ops/s
GoessnerBenchmark.gatling_jodd_chars                                              $.store.*  thrpt   10  650479,719 ± 34429,087  ops/s
GoessnerBenchmark.gatling_jackson_stream                                          $.store.*  thrpt   10  408558,071 ± 52775,108  ops/s
GoessnerBenchmark.jayway_jackson_stream                                           $.store.*  thrpt   10  369859,072 ± 18143,601  ops/s

GoessnerBenchmark.gatling_jodd_string                                      $..book[2].title  thrpt   10  729861,433 ± 34334,819  ops/s
GoessnerBenchmark.gatling_jodd_chars                                       $..book[2].title  thrpt   10  627590,370 ± 80378,140  ops/s
GoessnerBenchmark.gatling_jackson_stream                                   $..book[2].title  thrpt   10  413286,684 ± 30658,120  ops/s
GoessnerBenchmark.jayway_jackson_stream                                    $..book[2].title  thrpt   10  229386,620 ± 15570,646  ops/s

GoessnerBenchmark.gatling_jodd_string                                                  $..*  thrpt   10  725164,438 ± 22375,134  ops/s
GoessnerBenchmark.gatling_jodd_chars                                                   $..*  thrpt   10  676060,873 ± 57266,729  ops/s
GoessnerBenchmark.gatling_jackson_stream                                               $..*  thrpt   10  450203,361 ± 29362,504  ops/s
GoessnerBenchmark.jayway_jackson_stream                                                $..*  thrpt   10  165373,891 ±  7263,248  ops/s

GoessnerBenchmark.gatling_jodd_string     $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  696719,905 ± 26066,859  ops/s
GoessnerBenchmark.gatling_jodd_chars      $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  691843,563 ± 43904,569  ops/s
GoessnerBenchmark.gatling_jackson_stream  $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  429400,176 ± 48214,415  ops/s
GoessnerBenchmark.jayway_jackson_stream   $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10   94540,783 ±  4865,509  ops/s

TwentyKBenchmark.gatling_jodd_string                                       $..friends..name  thrpt   10   29250,741 ±  1509,612  ops/s
TwentyKBenchmark.gatling_jodd_chars                                        $..friends..name  thrpt   10   28960,471 ±  1677,351  ops/s
TwentyKBenchmark.gatling_jackson_stream                                    $..friends..name  thrpt   10   18837,010 ±  1050,513  ops/s
TwentyKBenchmark.jayway_jackson_stream                                     $..friends..name  thrpt   10    9296,304 ±  1190,512  ops/s

TwitterBenchmark.gatling_jodd_chars                                          $.completed_in  thrpt   10   61875,438 ±  2294,678  ops/s
TwitterBenchmark.gatling_jodd_string                                         $.completed_in  thrpt   10   57035,810 ±  2198,980  ops/s
TwitterBenchmark.gatling_jackson_stream                                      $.completed_in  thrpt   10   43497,456 ±  3087,093  ops/s
TwitterBenchmark.jayway_jackson_stream                                       $.completed_in  thrpt   10   41292,192 ±  3030,398  ops/s

WebXmlBenchmark.gatling_jackson_stream        $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  163089,710 ± 11570,557  ops/s
WebXmlBenchmark.gatling_jodd_chars            $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  189186,981 ±  7706,387  ops/s
WebXmlBenchmark.gatling_jodd_string           $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  179017,113 ± 10244,521  ops/s
WebXmlBenchmark.jayway_jackson_stream         $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  150900,108 ± 10190,489  ops/s
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
