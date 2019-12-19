package importDataUtils.excel;

import POIUtils.annotation.ExcelData;
import lombok.Data;

import static POIUtils.annotation.ExcelData.SwitchType.COLUMN_NUM;

/**
 * @author cheney
 * @date 2019/5/27
 */
@Data
public class Entity {

    @ExcelData(column = 0, type = COLUMN_NUM)
    private String arg0;

    @ExcelData(column = 1, type = COLUMN_NUM)
    private String arg1;

    @ExcelData(column = 2, type = COLUMN_NUM)
    private String arg2;

}
