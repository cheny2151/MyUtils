package POIUtils.demo;

import POIUtils.ExcelCell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hboxs011 on 2017/8/21.
 */
public class MemberVoPOI implements Serializable {

    private static final long serialVersionUID = 5312112449334866041L;
    @ExcelCell(name = "微信xxxxxxxxxx")
    private String wechat;
    @ExcelCell(name = "姓名aaaaaaaaa")
    private String name;
//    @ExcelCell(name = "high",isList = true,wight = 10000)
    private List<String> high = new ArrayList<>();
    @ExcelCell(name = "xaaxaxaxaxa地址")
    private String address;
    @ExcelCell(name = "电话号码")
    private String phone;
//    @ExcelCell(name = "内容",isList = true,wight = 10000)
    private List<String> text = new ArrayList<>();
    private String memberRank;
    private Boolean isExpired;
    private String membercard;
    private String type;

    public List<String> getHigh() {
        return high;
    }

    public void setHigh(List<String> high) {
        this.high = high;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMemberRank() {
        return memberRank;
    }

    public void setMemberRank(String memberRank) {
        this.memberRank = memberRank;
    }

    public String getMembercard() {
        return membercard;
    }

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean expired) {
        isExpired = expired;
    }

    public void setMembercard(String membercard) {
        this.membercard = membercard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
