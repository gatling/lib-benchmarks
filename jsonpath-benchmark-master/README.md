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

* Gatling is almost 2x faster than Jayway (when both running on Jackson)
* The combo (Gatling + Boon) is more than 3x faster than (Jayway + Jackson)
* Boon lazy value loading is very well suited for JsonPath where you only want some branches of the JSON AST
* Results displayed on Jayway's website are completely flawed, as they only have path caching enabled for their side, not for Gatling.
* Jodd parser is really good, on par with Jackson on this use case (kudos Igor!)
* Gson falls way behind, Jackson is more than 2x faster (and make sure to cache `java.lang.reflect.Type` instances)

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
CtimBenchmark.gatling_jackson_stream                                            $.events.*.name  thrpt   10     361,458 ±    28,440  ops/s
CtimBenchmark.jayway_jackson_stream                                             $.events.*.name  thrpt   10     347,433 ±    41,186  ops/s
CtimBenchmark.gatling_jodd_string                                               $.events.*.name  thrpt   10     223,214 ±     7,115  ops/s
CtimBenchmark.gatling_jodd_chars                                                $.events.*.name  thrpt   10     213,195 ±    10,390  ops/s

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
GoessnerBenchmark.jayway_jackson_bytes                                                $..author  thrpt   10  215629,796 ± 21106,829  ops/s

GoessnerBenchmark.gatling_jodd_chars                                                  $.store.*  thrpt   10  716462,316 ± 41178,303  ops/s
GoessnerBenchmark.gatling_jodd_string                                                 $.store.*  thrpt   10  716788,719 ± 54414,965  ops/s
GoessnerBenchmark.gatling_jackson_stream                                              $.store.*  thrpt   10  464056,774 ± 24101,976  ops/s
GoessnerBenchmark.jayway_jackson_stream                                               $.store.*  thrpt   10  402515,912 ± 26253,349  ops/s

GoessnerBenchmark.gatling_jodd_string                                            $.store..price  thrpt   10  728204,803 ± 65657,964  ops/s
GoessnerBenchmark.gatling_jodd_chars                                             $.store..price  thrpt   10  707869,694 ± 30117,557  ops/s
GoessnerBenchmark.gatling_jackson_stream                                         $.store..price  thrpt   10  475891,456 ± 24477,374  ops/s
GoessnerBenchmark.jayway_jackson_stream                                          $.store..price  thrpt   10  256336,546 ± 26624,323  ops/s

GoessnerBenchmark.gatling_jodd_chars                                           $..book[2].title  thrpt   10  725319,031 ± 34149,445  ops/s
GoessnerBenchmark.gatling_jodd_string                                          $..book[2].title  thrpt   10  754908,451 ± 28221,410  ops/s
GoessnerBenchmark.gatling_jackson_stream                                       $..book[2].title  thrpt   10  430898,682 ± 21003,529  ops/s
GoessnerBenchmark.jayway_jackson_stream                                        $..book[2].title  thrpt   10  243229,836 ± 16937,642  ops/s

GoessnerBenchmark.gatling_jodd_string                                        $..book[-1:].title  thrpt   10  722504,949 ± 30361,335  ops/s
GoessnerBenchmark.gatling_jodd_chars                                         $..book[-1:].title  thrpt   10  699590,587 ± 61369,162  ops/s
GoessnerBenchmark.gatling_jackson_stream                                     $..book[-1:].title  thrpt   10  442964,182 ± 47802,629  ops/s
GoessnerBenchmark.jayway_jackson_stream                                      $..book[-1:].title  thrpt   10  231090,446 ±  8189,519  ops/s

GoessnerBenchmark.gatling_jodd_chars                                          $..book[:2].title  thrpt   10  698102,126 ± 32445,997  ops/s
GoessnerBenchmark.gatling_jodd_string                                         $..book[:2].title  thrpt   10  702183,188 ± 31855,733  ops/s
GoessnerBenchmark.gatling_jackson_stream                                      $..book[:2].title  thrpt   10  384092,618 ± 83673,012  ops/s
GoessnerBenchmark.jayway_jackson_stream                                       $..book[:2].title  thrpt   10  227583,887 ± 16342,966  ops/s

GoessnerBenchmark.gatling_jodd_string                                                      $..*  thrpt   10  727748,549 ± 28693,801  ops/s
GoessnerBenchmark.gatling_jodd_chars                                                       $..*  thrpt   10  661918,534 ± 34739,219  ops/s
GoessnerBenchmark.gatling_jackson_stream                                                   $..*  thrpt   10  417010,968 ± 43359,052  ops/s
GoessnerBenchmark.jayway_jackson_stream                                                    $..*  thrpt   10  162307,152 ± 15474,768  ops/s

