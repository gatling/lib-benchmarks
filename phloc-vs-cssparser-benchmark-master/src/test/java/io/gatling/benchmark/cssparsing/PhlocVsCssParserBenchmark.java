package io.gatling.benchmark.cssparsing;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

public class PhlocVsCssParserBenchmark extends AbstractBenchmark {

	private static final String TEXT1 = loadText("base.css");
//	private static final String TEXT2 = loadText("page2.html");

	private static final int BENCHMARKROUNDS = 5000;
	private static final int WARMUPROUNDS = 5000;
	private static final int CONCURRENCY = 50;

	private AtomicInteger count = new AtomicInteger();

	private static String loadText(String file) {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		try {
			return IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	@Ignore
	public void testPhloc1() {
		List<String> urls = PhlocParsing.extractUrls(TEXT1);
		Assert.assertEquals(12, urls.size());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = BENCHMARKROUNDS, warmupRounds = WARMUPROUNDS, concurrency = CONCURRENCY)
	public void testPhloc() {
//		if (count.getAndIncrement() % 2 == 0)
			testPhloc1();
//		else
//			testPhloc2();
	}

	@Ignore
	public void testCssParser1() throws IOException {
		List<String> urls = CssParserParsing.extractUrls(TEXT1);
		Assert.assertEquals(12, urls.size());
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = BENCHMARKROUNDS, warmupRounds = WARMUPROUNDS, concurrency = CONCURRENCY)
	public void testCssParser() throws IOException {
//		if (count.getAndIncrement() % 2 == 0)
			testCssParser1();
//		else
//			testCssParser2();
	}
}
