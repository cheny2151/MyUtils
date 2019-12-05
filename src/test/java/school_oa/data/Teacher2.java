package school_oa.data;

import POIUtils.annotation.ExcelData;
import lombok.Data;

/**
 * @author cheney
 * @date 2019-12-02
 */
@Data
public class Teacher2 {

    @ExcelData(column = 2)
    private String name;
    @ExcelData(column = 1)
    private String subject;
    @ExcelData(column = 3)
    private String phone;
    @ExcelData(column = 4)
    private String shortPhone;

}
