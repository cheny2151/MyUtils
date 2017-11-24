package httpClient;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class HttpClientUtils {

    /**
     * post一般请求
     *
     * @param params 请求参数
     * @param url    请求地址
     * @return String
     */
    public static String sendPost(Map<String, Object> params, Map<String, String> header, String url, String encoding, boolean isJson) {

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = httpClientBuilder.build();
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
    public static void sendMultipartPost(String content, String deviceNo) throws CloudPosRequestException {

        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = DigestUtils.md5Hex(MEMBER_CODE + DEVICE_NO + "" + reqTime + KEY);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(URL);
        HttpEntity responseEntity = null;


        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addPart("reqTime", new StringBody(reqTime, ContentType.create("multipart/form-data", "utf-8"))).addPart("securityCode", new StringBody(securityCode, ContentType.create("multipart/form-data", "utf-8"))).addPart("memberCode", new StringBody(MEMBER_CODE, ContentType.create("multipart/form-data", "utf-8"))).addPart("deviceNo", new StringBody(deviceNo, ContentType.create("multipart/form-data", "utf-8")))
                    .addPart("mode", new StringBody(MODE, ContentType.create("multipart/form-data", "utf-8"))).addPart("msgDetail", new StringBody(content, ContentType.create("multipart/form-data", "utf-8")));
            HttpEntity entity = multipartEntityBuilder.build();
            httpPost.setEntity(entity);
            CloseableHttpResponse response = client.execute(httpPost);
            responseEntity = response.getEntity();
            Map<String, Object> map = JsonUtils.toObject(EntityUtils.toString(responseEntity), Map.class);
            EntityUtils.consume(responseEntity);
            //请求失败抛出自定义异常，controller处理
            if (map == null || (Integer) map.get("code") != 0) {
                throw new CloudPosRequestException("Request Error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (responseEntity != null) {
                try {
                    EntityUtils.consume(responseEntity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
