package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import io.gatling.benchmark.jsonpath.parser.*;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.gatling.benchmark.util.Bytes.*;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class WebXmlBenchmark {

  private static final byte[][] CHUNKS = split(readBytes("data/webxml.json"), MTU);

  @Param({"$.web-app.servlet[0].init-param.dataStoreName"})
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
