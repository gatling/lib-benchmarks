package org.jsoup.helper;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class MissingFeatures {
    public static Document load(InputStream is, String charsetName, String baseUri) throws IOException {
        return DataUtil.parseInputStream(is, charsetName, baseUri, Parser.htmlParser());
    }
}
