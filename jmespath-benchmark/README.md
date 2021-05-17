# JMESPath microbenchmark

## Test Case

JSON payloads are read from file before test as we only want to focus on search

The test mixes different payload sizes and paths:

* CtimBenchmark:     1.8MB
* GoessnerBenchmark: 789B
* TwentyKBenchmark:  32KB
* TwitterBenchmark:  13KB
* WebXmlBenchmark:   3KB

Expressions are precompiled.

## Conclusions

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
Benchmark                                                                            (path)   Mode  Cnt         Score   Error  Units
i.g.b.jmespath.CtimBenchmark.search                   performances[?eventId == `339420805`]  thrpt        6938350.141          ops/s
i.g.b.jsonpath.CtimBenchmark.search               $.performances[?(@.eventId == 339420805)]  thrpt        2985692.990          ops/s

i.g.b.jmespath.GoessnerBenchmark.search                                store.book[2].author  thrpt       11638450.865          ops/s
i.g.b.jsonpath.GoessnerBenchmark.search                              $.store.book[2].author  thrpt        4696754.917          ops/s

i.g.b.jmespath.GoessnerBenchmark.search                                store.book[*].author  thrpt        7378706.376          ops/s
i.g.b.jsonpath.GoessnerBenchmark.search                                           $..author  thrpt        2614387.186          ops/s

i.g.b.jmespath.GoessnerBenchmark.search                                           store.[*]  thrpt        9145832.690          ops/s
i.g.b.jsonpath.GoessnerBenchmark.search                                           $.store.*  thrpt        5931484.214          ops/s

i.g.b.jmespath.GoessnerBenchmark.search                                 store.book[2].title  thrpt       11615368.759          ops/s
i.g.b.jsonpath.GoessnerBenchmark.search                               $.store.book[2].title  thrpt        4688555.056          ops/s

i.g.b.jmespath.GoessnerBenchmark.search                                store.book[:2].title  thrpt        5656214.430          ops/s
i.g.b.jsonpath.GoessnerBenchmark.search                              $.store.book[:2].title  thrpt        3028539.093          ops/s

i.g.b.jmespath.GoessnerBenchmark.search                             store.book[?isbn].title  thrpt        5078121.949          ops/s
i.g.b.jsonpath.GoessnerBenchmark.search                       $.store.book[?(@.isbn)].title  thrpt        2011166.409          ops/s

i.g.b.jmespath.GoessnerBenchmark.search      store.book[?price < `10` && price > `4`].title  thrpt        2918079.391          ops/s
i.g.b.jsonpath.GoessnerBenchmark.search  $.store.book[?(@.price < 10 && @.price > 4)].title  thrpt        1191390.370          ops/s

i.g.b.jmespath.GoessnerBenchmark.search            store.book[?category == 'fiction'].title  thrpt        3615047.505          ops/s
i.g.b.jsonpath.GoessnerBenchmark.search      $.store.book[?(@.category == 'fiction')].title  thrpt        1475567.467          ops/s

i.g.b.jmespath.TwentyKBenchmark.search                                   [*].friends[].name  thrpt         454110.156          ops/s
i.g.b.jsonpath.TwentyKBenchmark.search                                 $[*].friends[*].name  thrpt         123406.904          ops/s

i.g.b.jmespath.TwentyKBenchmark.search                       [*].friends[?id == `1`].name[]  thrpt         196076.841          ops/s
i.g.b.jsonpath.TwentyKBenchmark.search                      $[*].friends[?(@.id == 1)].name  thrpt          89896.170          ops/s

i.g.b.jmespath.TwitterBenchmark.search                                         completed_in  thrpt       12301123.639          ops/s
i.g.b.jsonpath.TwitterBenchmark.search                                       $.completed_in  thrpt       10728864.926          ops/s

i.g.b.jmespath.TwitterBenchmark.search                                results[:3].from_user  thrpt        4714487.865          ops/s
i.g.b.jsonpath.TwitterBenchmark.search                              $.results[:3].from_user  thrpt        2608627.966          ops/s

i.g.b.jmespath.TwitterBenchmark.search                              results[*].to_user_name  thrpt        4618617.390          ops/s
i.g.b.jsonpath.TwitterBenchmark.search                            $.results[*].to_user_name  thrpt        1408469.130          ops/s

i.g.b.jmespath.TwitterBenchmark.search                      results[5].metadata.result_type  thrpt        8491762.162          ops/s
i.g.b.jsonpath.TwitterBenchmark.search                    $.results[5].metadata.result_type  thrpt        4671238.139          ops/s

i.g.b.jmespath.TwitterBenchmark.search                results[?from_user == 'anna_gatling']  thrpt        2494036.345          ops/s
i.g.b.jsonpath.TwitterBenchmark.search          $.results[?(@.from_user == 'anna_gatling')]  thrpt         929922.749          ops/s

i.g.b.jmespath.TwitterBenchmark.search               results[?from_user_id >= `1126180920`]  thrpt        2336137.132          ops/s
i.g.b.jsonpath.TwitterBenchmark.search           $.results[?(@.from_user_id >= 1126180920)]  thrpt         711846.527          ops/s

i.g.b.jmespath.WebXmlBenchmark.search       "web-app".servlet[0]."init-param".dataStoreName  thrpt        9461642.471          ops/s
i.g.b.jsonpath.WebXmlBenchmark.search         $.web-app.servlet[0].init-param.dataStoreName  thrpt        3954484.040          ops/s

i.g.b.jsonpath.TwitterBenchmark.search                          $.results[1:9:-2].from_user  thrpt        8722733.448          ops/s
```

* Hotspot 11.0.3+7

Results are worse than with JDK8 (GC?).

```
```
