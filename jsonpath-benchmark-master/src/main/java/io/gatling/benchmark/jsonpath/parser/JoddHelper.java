package io.gatling.benchmark.jsonpath.parser;

import io.gatling.benchmark.util.Bytes;
import io.gatling.benchmark.util.Iterators;
import jodd.json.JsonParser;

import java.util.List;

public class JoddHelper {

  private static final ThreadLocal<JsonParser> JSON_PARSER_THREAD_LOCAL = ThreadLocal.withInitial(JsonParser::create);

  public static List<Object> parseString(byte[][] chunks, String path) {
    String string = Bytes.toString(chunks);
    return Iterators.toList(GatlingHelper.compile(path).query(JSON_PARSER_THREAD_LOCAL.get().parse(string)));
  }
}
