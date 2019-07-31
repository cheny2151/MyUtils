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
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class HttpClientUtils {

    public HttpClientUtils() {
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
     * 获取client(懒加载builder 内部类方式实现)
     * 详解:为什么静态内部类单例可以实现延迟加载？实际上是外部类被加载时内部类并不需要立即加载内部类，内部类不被加载则不需要进行类初始化，因此单例对象在外部类被加载了以后不占用内存。
     * 详见:<a href="http://blog.csdn.net/reliveit/article/details/52874833"></a>
     */
    private static CloseableHttpClient getCloseableHttpClient() {
        return ClientHolder.httpClientBuilder.build();
    }

    private static class ClientHolder {
        private final static HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        static {
            System.out.println("加载内部类");
        }
    }

    @Test
    public void test() throws InterruptedException {
        System.out.println(new HttpClientUtils());
        Thread.sleep(5000);
        getCloseableHttpClient();
    }

}
