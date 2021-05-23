package io.gatling.benchmark.regex;

import java.util.regex.Pattern;

import static io.gatling.benchmark.util.Bytes.*;

public class Data {
  public static final Pattern PAGE1_PATTERN = Pattern.compile("<a href=\"([^\"]+)\">MacBook Pro</a>");
  public static final Pattern PAGE2_PATTERN = Pattern.compile("(?s)<input\\s+type=\"hidden\"\\s+name=\"_token\"\\s+value=\"([^\"]+)");
  public static final Pattern[] ALL_PATTERNS = new Pattern[]{PAGE1_PATTERN, PAGE2_PATTERN};

  public static final com.google.re2j.Pattern RE2J_PAGE1_PATTERN = com.google.re2j.Pattern.compile("<a href=\"([^\"]+)\">MacBook Pro</a>");
  public static final com.google.re2j.Pattern RE2J_PAGE2_PATTERN = com.google.re2j.Pattern.compile("(?s)<input\\s+type=\"hidden\"\\s+name=\"_token\"\\s+value=\"([^\"]+)");
  public static final com.google.re2j.Pattern[] RE2J_ALL_PATTERNS = new com.google.re2j.Pattern[]{RE2J_PAGE1_PATTERN, RE2J_PAGE2_PATTERN};

  public static final byte[][] PAGE1_BYTES = split(readBytes("data/page1.html"), MTU);
  public static final byte[][] PAGE2_BYTES = split(readBytes("data/page2.html"), MTU);
  public static final byte[][][] ALL_BYTES = new byte[][][]{PAGE1_BYTES, PAGE2_BYTES,};
}
