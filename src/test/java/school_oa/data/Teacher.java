package school_oa.data;

import POIUtils.annotation.ExcelData;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

/**
 * @author cheney
 * @date 2019-11-27
 */
@ToString
public class Teacher {

    @ExcelData(column = 0)
    private String tid;
    @ExcelData(column = 1)
    private String name;
    @ExcelData(column = 2)
    private String gender;
    @ExcelData(column = 3)
    private String cardId;
    @ExcelData(column = 4)
    private String birthday;
    // 政治面貌
    @ExcelData(column = 8)
    private String political;
    // 学历
    @ExcelData(column = 9)
    private String education;
    @ExcelData(column = 16)
    private String subject;
    @ExcelData(column = 17)
    private String phone;
    @ExcelData(column = 18)
    private String shortPhone;
    @ExcelData(column = 20)
    private String married;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = trim(tid);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = trim(name);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = trim(gender);
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = trim(cardId);
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = trim(birthday);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = trim(subject);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = trim(phone);
    }

    public String getShortPhone() {
        return shortPhone;
    }

    public void setShortPhone(String shortPhone) {
        this.shortPhone = trim(shortPhone);
    }

    private String trim(String field) {
        if (StringUtils.isNotEmpty(field)) {
            field = field.trim();
        }
        return field;
    }

    public String getPolitical() {
        return political;
    }

    public void setPolitical(String political) {
        this.political = trim(political);
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = trim(education);
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = trim(married);
    }
}
