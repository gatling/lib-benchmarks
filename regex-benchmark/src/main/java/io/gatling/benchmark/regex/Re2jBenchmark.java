/*
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package io.gatling.benchmark.regex;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import io.gatling.benchmark.util.UnsafeUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.gatling.benchmark.regex.Bytes.ALL_BYTES;
import static io.gatling.benchmark.regex.Bytes.RE2J_ALL_PATTERNS;

@OutputTimeUnit(TimeUnit.SECONDS)
public class Re2jBenchmark {

	@State(Scope.Thread)
	public static class ThreadState {
		private int i = -1;

		public int next() {
			i++;
			if (i == ALL_BYTES.length)
				i = 0;
			return i;
		}
	}

  private List<String> extractAll(Matcher matcher) {
    List<String> res = new ArrayList<>();
    while (matcher.find())
      res.add(matcher.group(0));
    return res;
  }

	private Matcher parseString(byte[] bytes, Pattern pattern) throws Exception {
		String text = new String(bytes, StandardCharsets.UTF_8);
		return pattern.matcher(text);
	}

	private Matcher parseFastCharSequence(byte[] bytes, Pattern pattern) throws Exception {
		String text = new String(bytes, StandardCharsets.UTF_8);
		return pattern.matcher(UnsafeUtil.newFastCharSequence(text));
	}

	private Matcher parseCharBuffer(byte[] bytes, Pattern pattern) throws Exception {
		CharBuffer buffer = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bytes));
		return pattern.matcher(buffer);
	}

	@Benchmark
	public List<String> parseString(ThreadState state) throws Exception {
		int i = state.next();
		return extractAll(parseString(ALL_BYTES[i], RE2J_ALL_PATTERNS[i]));
	}

	@Benchmark
	public List<String> parseFastCharSequence(ThreadState state) throws Exception {
		int i = state.next();
		return extractAll(parseFastCharSequence(ALL_BYTES[i], RE2J_ALL_PATTERNS[i]));
	}

	@Benchmark
	public List<String> parseCharBuffer(ThreadState state) throws Exception {
		int i = state.next();
		return extractAll(parseCharBuffer(ALL_BYTES[i], RE2J_ALL_PATTERNS[i]));
	}
}
