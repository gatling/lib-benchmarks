package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import io.gatling.benchmark.util.Iterators;
import io.gatling.jsonpath.JsonPath;
import io.gatling.jsonpath.JsonPath$;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class JsonPathBenchmark {

  private static final ConcurrentHashMap<String, JsonPath> compiledPaths = new ConcurrentHashMap<>();
  private static final Function<String, JsonPath> SUPPLIER = p -> JsonPath$.MODULE$.compile(p).right().get();

  public static JsonPath compile(String path) {
    return compiledPaths.computeIfAbsent(path, SUPPLIER);
  }

  public static List<JsonNode> search(JsonNode node, String path) {
    return Iterators.toList(compile(path).query(node));
  }
}
