package io.gatling.benchmark.cssselectors;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class Bytes {

	public static final String SELECTOR1 = ".article a[href*=api]";
	public static final String SELECTOR2 = ".name";
	public static final String[] ALL_SELECTOR = { SELECTOR1, SELECTOR2 };

	public static final byte[] PAGE1_BYTES = readBytes("data/page1.html");
	public static final byte[] PAGE2_BYTES = readBytes("data/page2.html");
	public static final byte[][] ALL_BYTES = { PAGE1_BYTES, PAGE2_BYTES };

	private static byte[] readBytes(String path) {
		try {
			return IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
