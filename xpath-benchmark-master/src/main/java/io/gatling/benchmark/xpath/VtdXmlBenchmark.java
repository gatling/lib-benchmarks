package io.gatling.benchmark.xpath;

import static com.ximpleware.VTDNav.*;
import static io.gatling.benchmark.xpath.Bytes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.xml.sax.InputSource;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

@OutputTimeUnit(TimeUnit.SECONDS)
public class VtdXmlBenchmark extends AbstractXPathBenchmark {

	@Override
	protected String parse(InputSource inputSource, String path) throws Exception {
		return null;
	}

	private int getTextIndex(int index, VTDNav vn) {
		switch (vn.getTokenType(index)) {
		case TOKEN_ATTR_NAME:
			return index + 1;

		case TOKEN_STARTING_TAG:
			return vn.getText();

		case TOKEN_PI_NAME:
			if (index + 1 < vn.getTokenCount() && vn.getTokenType(index + 1) == TOKEN_PI_VAL)
				return index + 1;
			else
				return index - 1;

		default:
			throw new IllegalArgumentException("Unknown token type ");
		}
	}

	private List<String> parse(byte[] bytes, String path) throws Exception {
		VTDGen vtdEngine = new VTDGen();
		vtdEngine.setDoc(bytes);
		vtdEngine.parse(false);
		VTDNav vn = vtdEngine.getNav();
		AutoPilot ap = new AutoPilot(vn);
		ap.selectXPath(path);

		int index = ap.evalXPath();
		List<String> results = new ArrayList<>();

		while (index != -1) {
			int textIndex = getTextIndex(index, vn);
			String result = "";

			if (textIndex != -1)
				result = vn.toString(textIndex);

			if (!result.isEmpty())
				results.add(result);

			index = ap.evalXPath();
		}

		return results;
	}

	@Benchmark
	public Object parseByBytes(ThreadState state) throws Exception {
		int i = state.next();
		
		Couple<byte[][], String> c = BYTES_AND_PATHS.get(i);
		byte[][] chunks = c.left;
		String path = c.right;
		
		return parse(merge(chunks), path);
	}
}
