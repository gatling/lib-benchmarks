package io.gatling.benchmark.jsonpath.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.benchmark.util.Bytes;

import java.io.InputStream;

public class JaywayJacksonHelper {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static Object parseString(byte[][] chunks, String path) throws Exception {
    String string = Bytes.toString(chunks);
    return JaywayHelper.compile(path).read(OBJECT_MAPPER.readValue(string, Object.class));
  }

  public static Object parseBytes(byte[][] chunks, String path) throws Exception {
    byte[] bytes = Bytes.toBytes(chunks);
    return JaywayHelper.compile(path).read(OBJECT_MAPPER.readValue(bytes, Object.class));
  }

  public static Object parseStream(byte[][] chunks, String path) throws Exception {
    InputStream stream = Bytes.toInputStream(chunks);
    return JaywayHelper.compile(path).read(OBJECT_MAPPER.readValue(stream, Object.class));
  }
}
