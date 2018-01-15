package io.gatling.benchmark.cssselectors;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import jodd.csselly.CSSelly;
import jodd.csselly.CssSelector;
import org.apache.commons.io.IOUtils;
import org.asynchttpclient.netty.util.Utf8ByteBufCharsetDecoder;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Evaluators;

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


	public Data(String path, String selector) {
		this.chunks = split(readBytes(path), 1500);
		joddSelectors = CSSelly.parse(selector);
		jsoupEvaluator = Evaluators.evaluator(selector);
	}

	public String toString() {

		ByteBuf buf = Unpooled.wrappedBuffer(chunks);
		try {
			return Utf8ByteBufCharsetDecoder.decodeUtf8(buf);
		} finally {
			buf.release();
		}
	}

	public InputStream toInputStream() {
		return new ByteBufInputStream(Unpooled.wrappedBuffer(chunks), true);
	}

	private static byte[] readBytes(String path) {
		try {
			return IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private static final byte[][] split(byte[] full, int chunkSize) {

		int chunkNumber = (int) Math.ceil(((double) full.length) / chunkSize);
		byte[][] chunks = new byte[chunkNumber][];

		int start = 0;
		int chunkCount = 0;

		while (start < full.length) {

			int length = Math.min(full.length - start, chunkSize);
			byte[] chunk = new byte[length];
			System.arraycopy(full, start, chunk, 0, length);
			chunks[chunkCount] = chunk;
			chunkCount++;
			start += length;
		}

		return chunks;
	}
}
