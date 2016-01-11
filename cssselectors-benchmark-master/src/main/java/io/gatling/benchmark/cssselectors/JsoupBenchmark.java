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
import io.gatling.benchmark.cssselectors.JoddBenchmark.ThreadState;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.jsoup.helper.MissingFeatures;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Evaluators;
import org.jsoup.select.Selector;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;

@OutputTimeUnit(TimeUnit.SECONDS)
public class JsoupBenchmark {

  private static final Evaluator PRECOMPILED_SELECTORS1 = Evaluators.evaluator(SELECTOR1);
  private static final Evaluator PRECOMPILED_SELECTORS2 = Evaluators.evaluator(SELECTOR2);
  private static final Evaluator[] ALL_PRECOMPILED_SELECTORS = { PRECOMPILED_SELECTORS1, PRECOMPILED_SELECTORS2 };

  private Object parseStringPrecompiled(byte[] bytes, Evaluator evaluator) throws Exception {
    Document doc = Parser.parse(new String(bytes, StandardCharsets.UTF_8), "http://gatling-tool.org");
    return Selector.select(evaluator, doc);
  }

  private Object parseStreamPrecompiled(byte[] bytes, Evaluator evaluator) throws Exception {
    Document doc = MissingFeatures.load(ByteBuffer.wrap(bytes), "UTF-8", "http://gatling-tool.org");
    return Selector.select(evaluator, doc);
  }

  @Benchmark
  public Object parseStringPrecompiledRoundRobin(ThreadState state) throws Exception {
    int i = state.next();
    byte[] bytes = ALL_BYTES[i];
    Evaluator evaluator = ALL_PRECOMPILED_SELECTORS[i];
    return parseStringPrecompiled(bytes, evaluator);
  }

  @Benchmark
  public Object parseStreamPrecompiledRoundRobin(ThreadState state) throws Exception {
    int i = state.next();
    byte[] bytes = ALL_BYTES[i];
    Evaluator evaluator = ALL_PRECOMPILED_SELECTORS[i];
    return parseStreamPrecompiled(bytes, evaluator);
  }
}
