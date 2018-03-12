package io.gatling.benchmark.jsonpath.parser;

import io.advantageous.boon.json.implementation.JsonFastParser;
import io.gatling.benchmark.util.Bytes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BoonHelper {

  static {
    if (!System.getProperty("java.version").startsWith("1.8")) {
      System.setProperty("io.advantageous.boon.faststringutils.disable", "true");
    }
  }

  private static JsonFastParser newLazyChopParser() {
    JsonFastParser jsonParser = new JsonFastParser(false, false, true, false);
    jsonParser.setCharset(StandardCharsets.UTF_8);
    return jsonParser;
  }

  public static Object parseChars(byte[][] chunks, String path) {
    char[] chars = Bytes.toChars(chunks);
    Object json = newLazyChopParser().parse(chars);
    return GatlingHelper.compile(path).query(json);
  }

  public static Object parseStream(byte[][] chunks, String path) {
    InputStream stream = Bytes.toInputStream(chunks);
    Object json = newLazyChopParser().parse(stream, StandardCharsets.UTF_8);
    return GatlingHelper.compile(path).query(json);
  }
}
