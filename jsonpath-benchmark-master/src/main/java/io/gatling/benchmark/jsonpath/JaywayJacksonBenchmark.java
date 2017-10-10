package io.gatling.benchmark.jsonpath;

import static io.gatling.benchmark.jsonpath.Bytes.*;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@OutputTimeUnit(TimeUnit.SECONDS)
public class JaywayJacksonBenchmark {

	private static final class BytesAndJaywayPath {
		public final byte[][] chunks;
		public final JsonPath path;

		public BytesAndJaywayPath(byte[][] chunks, JsonPath path) {
			this.chunks = chunks;
			this.path = path;
		}
	}

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private static final BytesAndJaywayPath[] BYTES_AND_JSONPATHS = new BytesAndJaywayPath[BYTES_AND_PATHS.size()];

	static {
		for (int i = 0; i < BYTES_AND_PATHS.size(); i++) {
			Couple<byte[][], String> bytesAndPath = BYTES_AND_PATHS.get(i);
			BYTES_AND_JSONPATHS[i] = new BytesAndJaywayPath(bytesAndPath.left, JsonPath.compile(bytesAndPath.right));
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
		return BYTES_AND_JSONPATHS[i].path.read(OBJECT_MAPPER.readValue(string, Object.class));
	}

	@Benchmark
	public Object parseBytes(ThreadState state) throws Exception {
		int i = state.next();
		byte[] bytes = Bytes.merge(BYTES_AND_JSONPATHS[i].chunks);
		return BYTES_AND_JSONPATHS[i].path.read(OBJECT_MAPPER.readValue(bytes, Object.class));
	}

	@Benchmark
	public Object parseStream(ThreadState state) throws Exception {
		int i = state.next();
		InputStream stream = Bytes.stream(BYTES_AND_JSONPATHS[i].chunks);
		return BYTES_AND_JSONPATHS[i].path.read(OBJECT_MAPPER.readValue(stream, Object.class));
	}
}
