package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class TwentyKBenchmark extends JsonPathBenchmark {

  private static final String[] PATHS = new String[] {
          "$[*].friends[*].name",
          "$[*].friends[?(@.id == 1)].name"
  };

  @Benchmark
  public List<JsonNode> search() {
    String path = PATHS[ThreadLocalRandom.current().nextInt(PATHS.length)];
    return search(io.gatling.benchmark.jmespath.TwentyKBenchmark.DATA, path);
  }

  public static void main(String[] args) {
    System.out.println(new TwentyKBenchmark().search());
  }
}
