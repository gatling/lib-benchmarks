package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class CtimBenchmark extends JsonPathBenchmark {

  private static final String[] PATHS = new String[]{
//          "events.[*].name",
    "$.performances[?(@.eventId == 339420805)]"};

  @Benchmark
  public List<JsonNode> search() {
    String path = PATHS[ThreadLocalRandom.current().nextInt(PATHS.length)];
    return search(io.gatling.benchmark.jmespath.CtimBenchmark.DATA, path);
  }

  public static void main(String[] args) {
    System.out.println(new CtimBenchmark().search());
  }
}
