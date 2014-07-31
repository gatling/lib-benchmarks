package io.gatling.benchmark.css;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import jodd.lagarto.dom.LagartoDOMBuilder;
import jodd.lagarto.dom.Node;
import jodd.lagarto.dom.NodeSelector;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

public class JoddVsJsoupTest extends AbstractBenchmark {

    private static final String TEXT1 = loadText("page.html");
    private static final String TEXT2 = loadText("page2.html");

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
    public void testJodd1() {
        NodeSelector selector = new NodeSelector((new LagartoDOMBuilder()).parse(TEXT1));
        LinkedList<Node> nodes = selector.select(".article a[href*=api]");
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("API Documentation", nodes.get(0).getTextContent().trim());
    }

    @Ignore
    public void testJodd2() {
        NodeSelector selector = new NodeSelector((new LagartoDOMBuilder()).parse(TEXT2));
        LinkedList<Node> nodes = selector.select(".name");
        Assert.assertEquals(2, nodes.size());
        Assert.assertEquals("slandelle", nodes.get(0).getTextContent().trim());
        Assert.assertEquals("README.md", nodes.get(1).getTextContent().trim());
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = BENCHMARKROUNDS, warmupRounds = WARMUPROUNDS, concurrency = CONCURRENCY)
    public void testJodd() {
        if (count.getAndIncrement() % 2 == 0)
            testJodd1();
        else
            testJodd2();
    }

    @Ignore
    public void testJsoup1() {
        Document doc = Jsoup.parse(TEXT1, "http://gatling-tool.org");
        Elements elements = doc.select(".article a[href*=api]");

        Assert.assertEquals("API Documentation", elements.text().trim());
    }

    @Ignore
    public void testJsoup2() {
        Document doc = Jsoup.parse(TEXT2, "https://github.com/excilys/gatling");
        Elements elements = doc.select(".name");

        Assert.assertEquals(2, elements.size());
        Assert.assertEquals("slandelle", elements.get(0).text().trim());
        Assert.assertEquals("README.md", elements.get(1).text().trim());
    }

    @Test
    @BenchmarkOptions(benchmarkRounds = BENCHMARKROUNDS, warmupRounds = WARMUPROUNDS, concurrency = CONCURRENCY)
    public void testJsoup() {
        if (count.getAndIncrement() % 2 == 0)
            testJsoup1();
        else
            testJsoup2();
    }
}
