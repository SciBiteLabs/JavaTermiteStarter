package com.scibite.hackathon;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class was created by simon on 03/09/2018.
 */
public class SimilarityServiceImpl extends AbstractHackathonService implements SimilarityService {





    @Override public List<WordSimilarity> similar(String word) throws IOException {
        String urlString = String.format("https://ugm.scibite.com:7000/similar/%s", word);
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();

        String userpass = userName + ":" + password;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
        urlConnection.setRequestProperty("Authorization", basicAuth);

        StringBuilder builder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
        }

        String json = builder.toString();
        JSONObject obj = new JSONObject(json);
        JSONObject results = obj.getJSONObject("results");
        Iterator<String> resultIterator = results.keys();
        List<WordSimilarity> similarities = new ArrayList<>();
        while (resultIterator.hasNext()) {
            String resultString = resultIterator.next();
            double value = results.getDouble(resultString);
            WordSimilarity ws = new WordSimilarity(resultString, value);
            similarities.add(ws);
        }
        Collections.sort(similarities);
        return similarities;
    }

    public static void main(String[] args) throws Exception {
        SimilarityService ss = new SimilarityServiceImpl();
        List<WordSimilarity> words = ss.similar("macula");
        for (WordSimilarity ws : words) {
            System.out.println(ws);
        }
    }
}

