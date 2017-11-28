package httpClient;

import jsonUtils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class HttpClientUtils {

    private static HttpClientBuilder httpClientBuilder;

    private HttpClientUtils() {
    }

    /**
     * post一般请求
     *
     * @param params 请求参数
     * @param url    请求地址
     * @return String
     */
    public static String sendPost(Map<String, Object> params, Map<String, String> header, String url, String encoding, boolean isJson) {

        CloseableHttpClient client = getCloseableHttpClient();
        HttpPost httpPost = new HttpPost(url);
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        setPostEntity(params, encoding, httpPost, isJson);
        return sendPost(client, httpPost);

    }

    private static void setPostEntity(Map<String, Object> params, String encoding, HttpPost httpPost, boolean isJson) {

        if (isJson) {
            String data = JsonUtils.toJson(params);
            System.out.println(data);
            StringEntity stringEntity = new StringEntity(data != null ? data : "", encoding);
            httpPost.setHeader("Content-type", "application/json;charset=" + encoding);
            httpPost.setEntity(stringEntity);
        } else {
            if (params != null) {
                ArrayList<org.apache.http.NameValuePair> pairs = new ArrayList<>();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                try {
                    UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(pairs, encoding);
                    httpPost.setEntity(requestEntity);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static String sendPost(CloseableHttpClient client, HttpPost httpPost) {

        try {
            CloseableHttpResponse response = client.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String toString = EntityUtils.toString(responseEntity);
            //关流
            EntityUtils.consume(responseEntity);
            return toString;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Multipart请求
     */
    public static String sendMultipartPost(String url, Map<String, String> stringParam, String deviceNo) {

        CloseableHttpClient client = getCloseableHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (Map.Entry<String, String> entry : stringParam.entrySet()) {
                multipartEntityBuilder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.create("multipart/form-data", "utf-8")));
            }
            HttpEntity entity = multipartEntityBuilder.build();
            httpPost.setEntity(entity);
            CloseableHttpResponse response = client.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            String toString = EntityUtils.toString(responseEntity);
            EntityUtils.consume(responseEntity);
            return toString;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取client
     */
    private static CloseableHttpClient getCloseableHttpClient() {
        if (httpClientBuilder == null) {
            httpClientBuilder = HttpClientBuilder.create();
        }
        return httpClientBuilder.build();
    }

}
