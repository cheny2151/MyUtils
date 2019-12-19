package school_oa.data;

import POIUtils.annotation.ExcelData;
import lombok.Data;

import static POIUtils.annotation.ExcelData.SwitchType.COLUMN_NUM;


/**
 * @author cheney
 * @date 2019-11-30
 */
@Data
public class Schedule {

    @ExcelData(column = 2, type = COLUMN_NUM)
    private String grade;
    @ExcelData(column = 3, type = COLUMN_NUM)
    private String classStr;
    @ExcelData(column = 7, type = COLUMN_NUM)
    private String master;
    @ExcelData(column = 8, type = COLUMN_NUM)
    @Name("语文")
    private String yuwen;
    @Name("数学")
    @ExcelData(column = 9, type = COLUMN_NUM)
    private String shuxue;
    @Name("英语")
    @ExcelData(column = 10, type = COLUMN_NUM)
    private String yingyu;
    @Name("物理")
    @ExcelData(column = 11, type = COLUMN_NUM)
    private String wuli;
    @Name("化学")
    @ExcelData(column = 12, type = COLUMN_NUM)
    private String huaxue;
    @Name("生物")
    @ExcelData(column = 13, type = COLUMN_NUM)
    private String shengwu;
    @Name("政治")
    @ExcelData(column = 14, type = COLUMN_NUM)
    private String zhengzhi;
    @Name("历史")
    @ExcelData(column = 15, type = COLUMN_NUM)
    private String lishi;
    @Name("地理")
    @ExcelData(column = 16, type = COLUMN_NUM)
    private String dili;
    @Name("体育")
    @ExcelData(column = 17, type = COLUMN_NUM)
    private String tiyu;
    @Name("音乐")
    @ExcelData(column = 18, type = COLUMN_NUM)
    private String yinyue;
    @Name("美术")
    @ExcelData(column = 19, type = COLUMN_NUM)
    private String meishu;
    @Name("电脑")
    @ExcelData(column = 20, type = COLUMN_NUM)
    private String diannao;
    @Name("通技")
    @ExcelData(column = 21, type = COLUMN_NUM)
    private String tongji;

}
