package POIUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

public class WorkBookReader {

    public <T> List<T> reader(File file, Class<T> targetClass) throws WorkBookReadException {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = judgeWorkBook(file.getName());
            Map<Integer, ReadProperty> readPropertyMap = analyAnnotation(targetClass);
            Sheet sheet = workbook.getSheetAt(1);
            //默认数据都从第二行开始
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                T t = createTarget(targetClass, readPropertyMap, sheet.getRow(i));
            }
            return null;
        } catch (Exception e) {
            throw new WorkBookReadException("excel解析失败" + e.getMessage(), e);
        }
    }

    private <T> T createTarget(Class<T> targetClass, Map<Integer, ReadProperty> readPropertyMap, Row row) throws IllegalAccessException, InstantiationException {
        T t = targetClass.newInstance();
        for (Map.Entry<Integer, ReadProperty> entry : readPropertyMap.entrySet()) {
            ReadProperty readProperty = entry.getValue();
            Cell cell = row.getCell(entry.getKey());
            Object value;
            if (cell.getCellTypeEnum().equals(NUMERIC)) {
                value = cell.getNumericCellValue();
            } else if (cell.getCellTypeEnum().equals(STRING)) {
                value = cell.getStringCellValue();
            }
//            BeanUtils.writerUnkownTypeProperty(t,);
//            readProperty.getPropertyClass()
        }
        return null;
    }


    private Workbook judgeWorkBook(String name) {
        return null;
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
