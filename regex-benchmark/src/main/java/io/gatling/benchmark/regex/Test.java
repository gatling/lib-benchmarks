package io.gatling.benchmark.regex;

import static io.gatling.benchmark.regex.Data.ALL_BYTES;
import static io.gatling.benchmark.regex.Data.*;

public class Test {

  public static void main(String[] args) {
    for (int i = 0; i < ALL_BYTES.length; i++) {
      System.out.println(Re2jBenchmark.extractAll(Re2jBenchmark.parseString(ALL_BYTES[i], RE2J_ALL_PATTERNS[i])));
      System.out.println(Re2jBenchmark.extractAll(Re2jBenchmark.parseGatlingBytes(ALL_BYTES[i], RE2J_ALL_PATTERNS[i])));
      System.out.println(RegexBenchmark.extractAll(RegexBenchmark.parseString(ALL_BYTES[i], ALL_PATTERNS[i])));
      System.out.println(RegexBenchmark.extractAll(RegexBenchmark.parseGatlingBytes(ALL_BYTES[i], ALL_PATTERNS[i])));
    }
  }
}
