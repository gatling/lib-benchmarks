package io.gatling.benchmark.jsonpath;

import io.gatling.benchmark.jsonpath.parser.*;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static io.gatling.benchmark.util.Bytes.*;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class TwentyKBenchmark {

  private static final byte[][] CHUNKS = split(readBytes("data/20k.json"), MTU);

  @Param({
//          "$..address",
          "$..friends..name"
//          ,
//          "$..friends[?(@.id == 1)].name"
  })
  public String path;

  @Benchmark
  public Object gatling_jackson_stream() throws Exception {
    return JacksonHelper.parseStream(CHUNKS, path);
  }

  @Benchmark
  public Object gatling_jodd_string() {
    return JoddHelper.parseString(CHUNKS, path);
  }

  @Benchmark
  public Object jayway_jackson_stream() throws Exception {
    return JaywayJacksonHelper.parseStream(CHUNKS, path);
  }

  public static void main(String[] args) throws Exception {
    System.out.println(JaywayJacksonHelper.parseStream(CHUNKS, "$..friends..name"));
  }
}
