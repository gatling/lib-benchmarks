package io.gatling.benchmark.util;

import io.gatling.netty.util.ByteBufUtils;
import io.gatling.netty.util.Utf8ByteBufCharsetDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class Bytes {

  public static byte[] readBytes(String path) {
    try {
      return IOUtils.toByteArray(Bytes.class.getClassLoader().getResourceAsStream(path));
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public static byte[][] split(byte[] full, int chunkSize) {

    int chunkNumber = (int) Math.ceil(((double) full.length) / chunkSize);
    byte[][] chunks = new byte[chunkNumber][];

    int start = 0;
    int chunkCount = 0;

    while (start < full.length) {

      int length = Math.min(full.length - start, chunkSize);
      byte[] chunk = new byte[length];
      System.arraycopy(full, start, chunk, 0, length);
      chunks[chunkCount] = chunk;
      chunkCount++;
      start += length;
    }

    return chunks;
  }

  public static InputStream toInputStream(byte[][] chunks) {
    return new ByteBufInputStream(Unpooled.wrappedBuffer(chunks), true);
  }

  public static char[] toChars(byte[][] chunks) {
    ByteBuf[] bufs = new ByteBuf[chunks.length];
    for (int i = 0; i < bufs.length; i ++) {
      bufs[i] = Unpooled.wrappedBuffer(chunks[i]);
    }
    try {
      return Utf8ByteBufCharsetDecoder.decodeUtf8Chars(bufs);
    } finally {
      for (ByteBuf buf : bufs) {
        buf.release();
      }
    }
  }

  public static String toStringVersionGatling(byte[][] chunks) {
    ByteBuf[] bufs = new ByteBuf[chunks.length];
    for (int i = 0; i < chunks.length; i++) {
      bufs[i] = Unpooled.wrappedBuffer(chunks[i]);
    }
    try {
      return Utf8ByteBufCharsetDecoder.decodeUtf8(bufs);
    } finally {
      for (ByteBuf buf: bufs) {
        buf.release();
      }
    }
  }

  public static String toStringVersionJava(byte[][] chunks) {
    int len = 0;
    for (byte[] chunk: chunks) {
      len += chunk.length;
    }
    byte[] bytes = new byte[len];
    int offset = 0;
    for (byte[] chunk: chunks) {
      System.arraycopy(chunk, 0, bytes, offset, chunk.length);
      offset += chunk.length;
    }
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
