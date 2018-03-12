package io.gatling.benchmark.jsonpath.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import io.gatling.benchmark.util.Bytes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class GsonHelper {

  private static final Gson GSON = new Gson();
  private static final Type OBJECT_TYPE = TypeToken.get(Object.class).getType();

  public static Object parseString(byte[][] chunks, String path) {
    String string = Bytes.toString(chunks);
    return GatlingHelper.compile(path).query(GSON.fromJson(new JsonReader(new StringReader(string)), OBJECT_TYPE));
  }

  public static Object parseStream(byte[][] chunks, String path) {
    InputStream stream = Bytes.toInputStream(chunks);
    return GatlingHelper.compile(path).query(GSON.fromJson(new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8)), OBJECT_TYPE));
  }
}
