package io.gatling.benchmark.xpath;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XalanBenchmark extends AbstractXPathBenchmark {

	static {
	  System.setProperty("org.apache.xml.dtm.DTMManager", "org.apache.xml.dtm.ref.DTMManagerDefault");
	  System.setProperty("com.sun.org.apache.xml.internal.dtm.DTMManager", "com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault");
		System.setProperty("javax.xml.xpath.XPathFactory", "org.apache.xpath.jaxp.XPathFactoryImpl");
	}

	public static final ThreadLocal<XPathFactory> XPATH_FACTORY = new ThreadLocal<XPathFactory>() {
		protected XPathFactory initialValue() {
			return XPathFactory.newInstance();
		};
	};

	private static final Map<String, XPathExpression> EXPRESSIONS = new ConcurrentHashMap<>();

	public String parse(InputSource inputSource, final String path) throws Exception {

		XPathExpression expression = EXPRESSIONS.get(path);
		if (expression == null) {
			XPath xpath = XPATH_FACTORY.get().newXPath();
			expression = xpath.compile(path);
			EXPRESSIONS.put(path, expression);
		}

		Document document = JaxenBenchmark.DOCUMENT_BUILDER.get().parse(inputSource);

		return expression.evaluate(document);
	}
}
