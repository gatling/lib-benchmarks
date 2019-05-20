package io.gatling.benchmark.jsonpath;

import io.gatling.benchmark.jsonpath.parser.*;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static io.gatling.benchmark.util.Bytes.MTU;
import static io.gatling.benchmark.util.Bytes.readBytes;
import static io.gatling.benchmark.util.Bytes.split;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class GoessnerBenchmark {

  protected static final byte[][] CHUNKS = split(readBytes("data/goessner.json"), MTU);

  @Param({"$.store.book[2].author",
          "$..author",
          "$.store.*",
//          "$.store..price",
          "$..book[2].title",
//          "$..book[-1:].title",
//          "$..book[:2].title",
          "$..*",
//          "$.store.book[*].niçôlàs['nico']['foo'][*].bar[1:-2:3]",
//          "$.store['book'][:2].title",
//          "$.store.book[?(@.isbn)].title",
//          "$.store.book[?(@.category == 'fiction')].title",
          "$.store.book[?(@.price < 10 && @.price >4)].title"})
  public String path;

//  @Benchmark
//  public Object gatling_gson_string() {
//    return GsonHelper.parseString(CHUNKS, path);
//  }
//
//  @Benchmark
//  public Object gatling_gson_stream() {
//    return GsonHelper.parseStream(CHUNKS, path);
//  }

//  @Benchmark
//  public Object gatling_jackson_string() throws Exception {
//    return JacksonHelper.parseString(CHUNKS, path);
//  }

  @Benchmark
  public Object gatling_jackson_stream() throws Exception {
    return JacksonHelper.parseStream(CHUNKS, path);
  }

  @Benchmark
  public Object gatling_jodd_string() throws Exception {
    return JoddHelper.parseString(CHUNKS, path);
  }

  @Benchmark
  public Object gatling_jodd_chars() throws Exception {
    return JoddHelper.parseChars(CHUNKS, path);
  }

//  @Benchmark
//  public Object jayway_jackson_string() throws Exception {
//    return JaywayJacksonHelper.parseString(CHUNKS, path);
//  }
//
//  @Benchmark
//  public Object jayway_jackson_bytes() throws Exception {
//    return JaywayJacksonHelper.parseString(CHUNKS, path);
//  }

  @Benchmark
  public Object jayway_jackson_stream() throws Exception {
    return JaywayJacksonHelper.parseStream(CHUNKS, path);
  }
}
