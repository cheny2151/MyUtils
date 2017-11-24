package youdao.utils;

//import com.hboxs.asl.entity.BaseEntity;
//import com.hboxs.asl.util.JsonUtils;
//import com.hboxs.asl.youdao.entity.Translation;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;

//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

/**
 * 有道翻译接口工具类
 * Created by hboxs010 on 2017/7/27.
 */
public class TranslationUtils {

//    private static final String APPKEY = "";
//    private static final String KEY = "";
//    private static final String URL = "https://openapi.youdao.com/api";
//
//    /**
//     * 可选 源语言 目标语言
//     */
//    public static List<String> translation(String q, String from, String to, String encoding) {
//        Map<String, String> map = setParams(q, from, to);
//        String resultJson = sendPost(map, URL, encoding);
//        Translation translation = JsonUtils.toObject(resultJson, Translation.class);
//        return (translation != null && translation.getTranslation() != null) ? translation.getTranslation() : null;
//    }
//
//    /**
//     * 中英互转 0:中转英 1:英转中
//     */
//    public static List<String> translation(String q, int code) {
//        if (code == 0) {
//            return translation(q, "zh-CHS", "EN", "utf-8");
//        } else if (code == 1) {
//            return translation(q, "EN", "zh-CHS", "utf-8");
//        }
//        return null;
//    }
//
//    /**
//     * 中英互转
//     */
//    public static String translation(String q, BaseEntity.Language language) {
//        if (BaseEntity.Language.zh.equals(language)) {
//            List<String> zh = translation(q, "EN", "zh-CHS", "utf-8");
//            return zh != null ? zh.get(0) : null;
//        } else if (BaseEntity.Language.en.equals(language)) {
//            List<String> en = translation(q, "zh-CHS", "EN", "utf-8");
//            return en != null ? en.get(0) : null;
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 封装参数
//     *
//     * @param query 要翻译的文本
//     * @param from  源语言 语言列表(可设置为auto)
//     * @param to    目标语言 语言列表(可设置为auto)
//     * @return 参数集合
//     */
//    private static Map<String, String> setParams(String query, String from, String to) {
//        String salt = String.valueOf(System.currentTimeMillis());
//        String sign = DigestUtils.md5Hex(APPKEY + query + salt + KEY);
//        Map<String, String> params = new HashMap<>();
//        params.put("q", query);
//        params.put("from", from);
//        params.put("to", to);
//        params.put("sign", sign);
//        params.put("salt", salt);
//        params.put("appKey", APPKEY);
//        return params;
//    }
//
//    /**
//     * post请求
//     *
//     * @param params 请求参数
//     * @param url    请求地址
//     * @return String
//     */
//    private static String sendPost(Map<String, String> params, String url, String encoding) {
//
//        CloseableHttpClient client = HttpClients.createDefault();
//        ArrayList<NameValuePair> pairs = new ArrayList<>();
//        HttpPost httpPost = new HttpPost(url);
//        if (params == null) {
//            return null;
//        }
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//        }
//        UrlEncodedFormEntity requestEntity;
//        try {
//            requestEntity = new UrlEncodedFormEntity(pairs, encoding);
//            httpPost.setEntity(requestEntity);
//            CloseableHttpResponse response = client.execute(httpPost);
//            HttpEntity responseEntity = response.getEntity();
//            String toString = EntityUtils.toString(responseEntity);
//            //关流
//            EntityUtils.consume(responseEntity);
//            return toString;
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (client != null) {
//                try {
//                    client.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }

}
