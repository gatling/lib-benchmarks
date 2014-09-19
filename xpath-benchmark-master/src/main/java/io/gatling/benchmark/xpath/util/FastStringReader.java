package io.gatling.benchmark.xpath.util;

import java.io.IOException;
import java.io.Reader;

public final class FastStringReader extends Reader {

  private final String str;
  private int length;
  private int next = 0;
  private int mark = 0;

  public FastStringReader(String s) {
    this.str = s;
    this.length = s.length();
  }

  private void ensureOpen() throws IOException {
    if (length == -1)
      throw new IOException("Stream closed");
  }

  @Override
  public int read() throws IOException {
    ensureOpen();
    if (next >= length)
      return -1;
    return str.charAt(next++);
  }

  @Override
  public int read(char cbuf[], int off, int len) throws IOException {
    ensureOpen();
    if (len == 0) {
      return 0;
    }
    if (next >= length)
      return -1;
    int n = Math.min(length - next, len);
    str.getChars(next, next + n, cbuf, off);
    next += n;
    return n;
  }

  @Override
  public long skip(long ns) throws IOException {
    ensureOpen();
    if (next >= length)
      return 0;
    // Bound skip by beginning and end of the source
    long n = Math.min(length - next, ns);
    n = Math.max(-next, n);
    next += n;
    return n;
  }

  @Override
  public boolean ready() throws IOException {
    ensureOpen();
    return true;
  }

  @Override
  public boolean markSupported() {
    return true;
  }

  @Override
  public void mark(int readAheadLimit) throws IOException {
    if (readAheadLimit < 0)
      throw new IllegalArgumentException("Read-ahead limit < 0");
    ensureOpen();
    mark = next;
  }

  @Override
  public void reset() throws IOException {
    ensureOpen();
    next = mark;
  }

  @Override
  public void close() {
    length = -1;
  }

  @Override
  public String toString() {
    return str;
  }
}
