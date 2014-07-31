package io.gatling.benchmark.jsonparsers;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.logic.BlackHole;

import com.fasterxml.jackson.databind.ObjectMapper;

@OutputTimeUnit(TimeUnit.SECONDS)
public class JacksonObjectBenchmark extends AbstractBenchmark {

	private static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();

	protected Object parse(byte[] bytes) throws Exception {
		return JACKSON_MAPPER.readValue(bytes, Map.class);
	}

	@GenerateMicroBenchmark
	public void roundRobin(ThreadState state, BlackHole bh) throws Exception {
		super.roundRobin(state, bh);
	}
}
