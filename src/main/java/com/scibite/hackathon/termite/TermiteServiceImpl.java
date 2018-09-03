package com.scibite.hackathon.termite;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.scibite.hackathon.AbstractSciBiteService;
import com.scibite.termitej.common.hit.THit;
import org.apache.http.HttpResponse;

import java.io.File;
import java.util.List;

/**
 * This class was created by simon on 03/09/2018.
 */
public class TermiteServiceImpl extends AbstractSciBiteService implements TermiteService {

    private static final String TERMITE_API_ENDPOINT = "https://109.74.206.246/termite";

    /**
     * Run termite over a provided String
     * @param inputData
     * @return
     * @throws Exception
     */
    public List<THit> termite(String inputData) throws Exception {
        TermiteHackathonClient client = new TermiteHackathonClient();
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

    /**
     * Run termite over a provided file
     * @param file
     * @return
     * @throws Exception
     */
    public List<THit> termite(File file) throws Exception{
        TermiteHackathonClient cl = new TermiteHackathonClient();

        //cl.setHost("http://localhost:9090/termite");
        cl.setHost(TERMITE_API_ENDPOINT);

        // Set some text
        cl.addFile(file);
        cl.setParameter(TermiteHackathonClient.PARAM_OUTPUT_FORMAT, "json");

        HttpResponse r = cl.execute();
        String json = TermiteHackathonClient.getContent(r);

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        List<THit> hits =  JsonPath.read(document, "$.RESP_PAYLOAD.DRUG");
        System.out.println("hits: " + hits);
        return hits;
    }

    public static void main(String[] args) throws Exception {
        TermiteServiceImpl termiteService = new TermiteServiceImpl();


        //termiteService.termite("viagra or sildenafil are two Termite hits");
        termiteService.termite(new File("./src/resources/termiteTestFile.txt"));
    }



    // TODO - left this here for the moment, its the old code without the Termite API jars
    /*public void termite(String word) throws IOException {
        String urlString = String.format("https://109.74.206.246/termite", word);
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
        System.out.println(obj);
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
    }*/
}
