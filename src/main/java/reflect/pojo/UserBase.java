package reflect.pojo;

import java.io.Serializable;

public class UserBase implements Serializable {

    private static final long serialVersionUID = 3896539459527873185L;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}