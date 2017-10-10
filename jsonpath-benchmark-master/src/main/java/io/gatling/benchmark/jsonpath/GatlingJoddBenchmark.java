package io.gatling.benchmark.jsonpath;

import static io.gatling.benchmark.jsonpath.Bytes.*;

import io.gatling.jsonpath.JsonPath;
import io.gatling.jsonpath.JsonPath$;

import java.util.concurrent.TimeUnit;

import jodd.json.JsonParser;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@OutputTimeUnit(TimeUnit.SECONDS)
public class GatlingJoddBenchmark {

  public static final class BytesAndGatlingPath {
    public final byte[][] chunks;
    public final JsonPath path;

    public BytesAndGatlingPath(byte[][] chunks, JsonPath path) {
      this.chunks = chunks;
      this.path = path;
    }
  }

  public static JsonPath compile(String path) {
    return JsonPath$.MODULE$.compile(path).right().get();
  }

  private static final ThreadLocal<JsonParser> JSON_PARSER_THREAD_LOCAL = new ThreadLocal<JsonParser>() {
    @Override
    protected JsonParser initialValue() {
      return new JsonParser();
    }
  };

  public static final BytesAndGatlingPath[] BYTES_AND_JSONPATHS = new BytesAndGatlingPath[BYTES_AND_PATHS.size()];

  static {
    for (int i = 0; i < BYTES_AND_PATHS.size(); i++) {
      Couple<byte[][], String> bytesAndPath = BYTES_AND_PATHS.get(i);
      BYTES_AND_JSONPATHS[i] = new BytesAndGatlingPath(bytesAndPath.left, compile(bytesAndPath.right));
    }
  }

  @State(Scope.Thread)
  public static class ThreadState {
    private int i = -1;

    public int next() {
      i++;
      if (i == BYTES_AND_JSONPATHS.length)
        i = 0;
      return i;
    }
  }

  @Benchmark
  public Object parseString(ThreadState state) throws Exception {
    int i = state.next();
    String string = ByteArrayUtf8Decoder.decode(BYTES_AND_JSONPATHS[i].chunks);
    return BYTES_AND_JSONPATHS[i].path.query(JSON_PARSER_THREAD_LOCAL.get().parse(string));
  }
}
