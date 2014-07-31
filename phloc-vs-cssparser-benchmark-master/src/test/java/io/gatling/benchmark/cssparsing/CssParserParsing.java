package io.gatling.benchmark.cssparsing;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;

import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

public class CssParserParsing {

	public static List<String> extractUrls(String cssString) throws IOException {

		final List<String> urls = new ArrayList<String>();

		final Reader r = new StringReader(cssString);
		final InputSource source = new InputSource(r);

		final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
		final CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);

		for (int i = 0; i < stylesheet.getCssRules().getLength(); i++) {

			CSSRule rule = stylesheet.getCssRules().item(i);

			if (rule.getType() == CSSRule.STYLE_RULE) {
				CSSStyleRuleImpl styleRule = (CSSStyleRuleImpl) rule;

				// selectors

				CSSValue background = styleRule.getStyle().getPropertyCSSValue("background-image");
				if (background == null)
					background = styleRule.getStyle().getPropertyCSSValue("background");

				if (background != null) {
					CSSValueImpl backgroundImpl = (CSSValueImpl) background;

					switch (backgroundImpl.getCssValueType()) {
					case CSSValueImpl.CSS_PRIMITIVE_VALUE:
						if (backgroundImpl.getPrimitiveType() == CSSValueImpl.CSS_URI) {
							for (int j = 0; j < styleRule.getSelectors().getLength(); j++) {
								urls.add(styleRule.getSelectors().item(j) + " " + backgroundImpl.getStringValue());
							}
						}
						break;

					case CSSValueImpl.CSS_VALUE_LIST:

						List<CSSValueImpl> values = (List<CSSValueImpl>) backgroundImpl.getValue();
						for (CSSValueImpl value : values) {
							if (value.getPrimitiveType() == CSSValueImpl.CSS_URI) {
								for (int j = 0; j < styleRule.getSelectors().getLength(); j++) {
									urls.add(styleRule.getSelectors().item(j) + " " + backgroundImpl.getStringValue());
								}
							}
						}

						break;
					}
				}
			}
		}

		return urls;
	}
}
