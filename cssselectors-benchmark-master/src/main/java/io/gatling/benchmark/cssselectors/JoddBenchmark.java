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

package io.gatling.benchmark.cssselectors;

import static io.gatling.benchmark.cssselectors.Bytes.*;
import io.gatling.benchmark.util.UnsafeUtil;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jodd.csselly.CSSelly;
import jodd.csselly.CssSelector;
import jodd.lagarto.dom.LagartoDOMBuilder;
import jodd.lagarto.dom.Node;
import jodd.lagarto.dom.NodeSelector;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@OutputTimeUnit(TimeUnit.SECONDS)
public class JoddBenchmark {

	private static final LagartoDOMBuilder LAGARTO_DOM_BUILDER = new LagartoDOMBuilder();
	private static final Collection<List<CssSelector>> PRECOMPILED_SELECTORS1 = CSSelly.parse(SELECTOR1);
	private static final Collection<List<CssSelector>> PRECOMPILED_SELECTORS2 = CSSelly.parse(SELECTOR2);
	private static final Collection[] ALL_PRECOMPILED_SELECTORS = { PRECOMPILED_SELECTORS1, PRECOMPILED_SELECTORS2 };

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

	@Benchmark
	public List<Node> parsePrecompiledRoundRobin(ThreadState state) throws Exception {
		int i = state.next();
		byte[] bytes = ALL_BYTES[i];
		Collection<List<CssSelector>> selectors = (Collection<List<CssSelector>>) ALL_PRECOMPILED_SELECTORS[i];
		String text = ByteArrayUtf8Decoder.decode(bytes);
		char[] chars = UnsafeUtil.getChars(text);
		NodeSelector nodeSelector = new NodeSelector(LAGARTO_DOM_BUILDER.parse(chars));

		return nodeSelector.select(selectors);
	}
}
