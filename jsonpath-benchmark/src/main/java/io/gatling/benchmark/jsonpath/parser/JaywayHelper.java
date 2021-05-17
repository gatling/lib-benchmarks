package io.gatling.benchmark.jsonpath.parser;

import com.jayway.jsonpath.JsonPath;

import java.util.concurrent.ConcurrentHashMap;

public class JaywayHelper {

  private static final ConcurrentHashMap<String, JsonPath> compiledPaths = new ConcurrentHashMap<>();

  public static JsonPath compile(String path) {
    return compiledPaths.computeIfAbsent(path, JsonPath::compile);
  }
}
