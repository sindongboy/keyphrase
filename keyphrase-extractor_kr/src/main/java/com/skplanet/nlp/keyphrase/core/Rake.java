package com.skplanet.nlp.keyphrase.core;

import com.skplanet.nlp.config.Configuration;
import com.skplanet.nlp.keyphrase.config.PROP;
import com.skplanet.nlp.keyphrase.data.Document;
import com.skplanet.nlp.keyphrase.util.MapUtil;
import org.apache.commons.collections.bag.HashBag;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/11/14.
 */
public final class Rake {
    private static final Logger LOGGER = Logger.getLogger(Rake.class.getName());

    // minimum word length of each word in candidates phrase , 0 by default
    //private static int MIN_WORD_LEN = 0;

    // minimum token length
    private static int MIN_TOKEN_LEN = 0;

    // stopword list
    private List<String> stopwordList = null;

    // stopword pattern
    private Pattern stopwordPattern = null;

    // vocabulary
    private HashBag vocabulary = null;

    /**
     * Default Constructor
     */
    public Rake() {
        Configuration config = Configuration.getInstance();
        try {
            config.loadProperties(PROP.RAKE_CONFIG);
            MIN_TOKEN_LEN = Integer.parseInt(config.readProperty(PROP.RAKE_CONFIG, PROP.MIN_TOKEN_LEN));
            //MIN_WORD_LEN = Integer.parseInt(config.readProperty(PROP.RAKE_CONFIG, PROP.MIN_WORD_LEN));
        } catch (IOException e) {
            LOGGER.error("failed to load configuration: " + PROP.RAKE_CONFIG);
        }
    }

    /**
     * Extract Document Keyword
     *
     * @param document single {@link com.skplanet.nlp.keyphrase.data.Document}
     * @return filtered set of words
     */
    public Map<String, Double> extract(Document document) {
        List<String> keywordCandidatesList = new ArrayList<String>();
        Map<String, Double> keywordScore = new HashMap<String, Double>();
        this.vocabulary = new HashBag();

        // ------------------------------- //
        // Generate Keyword Candidates
        // ------------------------------- //
        //int line = 0;
        /*
        for (String sentence : document.toString().split(" ")) {
            StringBuffer sb = new StringBuffer();
            String[] tokens = sentence.split(" ");
            for (int i = 0; i < tokens.length; i++) {
                if (i == tokens.length - 1) {
                    sb.append(tokens[i]);
                } else {
                    sb.append(tokens[i]).append(" ");
                }
            }

            String[] candidates = this.stopwordPattern.matcher(sb).replaceAll("|").split("\\|");
            if (candidates.length > 0) {
                for (final String candidate : candidates) {
                    String c = candidate.trim().toLowerCase();
                    if (c.length() > 0) {
                        keywordCandidatesList.add(c);
                        this.vocabulary.add(c);
                    }
                }
            }
            line++;
        }
        */
        String candidates_raw = this.stopwordPattern.matcher(document.toString()).replaceAll("|");
	    String[] candidates = this.stopwordPattern.matcher(document.toString()).replaceAll("|").split("\\|");
        if (candidates.length > 0) {
            for (final String candidate : candidates) {
                if (candidate.contains("\n")) {
                    String[] tokens = candidate.split("\\n");
                    for (String token : tokens) {
                        if (token.length() > 0) {
                            keywordCandidatesList.add(token.trim());
                            this.vocabulary.add(token.trim());
                        }
                    }
                } else {
                    String c = candidate.trim().toLowerCase();
                    if (c.length() > 0) {
                        keywordCandidatesList.add(c.trim());
                        this.vocabulary.add(c.trim());
                    }
                }
            }
        }


        // ----------------------- //
        // calculate word score
        // ----------------------- //
        Map<String, Integer> wordFrequency = new HashMap<String, Integer>();
        Map<String, Integer> wordDegree = new HashMap<String, Integer>();

        for (String candidate : keywordCandidatesList) {

            List<String> wordList = Arrays.asList(candidate.split(" "));
            int wordListDegree = wordList.size() - 1;

            for (final String word : wordList) {
                if (!wordFrequency.containsKey(word)) {
                    wordFrequency.put(word, 0);
                }

                if (!wordDegree.containsKey(word)) {
                    wordDegree.put(word, 0);
                }

                wordFrequency.put(word, wordFrequency.get(word) + 1);
                wordDegree.put(word, wordDegree.get(word) + wordListDegree);
            }
        }

        for (String word : wordFrequency.keySet()) {
            wordDegree.put(word, wordDegree.get(word) + wordFrequency.get(word));

            if (!keywordScore.containsKey(word)) {
                keywordScore.put(word, 0.0);
            }
            keywordScore.put(word, wordDegree.get(word) / (wordFrequency.get(word) * 1.0));
        }

        // --------------------------- //
        // calculate candidates score
        // --------------------------- //
        final Map<String, Double> candidateScoreMap = new HashMap<String, Double>();
        for (String candidate : keywordCandidatesList) {
            if (candidate.length() <= MIN_TOKEN_LEN) {
                continue;
            }
            final List<String> wordList = Arrays.asList(candidate.split(" "));
            double score = 0;

            for (final String word : wordList) {
                /*
                if (word.contains("무궁화")) {
                    LOGGER.debug("무궁화");
                }
                */
                if (keywordScore.get(word) != null) {
                    score += keywordScore.get(word);
                }
            }

            candidateScoreMap.put(candidate, score);
        }

        Map<String, Double> sortedCandidateScoreMap = MapUtil.sortByValue(candidateScoreMap, MapUtil.DSCENDING);
        return sortedCandidateScoreMap;
    }


    /**
     * Initialize Rake Keyword Extractor
     * @throws IOException
     */
    public void init() throws IOException {
        // get stopword resource
        Configuration config = Configuration.getInstance();
        File stopwordFile = new File(config.getResource(PROP.STOPWORD_FILE).getFile());

        // load stopword resource
        loadStopword(stopwordFile);
    }

    /**
     * Load stopword file
     * @param stopwordFile stopword file
     */
    private void loadStopword(File stopwordFile) throws IOException {
        // ---------------------- //
        // read stopword file
        // ---------------------- //
        this.stopwordList = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(stopwordFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                this.stopwordList.add(line.trim().toLowerCase());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            LOGGER.error("Stopword File not found : " + stopwordFile.getName(), e);
        } catch (IOException e) {
            LOGGER.error("Failed to read Stopword File : " + stopwordFile.getName(), e);
        } finally {
            reader.close();
        }

        // ---------------------- //
        // build stopword
        // ---------------------- //
        if (this.stopwordList.size() == 0) {
            // default stopword 가 필요하지는 않을까?
            LOGGER.warn("No Stopwords!");
        }

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
