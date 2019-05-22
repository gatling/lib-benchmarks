package io.gatling.benchmark.jsonpath.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.benchmark.util.Bytes;
import io.gatling.benchmark.util.Iterators;

import java.io.InputStream;
import java.util.List;

public class JacksonHelper {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static List<Object> parseStream(byte[][] chunks, String path) throws Exception {
    InputStream is = Bytes.toInputStream(chunks);
    return Iterators.toList(GatlingHelper.compile(path).query(OBJECT_MAPPER.readValue(is, Object.class)));
  }
}
