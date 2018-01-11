package POIUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class WorkBookReader {

    public <T> List<T> reader(File file, Class<T> targetClass) throws WorkBookReadException {
        try {
            Workbook workbook = judgeWorkBook(file);
            Map<Integer, ReadProperty> readPropertyMap = analyAnnotation(targetClass);
            Sheet sheet = workbook.getSheetAt(0);
            //默认数据都从第二行开始
            List<T> results = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
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
        T t = targetClass.newInstance();
        for (Map.Entry<Integer, ReadProperty> entry : readPropertyMap.entrySet()) {
            ReadProperty readProperty = entry.getValue();
            Cell cell = row.getCell(entry.getKey());
            Object value;
            if (cell.getCellTypeEnum().equals(NUMERIC)) {
                value = cell.getNumericCellValue();
            } else if (cell.getCellTypeEnum().equals(STRING)) {
                value = cell.getStringCellValue();
            } else {
                value = null;
            }
            readProperty.writerUnknownTypeValue(t, value);
        }
        return t;
    }


    private Workbook judgeWorkBook(File file) throws IOException {
        return new HSSFWorkbook(new FileInputStream(file));
    }

    /**
     * 解析注解，获取列对应field的map
     *
     * @param targetClass
     * @return
     */
    public <T> Map<Integer, ReadProperty> analyAnnotation(Class<T> targetClass) throws WorkBookReadException {
        //遍历所有含有@ExcelData的字段
        Map<Integer, ReadProperty> readPropertyMap = new HashMap<>();
        for (Field field : targetClass.getDeclaredFields()) {
            ExcelData annotation = field.getAnnotation(ExcelData.class);
            if (annotation != null) {
                //存放字段信息
                readPropertyMap.put(annotation.column()
                        , new ReadProperty<T>(field.getName(), field.getType(), field, BeanUtils.getWriterMethod(targetClass, field.getName(), field.getType()))
                );
            }
        }
        if (readPropertyMap.size() < 1) {
            throw new WorkBookReadException("未发现该类的@ExcelData注解");
        }
        return readPropertyMap;
    }

}
