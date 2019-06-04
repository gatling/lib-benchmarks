package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class WebXmlBenchmark extends JmesPathBenchmark {

  private static final JsonNode DATA = parseJson("/data/webxml.json");


  @Param({"\"web-app\".servlet[0].\"init-param\".dataStoreName"})
  public String path;

  @Benchmark
  public JsonNode search() {
    return search(DATA, path);
  }
}
