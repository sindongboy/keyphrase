package com.rakuten.nlp;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Stopword dictionary
 * @author Donghun Shin | donghun.shin@rakuten.com | RIT | Rakuten Inc.
 * @since 2017/04/12
 */
public class StopwordDict {
	private static final Logger LOGGER = Logger.getLogger(StopwordDict.class.getName());
	private static StopwordDict instance = null;

	// stopword list
	private List<String> stopwordList = null;

	// stopword pattern
	private Pattern stopwordPattern = null;

	// stopword Path
	private String stopwordPath = null;


	public static StopwordDict getInstance(String stopwordPath) {
		if (instance == null) {
			synchronized (StopwordDict.class) {
				if (instance == null) {
					instance = new StopwordDict(stopwordPath);
				}
			}
		}
		return instance;
	}

	protected StopwordDict(String stopwordPath) {
		this.stopwordPath = stopwordPath;

		LOGGER.info("loading stopwords start");
		// load stopword
		try {
			loadStopword();
		} catch (IOException e) {
			LOGGER.error("failed to load stopword dict: " + this.stopwordPath, e);
		}
		LOGGER.info("loading stopwords end");

		// build stopword
		LOGGER.info("building stopwords start");
		buildStopword();
		LOGGER.info("building stopwords end");
	}



	private void buildStopword() {
		final StringBuilder stopWordPatternBuilder = new StringBuilder();
		int count = 0;
		for (final String stopWord : this.stopwordList) {
			if (count++ != 0) {
				stopWordPatternBuilder.append("|");
			}
			stopWordPatternBuilder.append("\\b").append(stopWord).append("\\b");
		}

		// regex compile
		this.stopwordPattern = Pattern.compile(stopWordPatternBuilder.toString(), Pattern.CASE_INSENSITIVE);
	}

	private void loadStopword() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(this.stopwordPath)));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.trim().length() == 0 || line.startsWith("#")) {
				continue;
			}
			if (this.stopwordList == null) {
				this.stopwordList = new ArrayList<String>();
			}
			this.stopwordList.add(line.trim().toLowerCase());
		}
		reader.close();
	}

	public String getStopwordPattern() {
		return this.stopwordPattern.toString();
	}


	public String replaceWithStopword(String sentence) {
		return this.stopwordPattern.matcher(sentence).replaceAll("|");
	}

	public static void main(String[] args) {
		StopwordDict stopwordDict = StopwordDict.getInstance("/Users/sindongboy/Dropbox/Documents/workspace/keyphrase-extractor/resource/stopword.jp");
		System.out.println(stopwordDict.getStopwordPattern());
	}


}
