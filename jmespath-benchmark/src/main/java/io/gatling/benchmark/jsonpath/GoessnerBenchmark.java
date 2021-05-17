package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class GoessnerBenchmark extends JsonPathBenchmark {

  private static final String[] PATHS = new String[] {
    "$.store.book[2].author",
      "$..author",
      "$.store.*",
      "$.store.book[2].title",
      "$.store.book[:2].title",
      "$.store.book[?(@.isbn)].title",
      "$.store.book[?(@.category == 'fiction')].title"
  };

  @Benchmark
  public List<JsonNode> search() {
    String path = PATHS[ThreadLocalRandom.current().nextInt(PATHS.length)];
    return search(io.gatling.benchmark.jmespath.GoessnerBenchmark.DATA, path);
  }

  public static void main(String[] args) {
    System.out.println(new GoessnerBenchmark().search());
  }
}
