package com.haizhi.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class HttpUtils {

    public static CloseableHttpClient createProxyHttpClient(String proxyHost, int proxyPort, String username, String password) {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        AuthScope authScope = new AuthScope(proxyHost, proxyPort);
        Credentials credentials = new UsernamePasswordCredentials(username, password);
        credsProvider.setCredentials(authScope, credentials);
        return HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
    }

    public static CloseableHttpClient createHttpClient() {
        return HttpClients.custom().build();
    }

    public static String get(CloseableHttpClient httpClient, String url, Map<String, String> map, int timeOut) {
        RequestConfig config;
        CloseableHttpResponse response = null;
        URIBuilder uriBuilder;
        HttpGet httpGet = null;
        try {
            uriBuilder = new URIBuilder(url);
            if (map != null) {
                for (Entry<String, String> entry : map.entrySet()) {
                    uriBuilder = uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = uriBuilder.build();
            httpGet = new HttpGet(uri);

            config = RequestConfig.custom().setConnectTimeout(timeOut * 1000).build();

            httpGet.setConfig(config);
            response = httpClient.execute(httpGet);

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity, "UTF-8");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static String post(CloseableHttpClient httpClient, String url, List<NameValuePair> params, int timeOut) {
        RequestConfig config;
        CloseableHttpResponse response = null;

        HttpPost httpPost = new HttpPost(url);

        config = RequestConfig.custom().setConnectTimeout(timeOut * 1000).build();

        httpPost.setConfig(config);
        try {
            // 设置类型
            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            response = httpClient.execute(httpPost);
            if (302 == response.getStatusLine().getStatusCode()) {
                return post(httpClient, response.getLastHeader("Location").getValue(), params, timeOut);
            }
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            httpPost.releaseConnection();
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

//    public static void main(String[] args) throws Exception {
//        //-----------post----------------
//        //List<NameValuePair> params = new ArrayList<NameValuePair>();
//        //params.add(new BasicNameValuePair("req", "{\"method\":\"checkMobile\",\"timestamp\":\"A100000000000001\",\"channelCode\":\"A1\",\"queryType\":\"1\",\"telephone\":\"15301929770\"}"));
////        HttpUtilsDemo.post("http://www.baidu.com", params,"",5,true);
////        HttpUtilsDemo.post("https://localhost/spdbSjptServer/service.cgi", params, "" , 5 ,false);
////        HttpUtilsDemo.post("http://localhost:7070/spdbSjptServer/service.cgi", params, "" , 5 ,false);
//        //-----------get------------------
//        String method = "{\"method\":\"checkMobile\",\"timestamp\":\"A100000000000001\",\"channelCode\":\"A1\",\"queryType\":\"1\",\"telephone\":\"15301929770\"}";
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("req", method);
////        HttpUtilsDemo.get("https://localhost/spdbSjptServer/service.cgi", map,"",5,false);
//        HttpUtils.get("http://localhost:7070/spdbSjptServer/service.cgi", map, "", 5);
////        HttpUtilsDemo.get("https://localhost/spdbSjptServer/service.cgi", map, "", 5, false,true);
////        HttpUtilsDemo.get("http://www.baidu.com", map, "", 5, true,false);
//    }

}