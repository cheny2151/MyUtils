package school_oa.data;

import POIUtils.annotation.ExcelData;
import lombok.Data;


/**
 * @author cheney
 * @date 2019-11-30
 */
@Data
public class Schedule {

    @ExcelData(column = 2)
    private String grade;
    @ExcelData(column = 3)
    private String classStr;
    @ExcelData(column = 7)
    private String master;
    @ExcelData(column = 8)
    @Name("语文")
    private String yuwen;
    @Name("数学")
    @ExcelData(column = 9)
    private String shuxue;
    @Name("英语")
    @ExcelData(column = 10)
    private String yingyu;
    @Name("物理")
    @ExcelData(column = 11)
    private String wuli;
    @Name("化学")
    @ExcelData(column = 12)
    private String huaxue;
    @Name("生物")
    @ExcelData(column = 13)
    private String shengwu;
    @Name("政治")
    @ExcelData(column = 14)
    private String zhengzhi;
    @Name("历史")
    @ExcelData(column = 15)
    private String lishi;
    @Name("地理")
    @ExcelData(column = 16)
    private String dili;
    @Name("体育")
    @ExcelData(column = 17)
    private String tiyu;
    @Name("音乐")
    @ExcelData(column = 18)
    private String yinyue;
    @Name("美术")
    @ExcelData(column = 19)
    private String meishu;
    @Name("电脑")
    @ExcelData(column = 20)
    private String diannao;
    @Name("通技")
    @ExcelData(column = 21)
    private String tongji;

}
