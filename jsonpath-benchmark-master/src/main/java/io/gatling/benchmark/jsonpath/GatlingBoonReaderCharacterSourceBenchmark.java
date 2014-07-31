package io.gatling.benchmark.jsonpath;

import static io.gatling.benchmark.jsonpath.GatlingJacksonBenchmark.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.boon.json.JsonParser;
import org.boon.json.implementation.JsonParserUsingCharacterSource;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@OutputTimeUnit(TimeUnit.SECONDS)
public class GatlingBoonReaderCharacterSourceBenchmark {

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
	public Object parseStream(ThreadState state) throws Exception {
		int i = state.next();
		InputStream stream = Bytes.stream(BYTES_AND_JSONPATHS[i].chunks);
		JsonParser jsonParser = new JsonParserUsingCharacterSource();
		Object json = jsonParser.parse(new InputStreamReader(stream, StandardCharsets.UTF_8));
		return BYTES_AND_JSONPATHS[i].path.query(json);
	}

	public static void main(String[] args) {

		for (int i = 0; i < BYTES_AND_JSONPATHS.length; i++) {

			InputStream stream = Bytes.stream(BYTES_AND_JSONPATHS[i].chunks);

			JsonParser jsonParser = new JsonParserUsingCharacterSource();
			Object json = jsonParser.parse(new InputStreamReader(stream, StandardCharsets.UTF_8));
			Object result = BYTES_AND_JSONPATHS[i].path.query(json);
			System.err.println(result);
		}
	}
}
