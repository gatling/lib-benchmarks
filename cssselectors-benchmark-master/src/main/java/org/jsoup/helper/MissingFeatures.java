package org.jsoup.helper;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class MissingFeatures {
    public static Document load(ByteBuffer byteData, String charsetName, String baseUri) throws IOException {
        return DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
    }
}
