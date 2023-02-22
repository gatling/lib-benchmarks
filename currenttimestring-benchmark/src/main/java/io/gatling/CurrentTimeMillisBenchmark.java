package io.gatling;

import org.openjdk.jmh.annotations.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class CurrentTimeMillisBenchmark {

  private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
  private static final DateFormat UNSAFE_SIMPLE_DATE_FORMAT = new SimpleDateFormat(PATTERN);
  private static final ThreadLocal<DateFormat> SIMPLE_DATE_FORMATS = ThreadLocal.withInitial(() -> new SimpleDateFormat(PATTERN));
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

  @Benchmark
  public String simpleDateFormat() {
    return SIMPLE_DATE_FORMATS.get().format(new Date());
  }

  @Benchmark
  public String unsafeSimpleDateFormat() {
    return UNSAFE_SIMPLE_DATE_FORMAT.format(new Date());
  }

  @Benchmark
  public Object dateTimeFormatter() throws Exception {
    return DATE_TIME_FORMATTER.format(ZonedDateTime.now());
  }
}
