package importDataUtils.excel;

import POIUtils.annotation.ExcelData;
import lombok.Data;

/**
 * @author cheney
 * @date 2019/5/27
 */
@Data
public class Entity {

    @ExcelData(column = 0)
    private String arg0;

    @ExcelData(column = 1)
    private String arg1;

    @ExcelData(column = 2)
    private String arg2;

}
