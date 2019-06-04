package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class CtimBenchmark extends JmesPathBenchmark {

  private static final JsonNode DATA = parseJson("/data/citm.json");

  @Param({
//          "events.[*].name",
          "performances[?eventId == `339420805`]"})
  public String path;

  @Benchmark
  public JsonNode search() {
    return search(DATA, path);
  }
}
