package io.gatling.benchmark.cssselectors;

import java.io.InputStream;
import java.util.List;

import io.gatling.benchmark.util.Bytes;
import jodd.csselly.CSSelly;
import jodd.csselly.CssSelector;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;

public class Data {

	public static final Data[] DATA = new Data[] {
		new Data("data/page1.html", ".article a[href*=api]"),
		new Data("data/page2.html", ".article a[href*=api]"),
		new Data("data/page3.html", "div.content h4"),
		new Data("data/page4.html", "h3.media__title")
	};

	private final byte[][] chunks;
	public final List<List<CssSelector>> joddSelectors;
	public final Evaluator jsoupEvaluator;


	private Data(String path, String selector) {
		this.chunks = Bytes.split(Bytes.readBytes(path), 1500);
		joddSelectors = CSSelly.parse(selector);
		jsoupEvaluator = QueryParser.parse(selector);
	}

	public String toString() {
		return Bytes.toString(chunks);
	}

	public char[] toChars() {
		return Bytes.toChars(chunks);
	}

	public InputStream toInputStream() {
		return Bytes.toInputStream(chunks);
	}
}
