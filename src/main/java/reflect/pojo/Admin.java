package reflect.pojo;

/**
 * 管理员
 */
public class Admin extends UserBase {

    private static final long serialVersionUID = 3043047132369017603L;

    private String adminNumber;

    private String test;

    public String getAdminNumber() {
        return adminNumber;
    }

    public void setAdminNumber(String adminNumber) {
        this.adminNumber = adminNumber;
    }

    public void setTest2(String test) {
        this.test = test;
    }

    public String getTest2() {
        return test;
    }

    private String getAdminNumber2(){
        return adminNumber;
    }

}
