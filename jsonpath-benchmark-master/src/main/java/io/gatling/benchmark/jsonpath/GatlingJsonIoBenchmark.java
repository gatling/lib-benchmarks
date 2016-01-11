package io.gatling.benchmark.jsonpath;

import com.cedarsoftware.util.io.JsonReader;
import io.gatling.jsonpath.JsonPath;
import io.gatling.jsonpath.JsonPath$;
import jodd.json.JsonParser;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.gatling.benchmark.jsonpath.Bytes.BYTES_AND_PATHS;

@OutputTimeUnit(TimeUnit.SECONDS)
public class GatlingJsonIoBenchmark {

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

  private static final Map<String, Object> OPTIONS;

  static {
    OPTIONS = new HashMap<>();
    OPTIONS.put(JsonReader.USE_MAPS, Boolean.TRUE);
  }

  @Benchmark
  public Object parseString(ThreadState state) throws Exception {
    int i = state.next();
    byte[] bytes = Bytes.merge(BYTES_AND_JSONPATHS[i].chunks);
    String text = new String(bytes, StandardCharsets.UTF_8);
    return BYTES_AND_JSONPATHS[i].path.query(JsonReader.jsonToJava(text, OPTIONS));
  }

  @Benchmark
  public Object parseStream(ThreadState state) throws Exception {
    int i = state.next();
    InputStream stream = Bytes.stream(BYTES_AND_JSONPATHS[i].chunks);
    return BYTES_AND_JSONPATHS[i].path.query(JsonReader.jsonToJava(stream, OPTIONS));
  }

  public static void main(String[] args) {
    byte[] bytes = Bytes.merge(BYTES_AND_JSONPATHS[0].chunks);
    String text = new String(bytes, StandardCharsets.UTF_8);
    JsonReader.jsonToJava(text, OPTIONS);
  }
}
