package school_oa.data;

import POIUtils.annotation.ExcelData;
import POIUtils.annotation.ExcelHead;
import lombok.Data;

/**
 * @author cheney
 * @date 2019-11-26
 */
@Data
public class Student {

    @ExcelData(column = 0)
    private String c1;
    @ExcelData(column = 1)
    private String c2;
    @ExcelData(column = 2)
    private String c3;
    @ExcelData(column = 3)
    private String c4;
    @ExcelData(column = 4)
    private String c5;
    @ExcelData(column = 5)
    private String c6;
    @ExcelData(column = 6)
    private String c7;
    @ExcelData(column = 7)
    private String c8;
    @ExcelData(column = 8)
    private String c9;

}
