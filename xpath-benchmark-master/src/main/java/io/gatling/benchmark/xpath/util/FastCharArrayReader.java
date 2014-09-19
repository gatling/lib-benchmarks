package io.gatling.benchmark.xpath.util;

import java.io.IOException;
import java.io.Reader;

public final class FastCharArrayReader extends Reader {

  protected char buf[];
  protected int pos;
  protected int markedPos = 0;
  protected int count;

  public FastCharArrayReader(char buf[]) {
    this.buf = buf;
    this.pos = 0;
    this.count = buf.length;
  }

  public FastCharArrayReader(char buf[], int offset, int length) {
    if ((offset < 0) || (offset > buf.length) || (length < 0) ||
      ((offset + length) < 0)) {
      throw new IllegalArgumentException();
    }
    this.buf = buf;
    this.pos = offset;
    this.count = Math.min(offset + length, buf.length);
    this.markedPos = offset;
  }

  private void ensureOpen() throws IOException {
    if (buf == null)
      throw new IOException("Stream closed");
  }

  public int read() throws IOException {
    ensureOpen();
    if (pos >= count)
      return -1;
    else
      return buf[pos++];
  }

  public int read(char b[], int off, int len) throws IOException {
    ensureOpen();
    if ((off < 0) || (off > b.length) || (len < 0) ||
      ((off + len) > b.length) || ((off + len) < 0)) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return 0;
    }

    if (pos >= count) {
      return -1;
    }
    if (pos + len > count) {
      len = count - pos;
    }
    if (len <= 0) {
      return 0;
    }
    System.arraycopy(buf, pos, b, off, len);
    pos += len;
    return len;
  }

  public long skip(long n) throws IOException {
    ensureOpen();
    if (pos + n > count) {
      n = count - pos;
    }
    if (n < 0) {
      return 0;
    }
    pos += n;
    return n;
  }

  public boolean ready() throws IOException {
    ensureOpen();
    return (count - pos) > 0;
  }

  public boolean markSupported() {
    return true;
  }

  public void mark(int readAheadLimit) throws IOException {
    ensureOpen();
    markedPos = pos;
  }

  public void reset() throws IOException {
    ensureOpen();
    pos = markedPos;
  }

  public void close() {
    buf = null;
  }
}
