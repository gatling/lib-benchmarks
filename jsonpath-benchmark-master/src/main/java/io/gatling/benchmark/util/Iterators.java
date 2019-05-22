package io.gatling.benchmark.util;

import java.util.LinkedList;
import java.util.List;

public class Iterators {
  public static List<Object> toList(scala.collection.Iterator<Object> it) {
    List<Object> list = new LinkedList<>();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }
}
