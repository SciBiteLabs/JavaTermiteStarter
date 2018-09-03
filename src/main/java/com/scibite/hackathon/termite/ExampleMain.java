package com.scibite.hackathon.termite;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.scibite.termitej.common.hit.THit;
import org.apache.http.HttpResponse;

import java.io.File;
import java.util.List;

public class ExampleMain {

    private static final String TERMITE_API_ENDPOINT = "https://109.74.206.246/termite";

    /**
     * curl -k -u hacker:g347regdsuxSW https://109.74.206.246/termite/toolkit/index.html
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        getTermiteHitsFromString("viagra and aspirin inhibits");
        //getTermiteHitsFromBinary(new File("./src/main/resources/termiteTestFile.txt"));
        //getTermiteHitsFromPubMed(new File("./src/main/resources/pubmed_result_100_pc.xml"));
    }

    private static List<THit> getTermiteHitsFromString(String inputData) throws Exception {
        TermiteHackathonClient client = new TermiteHackathonClient();

        //client.setHost("http://localhost:9090/termite");
        client.setHost(TERMITE_API_ENDPOINT);

        // Set some text
        client.setParameter(TermiteHackathonClient.PARAM_TEXT, inputData);
        client.setParameter(TermiteHackathonClient.PARAM_OUTPUT_FORMAT, "json");

        HttpResponse r = client.execute();
        String json = TermiteHackathonClient.getContent(r);

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        List<THit> hits =  JsonPath.read(document, "$.RESP_PAYLOAD.DRUG");
        System.out.println("Hits: " + hits);
        return hits;
    }

    private static List<THit> getTermiteHitsFromBinary(File testFile) throws Exception {
        TermiteHackathonClient cl = new TermiteHackathonClient();

        //cl.setHost("http://localhost:9090/termite");
        cl.setHost(TERMITE_API_ENDPOINT);

        // Set some text
        cl.addFile(testFile);
        cl.setParameter(TermiteHackathonClient.PARAM_OUTPUT_FORMAT, "json");

        HttpResponse r = cl.execute();
        String json = TermiteHackathonClient.getContent(r);

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        List<THit> hits =  JsonPath.read(document, "$.RESP_PAYLOAD.DRUG");
        System.out.println("hits: " + hits);
        return hits;
    }

    private static List<THit> getTermiteHitsFromPubMed(File pubMedFile) throws Exception {
        // Create client
        TermiteHackathonClient client = new TermiteHackathonClient();

        // Set the endpoint for termite
        client.setHost(TERMITE_API_ENDPOINT);

        // Add pubmed file for termite to execute on
        client.addFile(pubMedFile);

        // Set json as the output
        client.setParameter(TermiteHackathonClient.PARAM_OUTPUT_FORMAT, "json");

        // Execute termite and print response
        HttpResponse r = client.execute();
        String json = TermiteHackathonClient.getContent(r);

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        List<THit> hits =  JsonPath.read(document, "$.RESP_PAYLOAD.DRUG");
        System.out.println("hits: " + hits);
        return hits;
    }
}
