package io.gatling.benchmark.jsonpath;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class GoessnerBenchmark extends JmesPathBenchmark {

  private static final JsonNode DATA = parseJson("/data/goessner.json");

  @Param({"store.book[2].author",
          "store.book[*].author",
    "store.[*]",
    "store.book[2].title",
    "store.book[:2].title",
//          "$.store.book[*].niçôlàs['nico']['foo'][*].bar[1:-2:3]",
    "store.book[?isbn].title",
    "store.book[?price < `10` && price > `4`].title", // FIXME
    "store.book[?category == 'fiction'].title",
  }
  )
  public String path;

  @Benchmark
  public JsonNode search() {
    return search(DATA, path);
  }
}
