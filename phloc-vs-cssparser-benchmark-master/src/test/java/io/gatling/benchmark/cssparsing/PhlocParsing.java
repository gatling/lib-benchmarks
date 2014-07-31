package io.gatling.benchmark.cssparsing;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.phloc.css.ECSSVersion;
import com.phloc.css.decl.CSSDeclaration;
import com.phloc.css.decl.CSSExpressionMemberTermURI;
import com.phloc.css.decl.CSSSelector;
import com.phloc.css.decl.CSSSelectorSimpleMember;
import com.phloc.css.decl.CSSStyleRule;
import com.phloc.css.decl.CascadingStyleSheet;
import com.phloc.css.decl.ICSSSelectorMember;
import com.phloc.css.decl.ICSSTopLevelRule;
import com.phloc.css.decl.visit.CSSVisitor;
import com.phloc.css.decl.visit.DefaultCSSUrlVisitor;
import com.phloc.css.reader.CSSReader;

public class PhlocParsing {

	public static List<String> extractUrls(String cssString) {

		final List<String> urls = new ArrayList<String>();

		CascadingStyleSheet css = CSSReader.readFromString(cssString, Charset.forName("utf-8"), ECSSVersion.CSS30);

		CSSVisitor.visitCSSUrl(css, new DefaultCSSUrlVisitor() {
			// @Override
			// public void onImport(final CSSImportRule aImportRule) {
			// System.out.println("Import: " + aImportRule.getLocationString());
			// }

			@Override
			public void onUrlDeclaration(final ICSSTopLevelRule topLevelRule, final CSSDeclaration declaration, final CSSExpressionMemberTermURI uriTerm) {

				for (CSSSelector cssSelector : CSSStyleRule.class.cast(topLevelRule).getAllSelectors()) {

					for (ICSSSelectorMember member : cssSelector.getAllMembers()) {
						urls.add(CSSSelectorSimpleMember.class.cast(member).getValue() + " " + uriTerm.getURIString());
					}
				}
			}
		});

		return urls;
	}
}
