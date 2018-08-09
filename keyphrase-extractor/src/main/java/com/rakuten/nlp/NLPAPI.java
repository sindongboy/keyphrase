package com.rakuten.nlp;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import java.util.List;

/**
 * KUROMOJI Tokenizer Wrapper
 *
 * @author Donghun Shin | donghun.shin@rakuten.com | RIT | Rakuten Inc.
 * @since 2017/04/12
 */
public class NLPAPI {
	private static NLPAPI instance = null;

	private Tokenizer tokenizer = new Tokenizer();

	public static NLPAPI getInstance() {
		if (instance == null) {
			synchronized (NLPAPI.class) {
				if (instance == null) {
					instance = new NLPAPI();
				}
			}
		}
		return instance;
	}

	protected NLPAPI() {
	}

	public String process(String sententce) {
		List<Token> tokens = tokenizer.tokenize(sententce);
		StringBuffer sb = new StringBuffer();
		for (Token token : tokens) {
			int posid = getPartOfSpeechID(token.getAllFeaturesArray());
			String surface = token.getSurface();

			if (tokenFilter(surface, posid)) {
				sb.append(surface.toLowerCase() + "_" + posid).append(" ");
			}
		}
		return sb.toString().trim();
	}

	private boolean tokenFilter(String surface, int posid) {
		if (posid < 10) {
			return false;
		}
		return true;
	}

	private int getPartOfSpeechID(String[] features) {
		String appended = features[0] + "," +
				features[1] + "," +
				features[2] + "," +
				features[3]
				;
		return POSIDMap.getPOSID(appended);
	}

}
