package io.gatling.benchmark.xpath;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JdkBenchmark extends AbstractXPathBenchmark {

    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();

    static {
        FACTORY.setExpandEntityReferences(false);
        FACTORY.setNamespaceAware(true);
    }

    private static final EntityResolver NOOP_ENTITY_RESOLVER = (publicId, systemId) -> new InputSource(new StringReader(""));

    static final ThreadLocal<DocumentBuilder> DOCUMENT_BUILDER = ThreadLocal.withInitial(() -> {
        try {
            DocumentBuilder builder = FACTORY.newDocumentBuilder();
            builder.setEntityResolver(NOOP_ENTITY_RESOLVER);
            return builder;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    });

    private static final ThreadLocal<XPathFactory> XPATH_FACTORY = ThreadLocal.withInitial(() -> XPathFactory.newInstance());

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
        Document document = DOCUMENT_BUILDER.get().parse(inputSource);
        return compilePath(path).evaluate(document);
    }
}
