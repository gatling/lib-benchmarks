package io.gatling.benchmark.jsonpath;

import io.advantageous.boon.json.implementation.JsonFastParser;
import io.gatling.benchmark.util.Bytes;
import io.gatling.benchmark.util.UnsafeUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static io.gatling.benchmark.jsonpath.GatlingJacksonBenchmark.BYTES_AND_JSONPATHS;

@OutputTimeUnit(TimeUnit.SECONDS)
public class GatlingBoonFastLazyChopBenchmark {

	static {
		if (!System.getProperty("java.version").startsWith("1.8")) {
			System.setProperty("io.advantageous.boon.faststringutils.disable", "true");
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

	private JsonFastParser newLazyChopParser() {
		JsonFastParser jsonParser = new JsonFastParser(false, false, true, false);
		jsonParser.setCharset(StandardCharsets.UTF_8);
		return jsonParser;
	}

	@Benchmark
	public Object parseChars(ThreadState state) {
		int i = state.next();
		char[] chars = Bytes.toChars(BYTES_AND_JSONPATHS[i].chunks);
		Object json = newLazyChopParser().parse(chars);
		return BYTES_AND_JSONPATHS[i].path.query(json);
	}
	
	@Benchmark
	public Object parseStream(ThreadState state) {
		int i = state.next();
		InputStream stream = Bytes.toInputStream(BYTES_AND_JSONPATHS[i].chunks);
		Object json = newLazyChopParser().parse(stream, StandardCharsets.UTF_8);
		return BYTES_AND_JSONPATHS[i].path.query(json);
	}
}
