package io.gatling.benchmark.jsonpath.parser;

import io.gatling.jsonpath.JsonPath;
import io.gatling.jsonpath.JsonPath$;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class GatlingHelper {

  private static final ConcurrentHashMap<String, JsonPath> compiledPaths = new ConcurrentHashMap<>();
  private static final Function<String, JsonPath> SUPPLIER = p -> JsonPath$.MODULE$.compile(p).right().get();

  public static JsonPath compile(String path) {
    return compiledPaths.computeIfAbsent(path, SUPPLIER);
  }
}
