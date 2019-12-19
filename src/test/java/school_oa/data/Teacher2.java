package school_oa.data;

import POIUtils.annotation.ExcelData;
import lombok.Data;

import static POIUtils.annotation.ExcelData.SwitchType.COLUMN_NUM;

/**
 * @author cheney
 * @date 2019-12-02
 */
@Data
public class Teacher2 {

    @ExcelData(column = 2, type = COLUMN_NUM)
    private String name;
    @ExcelData(column = 1, type = COLUMN_NUM)
    private String subject;
    @ExcelData(column = 3, type = COLUMN_NUM)
    private String phone;
    @ExcelData(column = 4, type = COLUMN_NUM)
    private String shortPhone;

}
