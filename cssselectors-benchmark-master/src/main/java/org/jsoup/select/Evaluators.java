package org.jsoup.select;

public class Evaluators {

    public static Evaluator evaluator(String query) {
        return QueryParser.parse(query);
    }
}