GoessnerBenchmark.gatling_jodd_chars      $.store.book[*].niçôlàs['nico']['foo'][*].bar[1:-2:3]  thrpt   10  726893,787 ± 23870,079  ops/s
GoessnerBenchmark.gatling_jodd_string     $.store.book[*].niçôlàs['nico']['foo'][*].bar[1:-2:3]  thrpt   10  710853,110 ± 24074,808  ops/s
GoessnerBenchmark.gatling_jackson_stream  $.store.book[*].niçôlàs['nico']['foo'][*].bar[1:-2:3]  thrpt   10  420072,074 ± 55699,863  ops/s
GoessnerBenchmark.jayway_jackson_stream   $.store.book[*].niçôlàs['nico']['foo'][*].bar[1:-2:3]  thrpt   10  353917,978 ± 25785,189  ops/s

GoessnerBenchmark.gatling_jodd_string                                 $.store['book'][:2].title  thrpt   10  729980,626 ± 25096,299  ops/s
GoessnerBenchmark.gatling_jodd_chars                                  $.store['book'][:2].title  thrpt   10  721304,170 ± 32195,553  ops/s
GoessnerBenchmark.gatling_jackson_stream                              $.store['book'][:2].title  thrpt   10  441274,199 ± 36813,581  ops/s
GoessnerBenchmark.jayway_jackson_stream                               $.store['book'][:2].title  thrpt   10  337093,333 ± 11206,288  ops/s

GoessnerBenchmark.gatling_jodd_string                             $.store.book[?(@.isbn)].title  thrpt   10  740407,820 ± 31535,295  ops/s
GoessnerBenchmark.gatling_jodd_chars                              $.store.book[?(@.isbn)].title  thrpt   10  725541,484 ± 27878,316  ops/s
GoessnerBenchmark.gatling_jackson_stream                          $.store.book[?(@.isbn)].title  thrpt   10  420730,518 ± 58274,176  ops/s
GoessnerBenchmark.jayway_jackson_stream                           $.store.book[?(@.isbn)].title  thrpt   10  153384,313 ±  2529,469  ops/s

GoessnerBenchmark.gatling_jodd_chars             $.store.book[?(@.category == 'fiction')].title  thrpt   10  726795,970 ± 31967,129  ops/s
GoessnerBenchmark.gatling_jodd_string            $.store.book[?(@.category == 'fiction')].title  thrpt   10  705117,294 ± 24948,745  ops/s
GoessnerBenchmark.gatling_jackson_stream         $.store.book[?(@.category == 'fiction')].title  thrpt   10  443122,086 ± 31004,627  ops/s
GoessnerBenchmark.jayway_jackson_stream          $.store.book[?(@.category == 'fiction')].title  thrpt   10  297996,624 ± 19834,120  ops/s

GoessnerBenchmark.gatling_jodd_string         $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  744802,424 ± 35575,593  ops/s
GoessnerBenchmark.gatling_jodd_chars          $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  672680,786 ± 26271,279  ops/s
GoessnerBenchmark.gatling_jackson_stream      $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  462328,177 ± 23672,630  ops/s
GoessnerBenchmark.jayway_jackson_stream       $.store.book[?(@.price < 10 && @.price >4)].title  thrpt   10  111774,743 ±  8713,608  ops/s

TwentyKBenchmark.gatling_jodd_chars                                                  $..address  thrpt   10   30984,185 ±  1829,645  ops/s
TwentyKBenchmark.gatling_jodd_string                                                 $..address  thrpt   10   30685,536 ±  1978,047  ops/s
TwentyKBenchmark.gatling_jackson_stream                                              $..address  thrpt   10   17814,517 ±  1111,924  ops/s
TwentyKBenchmark.jayway_jackson_stream                                               $..address  thrpt   10   10763,743 ±  1512,243  ops/s

TwentyKBenchmark.gatling_jodd_chars                                            $..friends..name  thrpt   10   31032,810 ±  1968,179  ops/s
TwentyKBenchmark.gatling_jodd_string                                           $..friends..name  thrpt   10   30893,496 ±  1543,162  ops/s
TwentyKBenchmark.gatling_jackson_stream                                        $..friends..name  thrpt   10   17208,600 ±   918,750  ops/s
TwentyKBenchmark.jayway_jackson_stream                                         $..friends..name  thrpt   10    9115,453 ±  1660,753  ops/s

TwentyKBenchmark.gatling_jodd_chars                               $..friends[?(@.id == 1)].name  thrpt   10   30896,273 ±  1840,442  ops/s
TwentyKBenchmark.gatling_jodd_string                              $..friends[?(@.id == 1)].name  thrpt   10   30169,590 ±  1738,767  ops/s
TwentyKBenchmark.gatling_jackson_stream                           $..friends[?(@.id == 1)].name  thrpt   10   17950,789 ±  1581,813  ops/s
TwentyKBenchmark.jayway_jackson_stream                            $..friends[?(@.id == 1)].name  thrpt   10    9569,863 ±  1525,252  ops/s

