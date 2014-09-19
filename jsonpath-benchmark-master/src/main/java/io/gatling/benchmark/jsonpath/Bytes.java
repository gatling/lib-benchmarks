package io.gatling.benchmark.jsonpath;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

public class Bytes {

  public static final List<Couple<byte[][], String>> BYTES_AND_PATHS = new ArrayList<>();

  private static byte[] readBytes(String path) {
    try {
      return IOUtils.toByteArray(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public static byte[] merge(byte[][] chunks) {

    int length = 0;
    for (byte[] chunk : chunks) {
      length += chunk.length;
    }

    byte[] full = new byte[length];

    int position = 0;
    for (byte[] chunk : chunks) {
      System.arraycopy(chunk, 0, full, position, chunk.length);
      position += chunk.length;
    }

    return full;
  }

  public static InputStream stream(final byte[][] chunks) {

    return new InputStream() {

      private int currentChunkNumber = 0;
      private byte[] currentChunk = chunks[currentChunkNumber];
      private int currentChunkPosition = 0;
      private boolean eof;

      @Override
      public int read() throws IOException {

        if (eof) {
          System.err.println("!!eof");
          return -1;
        }

        int b = currentChunk[currentChunkPosition];
        if (++currentChunkPosition == currentChunk.length) {
          forwardChunk();
        }

        return b;
      }

      private void forwardChunk() {
        if (++currentChunkNumber == chunks.length) {
          currentChunk = null;
          eof = true;
        } else {
          currentChunk = chunks[currentChunkNumber];
          currentChunkPosition = 0;
        }
      }

      @Override
      public int read(byte b[], int off, int len) throws IOException {

        if (eof)
          return -1;

        int maxBytesToWrite = Math.min(b.length - off, len);

        int bytesWritten = 0;

        while (bytesWritten < maxBytesToWrite) {

          int bytesLeftInCurrentChunk = currentChunk.length - currentChunkPosition;
          int maxBytesLeftToWrite = maxBytesToWrite - bytesWritten;
          if (bytesLeftInCurrentChunk <= maxBytesLeftToWrite) {
            System.arraycopy(currentChunk, currentChunkPosition, b, off, bytesLeftInCurrentChunk);
            bytesWritten += bytesLeftInCurrentChunk;
            off += bytesLeftInCurrentChunk;
            forwardChunk();

            if (eof)
              break;

          } else {
            System.arraycopy(currentChunk, currentChunkPosition, b, off, maxBytesLeftToWrite);
            currentChunkPosition += maxBytesLeftToWrite;
            bytesWritten += maxBytesLeftToWrite;
            off += maxBytesLeftToWrite;
          }
        }

        return bytesWritten;
      }
    };
  }

  private static final byte[][] split(byte[] full) {

    int chunkNumber = (int) Math.ceil(full.length / 2048.0);
    byte[][] chunks = new byte[chunkNumber][];

    int start = 0;
    int chunkCount = 0;

    while (start < full.length) {

      int length = Math.min(full.length - start, 2048);
      byte[] chunk = new byte[length];
      System.arraycopy(full, start, chunk, 0, length);
      chunks[chunkCount] = chunk;
      chunkCount++;
      start += length;
    }

    return chunks;
  }

  private static void addGoessner(Map<byte[][], String[]> map) {
    String[] goessnerPaths = {//
    "$.store.book[2].author",//
        "$..author",//
        "$.store.*",//
        "$.store..price",//
        "$..book[2].title",//
        "$..book[-1:].title",//
        "$..book[:2].title",//
        "$..*",//
        "$.store.book[*].niçôlàs['nico']['foo'][*].bar[1:-2:3]",//
        "$.store['book'][:2].title",//
        "$.store.book[?(@.isbn)].title",//
        "$.store.book[?(@.category == 'fiction')].title",//
        "$.store.book[?(@.price < 10 && @.price >4)].title" };

    map.put(split(readBytes("data/goessner.json")), goessnerPaths);
  }

  private static void addWebxml(Map<byte[][], String[]> map) {
    String[] webxmlPaths = { "$.web-app.servlet[0].init-param.dataStoreName" };

    map.put(split(readBytes("data/webxml.json")), webxmlPaths);
  }

  private static void addTwitter(Map<byte[][], String[]> map) {
    String[] twitterPaths = {//
    "$.completed_in",//
        "$.results[:3].from_user",//
        "$.results[1:9:-2].from_user",//
        "$.results[*].to_user_name",//
        "$.results[5].metadata.result_type",//
        "$.results[?(@.from_user == 'anna_gatling')]",//
        "$.results[?(@.from_user_id >= 1126180920)]" };

    map.put(split(readBytes("data/twitter.json")), twitterPaths);
  }

  private static void add20K(Map<byte[][], String[]> map) {
    String[] twentyKPaths = {//
    "$..address",//
        "$..friends..name",//
        "$..friends[?(@.id == 1)].name" };

    map.put(split(readBytes("data/20k.json")), twentyKPaths);
  }

  private static void addCitm(Map<byte[][], String[]> map) {
    String[] citmPaths = {//
    "$.events.*.name",//
        "$.performances[?(@.eventId == 339420805)]" };

    map.put(split(readBytes("data/citm_catalog.json")), citmPaths);
  }

  static {
    Map<byte[][], String[]> map = new LinkedHashMap<>();
//    addGoessner(map);
//    addWebxml(map);
//    addTwitter(map);
//    add20K(map);
    addCitm(map);

    for (Entry<byte[][], String[]> entry : map.entrySet()) {
      for (String path : entry.getValue()) {
        BYTES_AND_PATHS.add(new Couple<>(entry.getKey(), path));
      }
    }
  }
}
