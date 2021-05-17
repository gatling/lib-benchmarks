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

import java.util.concurrent.TimeUnit;

import jodd.lagarto.LagartoParserConfig;
import jodd.lagarto.dom.*;

import org.openjdk.jmh.annotations.*;

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class JoddBenchmark {

	private static final LagartoDOMBuilder LAGARTO_DOM_BUILDER;

	static {
		LagartoDomBuilderConfig config = new LagartoDomBuilderConfig();
		config.setErrorLogEnabled(false);
		config.setParserConfig(new LagartoParserConfig().setEnableConditionalComments(false).setEnableRawTextModes(false));
		LAGARTO_DOM_BUILDER = new LagartoDOMBuilder(config);
	}

	@Param({"3"})
	public int sample;

//	@Benchmark
//	public List<Node> parseStringVersionGatling() {
//		Data data = Data.DATA[sample];
//		return new NodeSelector(LAGARTO_DOM_BUILDER.parse(data.toStringVersionGatling())).select(data.joddSelectors);
//	}
//
//	@Benchmark
//	public List<Node> parseStringVersionJava() {
//		Data data = Data.DATA[sample];
//		return new NodeSelector(LAGARTO_DOM_BUILDER.parse(data.toStringVersionJava())).select(data.joddSelectors);
//	}
//
//	@Benchmark
//	public List<Node> parseCharsVersionGatling() {
//		Data data = Data.DATA[sample];
//		return new NodeSelector(LAGARTO_DOM_BUILDER.parse(data.toCharsViaStringVersionGatling())).select(data.joddSelectors);
//	}
//
//	@Benchmark
//	public List<Node> parseCharsVersionJava() {
//		Data data = Data.DATA[sample];
//		return new NodeSelector(LAGARTO_DOM_BUILDER.parse(data.toCharsViaStringVersionJava())).select(data.joddSelectors);
//	}


	@Benchmark
	public Document reference() {
		Data data = Data.DATA[sample];
		return LAGARTO_DOM_BUILDER.parse(data.string);
	}

	@Benchmark
	public Document parseStringVersionGatling() {
		Data data = Data.DATA[sample];
		return LAGARTO_DOM_BUILDER.parse(data.toStringVersionGatling());
	}

	@Benchmark
	public Document parseStringVersionJava() {
		Data data = Data.DATA[sample];
		return LAGARTO_DOM_BUILDER.parse(data.toStringVersionJava());
	}

	@Benchmark
	public Document parseCharsVersionGatling() {
		Data data = Data.DATA[sample];
		return LAGARTO_DOM_BUILDER.parse(data.toCharsViaStringVersionGatling());
	}

	@Benchmark
	public Document parseCharsVersionJava() {
		Data data = Data.DATA[sample];
		return LAGARTO_DOM_BUILDER.parse(data.toCharsViaStringVersionJava());
	}
}
