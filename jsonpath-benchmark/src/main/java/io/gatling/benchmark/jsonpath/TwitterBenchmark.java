package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import io.gatling.benchmark.jsonpath.parser.*;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.gatling.benchmark.util.Bytes.*;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class TwitterBenchmark {

  private static final byte[][] CHUNKS = split(readBytes("data/twitter.json"), MTU);

  @Param({
          "$.completed_in"
//          ,
//          "$.results[:3].from_user",
//          "$.results[1:9:-2].from_user",
//          "$.results[*].to_user_name",
//          "$.results[5].metadata.result_type",
//          "$.results[?(@.from_user == 'anna_gatling')]",
//          "$.results[?(@.from_user_id >= 1126180920)]"
  })
  public String path;

  @Benchmark
  public List<JsonNode> gatling_jackson_stream() throws Exception {
    return JacksonHelper.parseStream(CHUNKS, path);
  }

  @Benchmark
  public Object jayway_jackson_stream() throws Exception {
    return JaywayJacksonHelper.parseStream(CHUNKS, path);
  }
}
