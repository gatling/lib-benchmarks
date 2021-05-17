package io.gatling.benchmark.jmespath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class CtimBenchmark extends JmesPathBenchmark {

  public static final JsonNode DATA = parseJson("/data/citm.json");

  private static final String[] PATHS = new String[] {
//          "events.[*].name",
          "performances[?eventId == `339420805`]"};

  @Benchmark
  public JsonNode search() {
    String path = PATHS[ThreadLocalRandom.current().nextInt(PATHS.length)];
    return search(DATA, path);
  }

  public static void main(String[] args) {
    System.out.println(new CtimBenchmark().search());
  }
}
