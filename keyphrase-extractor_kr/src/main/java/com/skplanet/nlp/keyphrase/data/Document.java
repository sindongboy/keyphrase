package com.skplanet.nlp.keyphrase.data;

import com.skplanet.nlp.config.Configuration;
import com.skplanet.nlp.keyphrase.config.PROP;
import com.skplanet.nlp.keyphrase.util.NLPUtils;
import com.skplanet.nlp.keyphrase.util.Pair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Document Loading Class
 *
 * @author Donghun Shin, donghun.shin@sk.com
 */
public class Document {
    private static final Logger LOGGER = Logger.getLogger(Document.class.getName());

    // raw text
    private String raw = null;
    // document id
    private String documentName = null;
    // sentences
    private List<String> sentences = null;
    // nlp
    private NLPUtils nlp = NLPUtils.getInstance();
    // Stop POSTAG
    private Set<String> stoptags = null;
    // token list
    private List<Pair<String>> tokens = null;

    /**
     * Constructor
     */
    public Document() {
        // initialize
        sentences = new ArrayList<String>();
        tokens = new ArrayList<Pair<String>>();

        // load stop postags
        stoptags = new HashSet<String>();
        Configuration config = Configuration.getInstance();
        try {
            config.loadProperties(PROP.RAKE_CONFIG);
            for (String tag : config.readProperty(PROP.RAKE_CONFIG, PROP.STOPTAG).split(",")) {
                stoptags.add(tag);
            }
        } catch (IOException e) {
            LOGGER.error("failed to load configuration: " + PROP.RAKE_CONFIG);
        }
    }

    /**
     * @return the raw text
     */
    public String getRaw() {
        return raw;
    }

    /**
     * Set Input Text and make sentences
     * @param raw the raw text to set
     */
    public void setRaw(String raw) {
        this.raw = raw;
        if (sentences == null) {
            sentences = new ArrayList<String>();
        }

        // tokens
        boolean lastStatus = false;
        for (String sent : nlp.getSentences(raw)) {
            if (sent.trim().length() == 0) {
                continue;
            }
            this.sentences.add(sent);
            Pair<String>[] nlpRes = nlp.getNLPResult(sent);
            for (Pair<String> pair : nlpRes) {
                if (stoptags.contains(pair.getSecond())) {
                    if (!lastStatus) {
                        this.tokens.add(new Pair<String>("|", "|"));
                    }
                    lastStatus = true;
                } else {
                    this.tokens.add(pair);
                    lastStatus = false;
                }
            }
            if (!lastStatus) {
                this.tokens.add(new Pair<String>("|", "|"));
                lastStatus = true;
            }
        }
    }

    /**
     * @return the sentences
     */
    public List<String> getSentences() {
        return sentences;
    }

    /**
     * @return the documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * @param documentName the documentName to set
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p/>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        String result = "";
        for (Pair<String> pair : tokens) {
            if (pair.getSecond().equals("|")) {
                result = result.trim();
                result = result + pair.getSecond();
            } else {
                result = result + pair.getFirst() + " ";
            }
        }
        return result;
    }
}