TwitterBenchmark.gatling_jodd_string                                             $.completed_in  thrpt   10   61439,419 ±  2165,309  ops/s
TwitterBenchmark.gatling_jodd_chars                                              $.completed_in  thrpt   10   59675,834 ±  2578,713  ops/s
TwitterBenchmark.gatling_jackson_stream                                          $.completed_in  thrpt   10   40422,112 ±  2303,300  ops/s
TwitterBenchmark.jayway_jackson_stream                                           $.completed_in  thrpt   10   40033,987 ±  2697,736  ops/s

TwitterBenchmark.gatling_jodd_string                                    $.results[:3].from_user  thrpt   10   62030,717 ±  1960,432  ops/s
TwitterBenchmark.gatling_jodd_chars                                     $.results[:3].from_user  thrpt   10   60266,930 ±  3333,768  ops/s
TwitterBenchmark.gatling_jackson_stream                                 $.results[:3].from_user  thrpt   10   40727,567 ±  2370,551  ops/s
TwitterBenchmark.jayway_jackson_stream                                  $.results[:3].from_user  thrpt   10   39095,292 ±  3780,998  ops/s

TwitterBenchmark.gatling_jodd_chars                                 $.results[1:9:-2].from_user  thrpt   10   59951,999 ±  3459,894  ops/s
TwitterBenchmark.gatling_jodd_string                                $.results[1:9:-2].from_user  thrpt   10   56512,878 ±  1876,498  ops/s
TwitterBenchmark.gatling_jackson_stream                             $.results[1:9:-2].from_user  thrpt   10   40133,516 ±  3446,284  ops/s
TwitterBenchmark.jayway_jackson_stream                              $.results[1:9:-2].from_user  thrpt   10   37977,536 ±  1650,560  ops/s

TwitterBenchmark.gatling_jodd_string                                  $.results[*].to_user_name  thrpt   10   61223,020 ±  2390,678  ops/s
TwitterBenchmark.gatling_jodd_chars                                   $.results[*].to_user_name  thrpt   10   61272,645 ±  2722,454  ops/s
TwitterBenchmark.gatling_jackson_stream                               $.results[*].to_user_name  thrpt   10   40589,518 ±  2530,940  ops/s
TwitterBenchmark.jayway_jackson_stream                                $.results[*].to_user_name  thrpt   10   38328,068 ±  1040,841  ops/s

TwitterBenchmark.gatling_jodd_string                          $.results[5].metadata.result_type  thrpt   10   61978,468 ±  1980,861  ops/s
TwitterBenchmark.gatling_jodd_chars                           $.results[5].metadata.result_type  thrpt   10   61291,943 ±  2554,687  ops/s
TwitterBenchmark.jayway_jackson_stream                        $.results[5].metadata.result_type  thrpt   10   39504,352 ±  1450,316  ops/s
TwitterBenchmark.gatling_jackson_stream                       $.results[5].metadata.result_type  thrpt   10   39017,849 ±  4228,358  ops/s

TwitterBenchmark.gatling_jodd_string                $.results[?(@.from_user == 'anna_gatling')]  thrpt   10   62084,287 ±  2238,324  ops/s
TwitterBenchmark.gatling_jodd_chars                 $.results[?(@.from_user == 'anna_gatling')]  thrpt   10   61235,145 ±  2539,308  ops/s
TwitterBenchmark.gatling_jackson_stream             $.results[?(@.from_user == 'anna_gatling')]  thrpt   10   37794,248 ±  3324,390  ops/s
TwitterBenchmark.jayway_jackson_stream              $.results[?(@.from_user == 'anna_gatling')]  thrpt   10   37023,355 ±  2039,022  ops/s

TwitterBenchmark.gatling_jodd_chars                  $.results[?(@.from_user_id >= 1126180920)]  thrpt   10   61349,104 ±  2612,523  ops/s
TwitterBenchmark.gatling_jodd_string                 $.results[?(@.from_user_id >= 1126180920)]  thrpt   10   61192,943 ±  1939,777  ops/s
TwitterBenchmark.gatling_jackson_stream              $.results[?(@.from_user_id >= 1126180920)]  thrpt   10   38646,750 ±  2321,698  ops/s
TwitterBenchmark.jayway_jackson_stream               $.results[?(@.from_user_id >= 1126180920)]  thrpt   10   36127,969 ±  2527,270  ops/s

WebXmlBenchmark.gatling_jodd_chars                $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  191308,414 ± 10472,964  ops/s
WebXmlBenchmark.gatling_jodd_string               $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  183604,549 ±  7216,906  ops/s
WebXmlBenchmark.gatling_jackson_stream            $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  152383,865 ± 19208,084  ops/s
WebXmlBenchmark.jayway_jackson_stream             $.web-app.servlet[0].init-param.dataStoreName  thrpt   10  145736,682 ±  7073,220  ops/s
```

* Hotspot 9.0.4

```
```
