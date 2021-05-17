package io.gatling.benchmark.jmespath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class TwitterBenchmark extends JmesPathBenchmark {

  public static final JsonNode DATA = parseJson("/data/twitter.json");

  private static final String[] PATHS = new String[] {
          "completed_in",
          "results[:3].from_user",
          "results[*].to_user_name",
          "results[5].metadata.result_type",
          "results[?from_user == 'anna_gatling']",
          "results[?from_user_id >= `1126180920`]"
  };

  @Benchmark
  public JsonNode search() {
    String path = PATHS[ThreadLocalRandom.current().nextInt(PATHS.length)];
    return search(DATA, path);
  }

  public static void main(String[] args) {
    System.out.println(new TwitterBenchmark().search());
  }
}
