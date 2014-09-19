package io.gatling.benchmark.xpath;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;

import org.xml.sax.InputSource;

public class SaxonBenchmark extends AbstractXPathBenchmark {

	private static final Processor SAXON_PROCESSOR = new Processor(false);

	private static final DocumentBuilder SAXON_DOCUMENT_BUILER = SAXON_PROCESSOR.newDocumentBuilder();

	private static final Map<String, ThreadLocal<XPathSelector>> SAXON_XPATH_SELECTORS = new ConcurrentHashMap<String, ThreadLocal<XPathSelector>>();

	protected String parse(InputSource inputSource, final String path) throws Exception {
		final XPathCompiler xPathCompiler = SAXON_PROCESSOR.newXPathCompiler(); // namespaces
		ThreadLocal<XPathSelector> xPathSelectorTL = SAXON_XPATH_SELECTORS.get(path);
		if (xPathSelectorTL == null) {
			xPathSelectorTL = new ThreadLocal<XPathSelector>() {
				@Override
				protected XPathSelector initialValue() {
					try {
						XPathExecutable xPathExecutable = xPathCompiler.compile(path);
						return xPathExecutable.load(); // not threadsafe but
						                               // reusable
					} catch (SaxonApiException e) {
						throw new ExceptionInInitializerError(e);
					}
				}
			};
			SAXON_XPATH_SELECTORS.put(path, xPathSelectorTL);
		}

		Source source = new SAXSource(inputSource);
		XdmNode xdmNode = SAXON_DOCUMENT_BUILER.build(source);

		XPathSelector xPathSelector = xPathSelectorTL.get();
		try {
			xPathSelector.setContextItem(xdmNode);
			XdmValue xdmValue = xPathSelector.evaluate();

			if (xdmValue instanceof XdmAtomicValue) {
				XdmAtomicValue value = (XdmAtomicValue) xdmValue;
				return value.getStringValue();

			} else if (xdmValue instanceof XdmItem) {
				XdmItem value = (XdmItem) xdmValue;
				return value.getStringValue();

			} else if (xdmValue instanceof XdmNode) {
				XdmNode value = (XdmNode) xdmValue;
				return value.getStringValue();

			} else {
				throw new UnsupportedOperationException("Unknown type " + xdmValue.getClass().getName());
			}

		} finally {
			// only do that once you've read
			xPathSelector.getUnderlyingXPathContext().setContextItem(null);
		}
	}
}
