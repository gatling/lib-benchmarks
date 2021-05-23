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

import static io.gatling.benchmark.regex.Data.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.gatling.benchmark.util.Bytes;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@OutputTimeUnit(TimeUnit.SECONDS)
public class RegexBenchmark {

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

  static List<String> extractAll(Matcher matcher) {
    List<String> res = new ArrayList<>();
    while (matcher.find())
      res.add(matcher.group(1));
    return res;
  }

	static Matcher parseString(byte[][] chunks, Pattern pattern) {
		String text = new String(Bytes.sumChunks(chunks), StandardCharsets.UTF_8);
		return pattern.matcher(text);
	}

	static Matcher parseGatlingBytes(byte[][] chunks, Pattern pattern) {
		String text = Bytes.toString(chunks);
		return pattern.matcher(text);
	}

	@Benchmark
	public List<String> parseString(ThreadState state) {
		int i = state.next();
		return extractAll(parseString(ALL_BYTES[i], ALL_PATTERNS[i]));
	}

	@Benchmark
	public List<String> parseGatling(Re2jBenchmark.ThreadState state) {
		int i = state.next();
		return extractAll(parseGatlingBytes(ALL_BYTES[i], ALL_PATTERNS[i]));
	}
}
