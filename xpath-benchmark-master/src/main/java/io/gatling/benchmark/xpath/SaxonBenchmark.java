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

	private static final DocumentBuilder SAXON_DOCUMENT_BUILDER = SAXON_PROCESSOR.newDocumentBuilder();

	// FIXME, if we were using namespaces, we'd have to have one XPathCompiler per namespace list
	private static final XPathCompiler xPathCompiler = SAXON_PROCESSOR.newXPathCompiler(); // namespaces

	private static final Map<String, ThreadLocal<XPathSelector>> SAXON_XPATH_SELECTORS = new ConcurrentHashMap<>();


	private XPathSelector getSelector(final String path) {
		ThreadLocal<XPathSelector> xPathSelectorTL = SAXON_XPATH_SELECTORS.get(path);
		if (xPathSelectorTL == null) {
			xPathSelectorTL = SAXON_XPATH_SELECTORS.computeIfAbsent(path, p ->
				ThreadLocal.withInitial(() -> {
					try {
						XPathExecutable xPathExecutable = xPathCompiler.compile(path);
						return xPathExecutable.load(); // not threadsafe but reusable
					} catch (SaxonApiException e) {
						throw new ExceptionInInitializerError(e);
					}
				})
			);
		}
		return xPathSelectorTL.get();
	}

	protected String parse(InputSource inputSource, final String path) throws Exception {

		Source source = new SAXSource(inputSource);
		XdmNode xdmNode = SAXON_DOCUMENT_BUILDER.build(source);

		XPathSelector xPathSelector = getSelector(path);
		try {
			xPathSelector.setContextItem(xdmNode);
			XdmValue xdmValue = xPathSelector.evaluate();

			if (xdmValue instanceof XdmAtomicValue) {
				XdmAtomicValue value = (XdmAtomicValue) xdmValue;
				return value.getStringValue();

			} else if (xdmValue instanceof XdmItem) {
				XdmItem value = (XdmItem) xdmValue;
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
