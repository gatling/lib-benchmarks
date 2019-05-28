package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import io.gatling.benchmark.jsonpath.parser.*;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.gatling.benchmark.util.Bytes.MTU;
import static io.gatling.benchmark.util.Bytes.readBytes;
import static io.gatling.benchmark.util.Bytes.split;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class GoessnerBenchmark {

  private static final byte[][] CHUNKS = split(readBytes("data/goessner.json"), MTU);

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

  @Benchmark
  public List<JsonNode> gatling_jackson_stream() throws Exception {
    return JacksonHelper.parseStream(CHUNKS, path);
  }

  @Benchmark
  public Object jayway_jackson_stream() throws Exception {
    return JaywayJacksonHelper.parseStream(CHUNKS, path);
  }
}
