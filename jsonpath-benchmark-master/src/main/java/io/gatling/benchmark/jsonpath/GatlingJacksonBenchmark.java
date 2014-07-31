package io.gatling.benchmark.jsonpath;

import static io.gatling.benchmark.jsonpath.Bytes.*;
import io.gatling.jsonpath.JsonPath;
import io.gatling.jsonpath.JsonPath$;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import com.fasterxml.jackson.databind.ObjectMapper;

@OutputTimeUnit(TimeUnit.SECONDS)
public class GatlingJacksonBenchmark {

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

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

	@GenerateMicroBenchmark
	public Object parseString(ThreadState state) throws Exception {
		int i = state.next();
		byte[] bytes = Bytes.merge(BYTES_AND_JSONPATHS[i].chunks);
		String text = new String(bytes, StandardCharsets.UTF_8);
		return BYTES_AND_JSONPATHS[i].path.query(OBJECT_MAPPER.readValue(text, Object.class));
	}

	@GenerateMicroBenchmark
	public Object parseBytes(ThreadState state) throws Exception {
		int i = state.next();
		byte[] bytes = Bytes.merge(BYTES_AND_JSONPATHS[i].chunks);
		return BYTES_AND_JSONPATHS[i].path.query(OBJECT_MAPPER.readValue(bytes, Object.class));
	}

	@GenerateMicroBenchmark
	public Object parseStream(ThreadState state) throws Exception {
		int i = state.next();
		InputStream stream = Bytes.stream(BYTES_AND_JSONPATHS[i].chunks);
		return BYTES_AND_JSONPATHS[i].path.query(OBJECT_MAPPER.readValue(stream, Object.class));
	}
	
	public static void main(String[] args) throws Exception {

		for (int i = 0; i < BYTES_AND_JSONPATHS.length; i++) {
			byte[] bytes = Bytes.merge(BYTES_AND_JSONPATHS[i].chunks);
			String text = new String(bytes, StandardCharsets.UTF_8);
			Object result = BYTES_AND_JSONPATHS[i].path.query(OBJECT_MAPPER.readValue(text, Object.class));
			System.err.println(result);
		}
	}
}
