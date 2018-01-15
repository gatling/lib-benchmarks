package io.gatling.benchmark.jsonpath;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import io.gatling.jsonpath.JsonPath;
import io.gatling.jsonpath.JsonPath$;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.concurrent.TimeUnit;

import static io.gatling.benchmark.jsonpath.Bytes.BYTES_AND_PATHS;

@OutputTimeUnit(TimeUnit.SECONDS)
public class GatlingGsonBenchmark {

	public static final class BytesAndGatlingPath {
		public final byte[][] chunks;
		public final JsonPath path;

		public BytesAndGatlingPath(byte[][] chunks, JsonPath path) {
			this.chunks = chunks;
			this.path = path;
		}
	}

	public static final JsonPath compile(String path) {
		return JsonPath$.MODULE$.compile(path).right().get();
	}

	private static final Gson GSON = new Gson();
	private static final Type OBJECT_TYPE = TypeToken.get(Object.class).getType();

	public static final BytesAndGatlingPath[] BYTES_AND_JSONPATHS = new BytesAndGatlingPath[BYTES_AND_PATHS.size()];

	static {
		for (int i = 0; i < BYTES_AND_PATHS.size(); i++) {
			AbstractMap.Entry<byte[][], String> bytesAndPath = BYTES_AND_PATHS.get(i);
			BYTES_AND_JSONPATHS[i] = new BytesAndGatlingPath(bytesAndPath.getKey(), compile(bytesAndPath.getValue()));
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
		return BYTES_AND_JSONPATHS[i].path.query(GSON.fromJson(new JsonReader(new StringReader(string)), OBJECT_TYPE));
	}

	@Benchmark
	public Object parseStream(ThreadState state) throws Exception {
		int i = state.next();
		InputStream stream = Bytes.stream(BYTES_AND_JSONPATHS[i].chunks);
		return BYTES_AND_JSONPATHS[i].path.query(GSON.fromJson(new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8)), OBJECT_TYPE));
	}
}
