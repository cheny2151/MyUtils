package POIUtils.worker;

import POIUtils.ReadProperty;
import POIUtils.annotation.ExcelData;
import POIUtils.annotation.ExcelHead;
import POIUtils.exception.WorkBookReadException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import reflect.ReflectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel表读取者
 */
public class WorkBookReader {

    public <T> List<T> reader(File file, Class<T> targetClass) throws WorkBookReadException {
        try {
            Workbook workbook = judgeWorkBook(file);
            Sheet sheet = null;
            //数据出现行数,从0开始算
            ExcelHead excelHead = targetClass.getAnnotation(ExcelHead.class);
            int titleRow = 1;
            if (excelHead != null) {
                titleRow = excelHead.titleRow();
                sheet = workbook.getSheet(excelHead.sheetName());
            } else {
                sheet = workbook.getSheetAt(0);
            }
            int startRowNumber = titleRow + 1;
            // 分析excel列表映射字段信息
            Map<Integer, ReadProperty> readPropertyMap = analysisAnnotation(targetClass, sheet.getRow(titleRow));
            List<T> results = new ArrayList<>();
            for (int i = startRowNumber; i <= sheet.getLastRowNum(); i++) {
                T t = createTarget(targetClass, readPropertyMap, sheet.getRow(i));
                results.add(t);
            }
            return results;
        } catch (Exception e) {
            throw new WorkBookReadException("excel解析失败" + e.getMessage(), e);
        }
    }

    /**
     * 根据每一行创建一个实体
     *
     * @param targetClass     目标class
     * @param readPropertyMap 字段信息集合
     * @param row             行
     * @param <T>             目标泛型
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    private <T> T createTarget(Class<T> targetClass, Map<Integer, ReadProperty> readPropertyMap, Row row) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        T t = ReflectUtils.newObject(targetClass, null, null);
        for (Map.Entry<Integer, ReadProperty> entry : readPropertyMap.entrySet()) {
            ReadProperty readProperty = entry.getValue();
            Cell cell = row.getCell(entry.getKey());
            if (cell == null) {
                return null;
            }
            Object value = getCellValue(cell);
            readProperty.writerUnknownTypeValue(t, value);
        }
        return t;
    }

    /**
     * 获取单元格数据(只处理Number和String)
     * Date属于Number
     *
     * @param cell 单元格实体
     * @return 单元格数据
     */
    private Object getCellValue(Cell cell) {
        Object value;
        switch (cell.getCellTypeEnum()) {
            case STRING: {
                value = cell.getStringCellValue();
                break;
            }
            case NUMERIC: {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            }
            default: {
                value = null;
            }
        }
        return value;
    }


    private Workbook judgeWorkBook(File file) throws IOException {
        if (file.getName().contains("xlsx")) {
            return new XSSFWorkbook(new FileInputStream(file));
        } else {
            return new HSSFWorkbook(new FileInputStream(file));
        }
    }

    /**
     * 解析注解，获取列对应field的map
     *
     * @param targetClass 目标类型
     * @param titleRow    标题行
     * @return 解析结果
     */
    public <T> Map<Integer, ReadProperty> analysisAnnotation(Class<T> targetClass, Row titleRow) throws WorkBookReadException {
        //遍历所有含有@ExcelData的字段
        Map<Integer, ReadProperty> readPropertyMap = new HashMap<>();
        Map<String, Integer> titleMap = new HashMap<>();
        titleRow.forEach(cell -> {
            titleMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        });
        for (Field field : targetClass.getDeclaredFields()) {
            ExcelData annotation = field.getAnnotation(ExcelData.class);
            if (annotation != null) {
                ExcelData.SwitchType type = annotation.type();
                switch (type) {
                    case COLUMN_NUM: {
                        //存放字段信息
                        readPropertyMap.put(annotation.column(),
                                new ReadProperty(field.getName(),
                                        field.getType(), field,
                                        ReflectUtils.getWriterMethod(targetClass, field.getName(), field.getType())
                                ));
                        break;
                    }
                    case COLUMN_TITLE: {
                        Integer index = titleMap.get(annotation.columnTitle());
                        if (index != null) {
                            readPropertyMap.put(index,
                                    new ReadProperty(field.getName(),
                                            field.getType(), field,
                                            ReflectUtils.getWriterMethod(targetClass, field.getName(), field.getType())
                                    ));
                        }
                    }
                }
            }
        }
        if (readPropertyMap.size() < 1) {
            throw new WorkBookReadException("未发现该类的@ExcelData注解");
        }
        return readPropertyMap;
    }

}
