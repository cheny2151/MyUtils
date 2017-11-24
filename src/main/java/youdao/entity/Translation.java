package youdao.entity;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 翻译信息对象
 * Created by hboxs010 on 2017/7/27.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Translation {

    private String errorCode;

    private String query;

    private List<String> translation = new ArrayList<>();

    /*中文 	zh-CHS
    日文 ja
    英文 EN
    韩文 ko
    法文 fr
    俄文 ru
    葡萄牙文 pt
    西班牙文 es*/

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

}
