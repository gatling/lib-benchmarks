package io.gatling.benchmark.xpath;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JdkBenchmark extends AbstractXPathBenchmark {

    static {
        System.setProperty("org.apache.xml.dtm.DTMManager", "org.apache.xml.dtm.ref.DTMManagerDefault");
        System.setProperty("com.sun.org.apache.xml.internal.dtm.DTMManager", "com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault");
        System.setProperty("javax.xml.xpath.XPathFactory", "com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl");
    }

    public static final ThreadLocal<XPathFactory> XPATH_FACTORY = new ThreadLocal<XPathFactory>() {
        protected XPathFactory initialValue() {
            return XPathFactory.newInstance();
        }
    };

    private static final Map<String, XPathExpression> EXPRESSIONS = new ConcurrentHashMap<>();

    private XPathExpression compilePath(final String path) {
        XPathExpression expression = EXPRESSIONS.get(path);
        if (expression == null) {
            expression = EXPRESSIONS.computeIfAbsent(path, p -> {
                XPath xpath = XPATH_FACTORY.get().newXPath();
                try {
                    return xpath.compile(p);
                } catch (XPathExpressionException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return expression;
    }

    public String parse(InputSource inputSource, final String path) throws Exception {
        Document document = JaxenBenchmark.DOCUMENT_BUILDER.get().parse(inputSource);
        return compilePath(path).evaluate(document);
    }
}
