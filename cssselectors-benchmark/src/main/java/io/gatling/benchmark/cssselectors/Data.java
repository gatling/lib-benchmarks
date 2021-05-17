package io.gatling.benchmark.cssselectors;

import io.gatling.benchmark.util.Bytes;
import jodd.csselly.CSSelly;
import jodd.csselly.CssSelector;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Data {

  public static final Data[] DATA = new Data[]{
    new Data("data/page1.html", ".article a[href*=api]"),
    new Data("data/page2.html", ".article a[href*=api]"),
    new Data("data/page3.html", "div.content h4"),
    new Data("data/page4.html", "h3.media__title")
  };

  final String string;
  private final byte[][] chunks;
  public final List<List<CssSelector>> joddSelectors;
  public final Evaluator jsoupEvaluator;


  private Data(String path, String selector) {
    byte[] bytes = Bytes.readBytes(path);
    this.string = new String(bytes, StandardCharsets.UTF_8);
    this.chunks = Bytes.split(bytes, 1500);
    joddSelectors = CSSelly.parse(selector);
    jsoupEvaluator = QueryParser.parse(selector);
  }

  public String toStringVersionGatling() {
    return Bytes.toStringVersionGatling(chunks);
  }

  public char[] toCharsViaStringVersionGatling() {
    return Bytes.toStringVersionGatling(chunks).toCharArray();
  }

  public String toStringVersionJava() {
    return Bytes.toStringVersionJava(chunks);
  }

  public char[] toCharsViaStringVersionJava() {
    return Bytes.toStringVersionJava(chunks).toCharArray();
  }

  public char[] toCharsDirect() {
    return Bytes.toChars(chunks);
  }

  public InputStream toInputStream() {
    return Bytes.toInputStream(chunks);
  }
}
