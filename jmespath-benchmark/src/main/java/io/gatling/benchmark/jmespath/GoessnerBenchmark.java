package io.gatling.benchmark.jmespath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class GoessnerBenchmark extends JmesPathBenchmark {

  public static final JsonNode DATA = parseJson("/data/goessner.json");

  private static final String[] PATHS = new String[] {
    "store.book[2].author",
    "store.book[*].author",
    "store.[*]",
    "store.book[2].title",
    "store.book[:2].title",
    "store.book[?isbn].title",
    "store.book[?category == 'fiction'].title"
  };

  @Benchmark
  public JsonNode search() {
    String path = PATHS[ThreadLocalRandom.current().nextInt(PATHS.length)];
    return search(DATA, path);
  }
}
