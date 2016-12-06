package io.gatling.benchmark.cssselectors;

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
}
