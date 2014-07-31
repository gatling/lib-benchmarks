package io.gatling.benchmark.xpath;

import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaxen.dom.DOMXPath;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.logic.BlackHole;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

@OutputTimeUnit(TimeUnit.SECONDS)
public class JaxenBenchmark extends AbstractXPathBenchmark {

  private static final DocumentBuilderFactory FACTORY;

  static {
    System.setProperty("org.apache.xml.dtm.DTMManager", "org.apache.xml.dtm.ref.DTMManagerDefault");
    System.setProperty("com.sun.org.apache.xml.internal.dtm.DTMManager", "com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault");
    System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
    System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
    FACTORY = DocumentBuilderFactory.newInstance();
    FACTORY.setExpandEntityReferences(false);
    FACTORY.setNamespaceAware(true);
  }

  private static final EntityResolver NOOP_ENTITY_RESOLVER = new EntityResolver() {
    public InputSource resolveEntity(String publicId, String systemId) {
      return new InputSource(new StringReader(""));
    }
  };

  public static final ThreadLocal<DocumentBuilder> DOCUMENT_BUILDER = new ThreadLocal<DocumentBuilder>() {
    protected DocumentBuilder initialValue() {
      DocumentBuilder builder;
      try {
        builder = FACTORY.newDocumentBuilder();
        builder.setEntityResolver(NOOP_ENTITY_RESOLVER);
        return builder;
      } catch (ParserConfigurationException e) {
        throw new RuntimeException(e);
      }
    };
  };

  private static final Map<String, DOMXPath> PATHS = new ConcurrentHashMap<>();

  public String parse(InputSource inputSource, final String path) throws Exception {

    Document document = DOCUMENT_BUILDER.get().parse(inputSource);

    DOMXPath domxPath = PATHS.get(path);
    if (domxPath == null) {
      domxPath = new DOMXPath(path);
      PATHS.put(path, domxPath);
    }

    Node node = (Node) domxPath.selectSingleNode(document);
    return node.getTextContent();
  }

  @GenerateMicroBenchmark
  public void parseByString(ThreadState state, BlackHole bh) throws Exception {
    super.parseByString(state, bh);
  }

  @GenerateMicroBenchmark
  public void parseByInputStreamReader(ThreadState state, BlackHole bh) throws Exception {
    super.parseByInputStreamReader(state, bh);
  }

  @GenerateMicroBenchmark
  public void parseByInputStream(ThreadState state, BlackHole bh) throws Exception {
    super.parseByInputStream(state, bh);
  }
}
