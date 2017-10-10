package io.gatling.benchmark.jsonpath;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.asynchttpclient.netty.util.Utf8ByteBufCharsetDecoder;

import java.nio.charset.CharacterCodingException;

public class ByteArrayUtf8Decoder {

  public static String decode(byte[] buf) throws CharacterCodingException {
    ByteBuf byteBuf = Unpooled.wrappedBuffer(buf);
    try {
      return Utf8ByteBufCharsetDecoder.decodeUtf8(byteBuf);
    } finally {
      byteBuf.release();
    }
  }

  public static String decode(byte[][] bufs) throws CharacterCodingException {
    ByteBuf[] byteBufs = new ByteBuf[bufs.length];
    for (int i = 0; i < bufs.length; i++) {
      byteBufs[i] = Unpooled.wrappedBuffer(bufs[i]);
    }

    try {
      return Utf8ByteBufCharsetDecoder.decodeUtf8(byteBufs);
    } finally {
      for (ByteBuf byteBuf: byteBufs) {
        byteBuf.release();
      }
    }
  }
}
