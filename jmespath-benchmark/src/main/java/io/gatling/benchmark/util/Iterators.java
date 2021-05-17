package io.gatling.benchmark.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedList;
import java.util.List;

public class Iterators {
  public static List<JsonNode> toList(scala.collection.Iterator<JsonNode> it) {
    List<JsonNode> list = new LinkedList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }
}
