package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class TwitterBenchmark extends JsonPathBenchmark {

  private static final String[] PATHS = new String[] {
    "$.completed_in",
    "$.results[:3].from_user",
    "$.results[1:9:-2].from_user",
    "$.results[*].to_user_name",
    "$.results[5].metadata.result_type",
    "$.results[?(@.from_user == 'anna_gatling')]",
    "$.results[?(@.from_user_id >= 1126180920)]"
  };

  @Benchmark
  public List<JsonNode> search() {
    String path = PATHS[ThreadLocalRandom.current().nextInt(PATHS.length)];
    return search(io.gatling.benchmark.jmespath.TwitterBenchmark.DATA, path);
  }

  public static void main(String[] args) {
    System.out.println(new TwitterBenchmark().search());
  }
}
