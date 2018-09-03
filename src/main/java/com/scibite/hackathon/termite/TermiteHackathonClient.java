package com.scibite.hackathon.termite;

import com.scibite.util.io.InputOutputUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TermiteHackathonClient {
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_INPUT_FORMAT = "format";
    public static final String PARAM_OUTPUT_FORMAT = "output";
    public static final String PARAM_FUZZY = "fuzzy";
    public static final String PARAM_SUBSUME = "subsume";
    public static final String PARAM_TEXPRESS_DYNA_PATTERN = "dpattern";
    public static final String PARAM_METHOD = "method";
    public static final String METHOD_TEXPRESS = "texpress";
    public static final String METHOD_TERMITE = "termite";
    public static final String METHOD_TERMITE_AND_TEXPRESS = "texpress,termite";
    private String host = "http://localhost:9090/termite";
    private Map<String, String> parameters = new HashMap();
    private Map<String, String> runtimeOptions = new HashMap();
    private byte[] uploadData;
    private String uploadUri;

    public TermiteHackathonClient() {
    }

    public void addFile(File f) throws FileNotFoundException, IOException {
        this.uploadData = InputOutputUtils.toBytes(f);
        this.uploadUri = f.getName();
    }

    public void addBinary(InputStream i, String uri) throws FileNotFoundException, IOException {
        this.uploadData = IOUtils.toByteArray(i);
        this.uploadUri = uri;
    }

    public HttpResponse execute() throws Exception {
        CloseableHttpClient httpclient = getHttpClientForHackathon();

        HttpPost httpPost = new HttpPost(this.getHost());
        String encoding = Base64.getEncoder().encodeToString(("hacker:g347regdsuxSW").getBytes());
        httpPost.setHeader("Authorization", "Basic " + encoding);

        CloseableHttpResponse response = null;
        MultipartEntityBuilder multiPartBuilder = MultipartEntityBuilder.create();
        Iterator var5 = this.getParameters().keySet().iterator();

        while(var5.hasNext()) {
            String pk = (String)var5.next();
            multiPartBuilder.addTextBody(pk, (String)this.getParameters().get(pk));
        }

        if (this.uploadData != null) {
            multiPartBuilder.addBinaryBody("binary", this.uploadData);
            multiPartBuilder.addTextBody("_binaryFile", this.uploadUri);
        }

        if (this.runtimeOptions != null && this.runtimeOptions.size() > 0) {
            StringBuilder sb = new StringBuilder();
            Iterator var10 = this.runtimeOptions.entrySet().iterator();

            while(var10.hasNext()) {
                Map.Entry<String, String> e = (Map.Entry)var10.next();
                if (sb.length() > 0) {
                    sb.append("&");
                }

                sb.append((String)e.getKey());
                sb.append("=");
                sb.append((String)e.getValue());
            }

            multiPartBuilder.addTextBody("opts", sb.toString());
        }

        HttpEntity multipart = multiPartBuilder.build();
        httpPost.setEntity(multipart);
        response = httpclient.execute(httpPost);
        return response;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    public Map<String, String> getRuntimeOptions() {
        return this.runtimeOptions;
    }

    public void setRuntimeOptions(Map<String, String> runtimeOptions) {
        this.runtimeOptions = runtimeOptions;
    }

    public void setRuntimeOption(String key, String value) {
        this.runtimeOptions.put(key, value);
    }

    public static String getContent(HttpResponse r) throws IllegalStateException, IOException {
        String var3;
        try {
            HttpEntity entity = r.getEntity();
            String result = IOUtils.toString(entity.getContent());
            EntityUtils.consume(entity);
            var3 = result;
        } finally {
            if (r != null && r instanceof CloseableHttpResponse) {
                try {
                    ((CloseableHttpResponse)r).close();
                } catch (Throwable var10) {
                    ;
                }
            }

        }

        return var3;
    }

    public void setMethod(String m) {
        this.setParameter("method", m);
    }

    private CloseableHttpClient getHttpClientForHackathon() throws Exception {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslsf)
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(2000);//max connection


        //System.setProperty("jsse.enableSNIExtension", "false"); //""
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .build();

        return httpclient;
    }
}
