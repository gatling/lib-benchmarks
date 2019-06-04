package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class TwentyKBenchmark extends JmesPathBenchmark {

  private static final JsonNode DATA = parseJson("/data/20k.json");

  @Param({
//          "$..address",
          "[*].friends[].name",
          "[*].friends[?id == `1`].name[]"
  })
  public String path;

  @Benchmark
  public JsonNode search() {
    return search(DATA, path);
  }
}
