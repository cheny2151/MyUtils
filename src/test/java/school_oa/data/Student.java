package school_oa.data;

import POIUtils.annotation.ExcelData;
import lombok.Data;

import static POIUtils.annotation.ExcelData.SwitchType.COLUMN_NUM;

/**
 * @author cheney
 * @date 2019-11-26
 */
@Data
public class Student {

    @ExcelData(column = 0, type = COLUMN_NUM)
    private String c1;
    @ExcelData(column = 1, type = COLUMN_NUM)
    private String c2;
    @ExcelData(column = 2, type = COLUMN_NUM)
    private String c3;
    @ExcelData(column = 3, type = COLUMN_NUM)
    private String c4;
    @ExcelData(column = 4, type = COLUMN_NUM)
    private String c5;
    @ExcelData(column = 5, type = COLUMN_NUM)
    private String c6;
    @ExcelData(column = 6, type = COLUMN_NUM)
    private String c7;
    @ExcelData(column = 7, type = COLUMN_NUM)
    private String c8;
    @ExcelData(column = 8, type = COLUMN_NUM)
    private String c9;

}
