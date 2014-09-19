package io.gatling.benchmark.xpath;

import static io.gatling.benchmark.xpath.Bytes.*;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import io.gatling.benchmark.xpath.util.FastStringReader;
import io.gatling.benchmark.xpath.util.UnsafeUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.xml.sax.InputSource;

@OutputTimeUnit(TimeUnit.SECONDS)
public abstract class AbstractXPathBenchmark {

	@State(Scope.Thread)
	public static class ThreadState {
		private int i = -1;

		public int next() {
			i++;
			if (i == BYTES_AND_PATHS.size())
				i = 0;
			return i;
		}
	}

	protected abstract String parse(InputSource inputSource, String path) throws Exception;

	@Benchmark
	public String parseByString(ThreadState state) throws Exception {
		int i = state.next();

		Couple<byte[][], String> c = BYTES_AND_PATHS.get(i);
		byte[][] chunks = c.left;
		String path = c.right;

		String text = new String(merge(chunks), StandardCharsets.UTF_8);
		InputSource inputSource = new InputSource(new StringReader(text));
		return parse(inputSource, path);
	}

  @Benchmark
  public String parseByFastStringReader(ThreadState state) throws Exception {
    int i = state.next();

    Couple<byte[][], String> c = BYTES_AND_PATHS.get(i);
    byte[][] chunks = c.left;
    String path = c.right;

    String text = new String(merge(chunks), StandardCharsets.UTF_8);
    InputSource inputSource = new InputSource(new FastStringReader(text));
    return parse(inputSource, path);
  }

  @Benchmark
  public String parseByFastCharArrayReader(ThreadState state) throws Exception {
    int i = state.next();

    Couple<byte[][], String> c = BYTES_AND_PATHS.get(i);
    byte[][] chunks = c.left;
    String path = c.right;

    String text = new String(merge(chunks), StandardCharsets.UTF_8);
    InputSource inputSource = new InputSource(UnsafeUtil.newFastCharArrayReader(text));
    return parse(inputSource, path);
  }

	@Benchmark
	public String parseByInputStreamReader(ThreadState state) throws Exception {
		int i = state.next();

		Couple<byte[][], String> c = BYTES_AND_PATHS.get(i);
		byte[][] chunks = c.left;
		String path = c.right;

		InputSource inputSource = new InputSource(new InputStreamReader(stream(chunks), StandardCharsets.UTF_8));
		return parse(inputSource, path);
	}

	@Benchmark
	public String parseByInputStream(ThreadState state) throws Exception {
		int i = state.next();

		Couple<byte[][], String> c = BYTES_AND_PATHS.get(i);
		byte[][] chunks = c.left;
		String path = c.right;

		InputSource inputSource = new InputSource(stream(chunks));
		inputSource.setEncoding(StandardCharsets.UTF_8.name());
		return parse(inputSource, path);
	}
}
