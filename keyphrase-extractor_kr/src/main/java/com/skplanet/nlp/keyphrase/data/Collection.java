package com.skplanet.nlp.keyphrase.data;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Document Collection class
 *
 * @author Donghun Shin, donghun.shin@sk.com
 */
@SuppressWarnings("unused")
public class Collection {
    private static final Logger LOGGER = Logger.getLogger(Collection.class.getName());

    private Map<String, Document> documents = null;

    /**
     * Constructor
     */
    public Collection() {
        this.documents = new HashMap<String, Document>();
    }

    /**
     * Add Single Document
     *
     * @param documentName Document Name
     * @param document     Document Content
     */
    public void addDocument(String documentName, Document document) {
        this.documents.put(documentName, document);
    }

    /**
     * Get Document IDs
     *
     * @return set of document id
     */
    public Set<String> getDocumentKeySet() {
        return this.documents.keySet();
    }

    /**
     * Get Document
     *
     * @param id document id
     * @return document text
     */
    public Document getDocument(String id) {
        return this.documents.get(id);
    }
}
