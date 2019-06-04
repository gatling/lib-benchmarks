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
```

* Hotspot 11.0.3+7

Results are worse than with JDK8 (GC?).

```
```
