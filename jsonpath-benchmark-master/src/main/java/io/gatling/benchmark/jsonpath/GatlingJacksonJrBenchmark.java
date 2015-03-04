package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.jr.ob.JSON;
import io.gatling.jsonpath.JsonPath;
import io.gatling.jsonpath.JsonPath$;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static io.gatling.benchmark.jsonpath.Bytes.BYTES_AND_PATHS;

@OutputTimeUnit(TimeUnit.SECONDS)
public class GatlingJacksonJrBenchmark {

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

	public static final BytesAndGatlingPath[] BYTES_AND_JSONPATHS = new BytesAndGatlingPath[BYTES_AND_PATHS.size()];

	static {
		for (int i = 0; i < BYTES_AND_PATHS.size(); i++) {
			Couple<byte[][], String> bytesAndPath = BYTES_AND_PATHS.get(i);
			BYTES_AND_JSONPATHS[i] = new BytesAndGatlingPath(bytesAndPath.left, compile(bytesAndPath.right));
		}
	}

  private static final JSON JSON_JR = JSON.std
    .without(JSON.Feature.HANDLE_JAVA_BEANS)
    .with(JSON.Feature.READ_ONLY);

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
		byte[] bytes = Bytes.merge(BYTES_AND_JSONPATHS[i].chunks);
		String text = new String(bytes, StandardCharsets.UTF_8);
		return BYTES_AND_JSONPATHS[i].path.query(JSON_JR.anyFrom(text));
	}

	@Benchmark
	public Object parseBytes(ThreadState state) throws Exception {
		int i = state.next();
		byte[] bytes = Bytes.merge(BYTES_AND_JSONPATHS[i].chunks);
		return BYTES_AND_JSONPATHS[i].path.query(JSON_JR.anyFrom(bytes));
	}

	@Benchmark
	public Object parseStream(ThreadState state) throws Exception {
		int i = state.next();
		InputStream stream = Bytes.stream(BYTES_AND_JSONPATHS[i].chunks);
		return BYTES_AND_JSONPATHS[i].path.query(JSON_JR.anyFrom(stream));
	}
	
	public static void main(String[] args) throws Exception {

		for (int i = 0; i < BYTES_AND_JSONPATHS.length; i++) {
			byte[] bytes = Bytes.merge(BYTES_AND_JSONPATHS[i].chunks);
			String text = new String(bytes, StandardCharsets.UTF_8);
			Object result = BYTES_AND_JSONPATHS[i].path.query(JSON_JR.anyFrom(text));
			System.err.println(result);
		}
	}
}
