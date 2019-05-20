package io.gatling.benchmark.jsonpath.parser;

import io.gatling.benchmark.util.Bytes;
import jodd.json.JsonParser;

public class JoddHelper {

  private static final ThreadLocal<JsonParser> JSON_PARSER_THREAD_LOCAL = ThreadLocal.withInitial(JsonParser::createLazyOne);

  public static Object parseString(byte[][] chunks, String path) {
    String string = Bytes.toString(chunks);
    return GatlingHelper.compile(path).query(JSON_PARSER_THREAD_LOCAL.get().parse(string));
  }

  public static Object parseChars(byte[][] chunks, String path) {
    char[] chars = Bytes.toChars(chunks);
    return GatlingHelper.compile(path).query(JSON_PARSER_THREAD_LOCAL.get().parse(chars));
  }
}
