package com.rakuten.nlp;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * @author Donghun Shin | donghun.shin@rakuten.com | RIT | Rakuten Inc.
 * @since 2017/04/12
 */
public class WordScoreBuilder {
	private static final Logger LOGGER = Logger.getLogger(WordScoreBuilder.class.getName());

	public static void main(String[] args) throws IOException {
		StopwordDict stopwordDict = StopwordDict.getInstance("/Users/sindongboy/Dropbox/Documents/workspace/keyphrase-extractor/resource/stopword.jp");
		NLPAPI nlp = NLPAPI.getInstance();

		BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
		String line;
		int count = 0;
		while ((line = reader.readLine()) != null) {
			if (count % 10000 == 0) {
				LOGGER.info("processing : " + count);
			}
			count++;
			if (line.trim().length() == 0) {
				continue;
			}

			String [] sentences =  line.split("[。！]");

			for (String sentence : sentences) {
				String nlpSent = nlp.process(sentence.trim());
				System.out.println(nlpSent);
				String[] candidates = stopwordDict.replaceWithStopword(sentence).split("\\|");
			}

		}
		reader.close();
	}

}
