package io.gatling.benchmark.util;

import io.gatling.netty.util.Utf8ByteBufCharsetDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public final class GatlingBytes {

    private GatlingBytes() {
    }

    public static String toUtf8String(byte[] chunks) {
        ByteBuf buf = Unpooled.wrappedBuffer(chunks);
        try {
            return Utf8ByteBufCharsetDecoder.decodeUtf8(buf);
        } finally {
            buf.release();
        }
    }

    public static String toUtf8String(byte[][] chunks) {
        ByteBuf buf = Unpooled.wrappedBuffer(chunks);
        try {
            return Utf8ByteBufCharsetDecoder.decodeUtf8(buf);
        } finally {
            buf.release();
        }
    }
}
