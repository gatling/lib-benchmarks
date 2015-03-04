package io.gatling.benchmark.regex;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class Bytes {

	public static final Pattern PAGE1_PATTERN = Pattern.compile("<a href=\"([^\"]+)\">MacBook Pro</a>");
	public static final Pattern PAGE2_PATTERN = Pattern.compile("(?s)<input\\s+type=\"hidden\"\\s+name=\"_token\"\\s+value=\"([^\"]+)");
	public static final Pattern[] ALL_PATTERNS = new Pattern[] { PAGE1_PATTERN, PAGE2_PATTERN };


  public static final com.google.re2j.Pattern RE2J_PAGE1_PATTERN = com.google.re2j.Pattern.compile("<a href=\"([^\"]+)\">MacBook Pro</a>");
  public static final com.google.re2j.Pattern RE2J_PAGE2_PATTERN = com.google.re2j.Pattern.compile("(?s)<input\\s+type=\"hidden\"\\s+name=\"_token\"\\s+value=\"([^\"]+)");
  public static final com.google.re2j.Pattern[] RE2J_ALL_PATTERNS = new com.google.re2j.Pattern[] { RE2J_PAGE1_PATTERN, RE2J_PAGE2_PATTERN };

	public static final byte[] PAGE1_BYTES = readBytes("data/page1.html");
	public static final byte[] PAGE2_BYTES = readBytes("data/page2.html");
	public static final byte[][] ALL_BYTES = new byte[][] { PAGE1_BYTES, PAGE2_BYTES, };

	private static byte[] readBytes(String path) {
		try {
			return IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
