package io.gatling.benchmark.jmespath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class WebXmlBenchmark extends JmesPathBenchmark {

  public static final JsonNode DATA = parseJson("/data/webxml.json");

  private static final String[] PATHS = new String[] {"\"web-app\".servlet[0].\"init-param\".dataStoreName"};

  @Benchmark
  public JsonNode search() {
    String path = PATHS[ThreadLocalRandom.current().nextInt(PATHS.length)];
    return search(DATA, path);
  }

  public static void main(String[] args) {
    System.out.println(new WebXmlBenchmark().search());
  }
}
