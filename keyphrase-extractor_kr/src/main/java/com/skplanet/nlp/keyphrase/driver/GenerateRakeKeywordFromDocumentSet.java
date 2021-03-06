package com.skplanet.nlp.keyphrase.driver;

import com.skplanet.nlp.cli.CommandLineInterface;
import com.skplanet.nlp.keyphrase.core.Rake;
import com.skplanet.nlp.keyphrase.data.Document;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Generate RAKE Keyword
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 8/11/14.
 */
public final class GenerateRakeKeywordFromDocumentSet {
    private static final Logger LOGGER = Logger.getLogger(GenerateRakeKeywordFromDocumentSet.class.getName());

    private GenerateRakeKeywordFromDocumentSet() {
    }

    /**
     * Main Method
     * @param args argument list
     */
    public static void main(String[] args) throws IOException {
        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("i", "input", true, "input document", true);
        cli.addOption("o", "output", true, "output path", true);
        cli.addOption("v", "verbose", false, "verbose mode", false);
        cli.addOption("k", "keep-freq", false, "keep frequency", false);
        cli.parseOptions(args);

        long bTime, eTime;

        File documents = new File(cli.getOption("i"));
        String outputPath = cli.getOption("o");

        for( File doc : documents.listFiles() ) {

            BufferedReader reader = new BufferedReader(new FileReader(doc));
            Document document = new Document();

            // document loading
            LOGGER.info("loading document ....");
            bTime = System.currentTimeMillis();
            char[] cbuf = new char[(int) doc.length()];
            String sbuf;
            while (!reader.ready()) {  }
            reader.read(cbuf);
            reader.close();
            sbuf = String.valueOf(cbuf);
            if (sbuf.trim().length() == 0)
                continue;
            document.setRaw(sbuf);
            document.setDocumentName(doc.getName());
            eTime = System.currentTimeMillis();
            LOGGER.info("loading document done : " + (double)(eTime - bTime) / (double) 1000 + " sec.");

            // get keywords
            Rake rake = new Rake();
            rake.init();
            LOGGER.info("processing document ....");
            bTime = System.currentTimeMillis();
            Map<String, Double> keywords = rake.extract(document);
            eTime = System.currentTimeMillis();
            LOGGER.info("processing document done : " + (double)((eTime - bTime)) / (double)1000 + " sec.");

            // print out
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + "/" + doc.getName()));
            Iterator<?> iter = keywords.entrySet().iterator();
            while (iter.hasNext()) {
                @SuppressWarnings("rawtypes")
                Map.Entry entry = (Map.Entry) iter.next();
                String term = (String) entry.getKey();
	            String termLRS = LRS.find(term);
	            if (termLRS.trim().length() > 0) {
		            term = termLRS;
	            }

	            int iterCount = 1;
                if (cli.hasOption("k")) {
                    iterCount = rake.getTermFrequency(term);
                }
                double score = (double) entry.getValue();
                for (int i = 0; i < iterCount; i++) {
                    writer.write(term + " ");
                }
                if (cli.hasOption("v")) {
                    writer.write(" ==> " + score);
                }
                writer.newLine();
            }
            writer.close();
        }

    }
}
