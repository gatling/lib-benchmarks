package io.gatling.benchmark.xpath;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Bytes {

	public static final List<Couple<byte[][], String>> BYTES_AND_PATHS = new ArrayList<>();

	static {

		BYTES_AND_PATHS.add(new Couple<byte[][], String>(split(readBytes("data/text1.xml")), "/test/store/book[3]/author"));
		BYTES_AND_PATHS.add(new Couple<byte[][], String>(split(readBytes("data/text2.xml")), "//id[@id = 'id13']/distance"));
	}

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
}
