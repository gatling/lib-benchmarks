package io.gatling.benchmark.jsonpath;

import java.util.*;
import java.util.Map.Entry;

import static io.gatling.benchmark.util.Bytes.*;

public class Data {

  private static final int MTU = 1500;

  public static final List<AbstractMap.Entry<byte[][], String>> BYTES_AND_PATHS = new ArrayList<>();

  private static void addGoessner(Map<byte[][], String[]> map) {
    String[] goessnerPaths = {//
    "$.store.book[2].author",//
        "$..author",//
        "$.store.*",//
        "$.store..price",//
        "$..book[2].title",//
        "$..book[-1:].title",//
        "$..book[:2].title",//
        "$..*",//
        "$.store.book[*].niçôlàs['nico']['foo'][*].bar[1:-2:3]",//
        "$.store['book'][:2].title",//
        "$.store.book[?(@.isbn)].title",//
        "$.store.book[?(@.category == 'fiction')].title",//
        "$.store.book[?(@.price < 10 && @.price >4)].title" };

    map.put(split(readBytes("data/goessner.json"), MTU), goessnerPaths);
  }

  private static void addWebxml(Map<byte[][], String[]> map) {
    String[] webxmlPaths = { "$.web-app.servlet[0].init-param.dataStoreName" };

    map.put(split(readBytes("data/webxml.json"), MTU), webxmlPaths);
  }

  private static void addTwitter(Map<byte[][], String[]> map) {
    String[] twitterPaths = {//
    "$.completed_in",//
        "$.results[:3].from_user",//
        "$.results[1:9:-2].from_user",//
        "$.results[*].to_user_name",//
        "$.results[5].metadata.result_type",//
        "$.results[?(@.from_user == 'anna_gatling')]",//
        "$.results[?(@.from_user_id >= 1126180920)]" };

    map.put(split(readBytes("data/twitter.json"), MTU), twitterPaths);
  }

  private static void add20K(Map<byte[][], String[]> map) {
    String[] twentyKPaths = {//
    "$..address",//
        "$..friends..name",//
        "$..friends[?(@.id == 1)].name" };

    map.put(split(readBytes("data/20k.json"), MTU), twentyKPaths);
  }

  private static void addCitm(Map<byte[][], String[]> map) {
    String[] citmPaths = {//
    "$.events.*.name",//
        "$.performances[?(@.eventId == 339420805)]" };

    map.put(split(readBytes("data/citm_catalog.json"), MTU), citmPaths);
  }

  static {
    Map<byte[][], String[]> map = new LinkedHashMap<>();
    addGoessner(map);
    addWebxml(map);
    addTwitter(map);
    add20K(map);
//    addCitm(map);

    for (Entry<byte[][], String[]> entry : map.entrySet()) {
      for (String path : entry.getValue()) {
        BYTES_AND_PATHS.add(new AbstractMap.SimpleEntry<>(entry.getKey(), path));
      }
    }
  }
}
