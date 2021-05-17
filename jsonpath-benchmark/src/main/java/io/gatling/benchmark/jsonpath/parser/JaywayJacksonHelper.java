package io.gatling.benchmark.jsonpath.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import io.gatling.benchmark.util.Bytes;

import java.io.InputStream;

public class JaywayJacksonHelper {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final Configuration CONFIGURATION = Configuration.builder()
    .jsonProvider(new JacksonJsonProvider(OBJECT_MAPPER))
    .mappingProvider(new JacksonMappingProvider(OBJECT_MAPPER))
    .build();

  public static Object parseStream(byte[][] chunks, String path) throws Exception {
    InputStream stream = Bytes.toInputStream(chunks);
    return JaywayHelper.compile(path).read(OBJECT_MAPPER.readValue(stream, Object.class), CONFIGURATION);
  }
}
