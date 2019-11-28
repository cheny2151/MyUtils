package school_oa.data;

import lombok.Data;

/**
 * @author cheney
 * @date 2019-11-27
 */
@Data
public class Grade {

    private int grade;

    private String name;

    private String code;

    public Grade(int grade, String name) {
        this.grade = grade;
        this.name = name;
        if (name.startsWith("文")) {
            this.code = "W" + name.replace("文", "");
        } else if (name.startsWith("理")) {
            this.code = "L" + name.replace("理", "");
        } else if (name.startsWith("中华")) {
            this.code = "ZH" + name.replace("中华", "");
        } else {
            this.code = name;
        }
    }

    @Override
    public int hashCode() {
        return grade;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Grade) {
            return ((Grade) obj).getCode().equals(this.code);
        }
        return false;
    }
}
