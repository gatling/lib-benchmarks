package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.burt.jmespath.Expression;
import io.burt.jmespath.JmesPath;
import io.burt.jmespath.RuntimeConfiguration;
import io.burt.jmespath.jackson.JacksonRuntime;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class JmesPathBenchmark {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final JmesPath<JsonNode> JACKSON_RUNTIME = new JacksonRuntime(RuntimeConfiguration.defaultConfiguration(), OBJECT_MAPPER);

  private static final ConcurrentHashMap<String, Expression<JsonNode>> COMPILED_EXPRESSIONS = new ConcurrentHashMap<>();
  private static final Function<String, Expression<JsonNode>> EXPRESSION_SUPPLIER = p -> JACKSON_RUNTIME.compile(p);

  public static Expression<JsonNode> expression(String path) {
    return COMPILED_EXPRESSIONS.computeIfAbsent(path, EXPRESSION_SUPPLIER);
  }

  public static JsonNode search(JsonNode node, String path) {
    return expression(path).search(node);
  }

  protected static JsonNode parseJson(String file) {
    try (InputStream is = JmesPathBenchmark.class.getResourceAsStream(file)) {
      return OBJECT_MAPPER.readTree(is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
