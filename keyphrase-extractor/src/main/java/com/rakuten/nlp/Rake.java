package com.rakuten.nlp;

import com.ibm.icu.text.Transliterator;
import org.apache.commons.collections15.bag.HashBag;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Rake Implementation
 *
 * @author Donghun Shin | donghun.shin@rakuten.com | RIT | Rakuten Inc.
 * @since 2017/04/12
 */
public class Rake {

	private static final Logger LOGGER = Logger.getLogger(Rake.class.getName());

	public static final String FULL_WIDTH_TO_HALF_WIDTH = "Fullwidth-Halfwidth";
	public static final String HALF_WIDTH_TO_FULL_WIDTH = "Halfwidth-Fullwidth";

	// minimum word length of each word in candidates phrase , 0 by default
	//private static int MIN_WORD_LEN = 0;

	// minimum token length
	private static int MIN_TOKEN_LEN = 0;

	// vocabulary
	private HashBag vocabulary = null;

	// word score table path
	private String wordScorePath = null;

	// NLP API
	private NLPAPI nlp = NLPAPI.getInstance();

	// Stopword
	private StopwordDict stopword = null;

	// ============================================================================================= //

	public Rake(String stopwordPath) {
		this.stopword = StopwordDict.getInstance(stopwordPath);
	}

	public Rake(String stopwordPath, String wordScorePath) {
		this.stopword = StopwordDict.getInstance(stopwordPath);
		this.wordScorePath = wordScorePath;
		init();
	}

	public Rake(String stopwordPath, String wordScorePath, int minLen) {
		this.stopword = StopwordDict.getInstance(stopwordPath);
		this.wordScorePath = wordScorePath;
		this.MIN_TOKEN_LEN = minLen;
		init();
	}

	private void init() {
	}

	// ============================================================================================= //

	public Map<String, Double> extract() {

		return null;
	}

	public void getStatistics() {

	}

	private String widthNormalize(String text, String direction) {
		Transliterator conv = Transliterator.getInstance(direction);
		String result = conv.transliterate(text);
		LOGGER.debug("Width Normalized (" + direction + "): \"" + text + "\" ==> \"" + result);
		return result;
	}

	/**
	 * Vocabulary Bag
	 * @return Bag of Words
	 */
	public HashBag getVocabulary() {
		return this.vocabulary;
	}

	/**
	 * Get Term Frequency
	 * @return term frequency
	 */
	public int getTermFrequency(String term) {
		return this.vocabulary.getCount(term);
	}



}
