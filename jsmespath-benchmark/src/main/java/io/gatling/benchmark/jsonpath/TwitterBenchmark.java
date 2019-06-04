package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class TwitterBenchmark extends JmesPathBenchmark {

  private static final JsonNode DATA = parseJson("/data/twitter.json");

  @Param({
          "completed_in",
          "results[:3].from_user",
          "results[*].to_user_name",
          "results[5].metadata.result_type",
          "results[?from_user == 'anna_gatling']",
          "results[?from_user_id >= `1126180920`]"
  })
  public String path;

  @Benchmark
  public JsonNode search() {
    return search(DATA, path);
  }
}
