package io.gatling.benchmark.jmespath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class TwentyKBenchmark extends JmesPathBenchmark {

  public static final JsonNode DATA = parseJson("/data/20k.json");

  private static final String[] PATHS = new String[] {
          "[*].friends[].name",
          "[*].friends[?id == `1`].name[]"
  };

  @Benchmark
  public JsonNode search() {
    String path = PATHS[ThreadLocalRandom.current().nextInt(PATHS.length)];
    return search(DATA, path);
  }

  public static void main(String[] args) {
    System.out.println(new TwentyKBenchmark().search());
  }
}
