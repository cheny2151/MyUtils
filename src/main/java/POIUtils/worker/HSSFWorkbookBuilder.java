package POIUtils.worker;

import POIUtils.annotation.ExcelCell;
import POIUtils.annotation.ExcelHead;
import POIUtils.utils.BeanUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * excel表创建者
 */
public class HSSFWorkbookBuilder {

    private final static String LIST_FIELD = "LIST_FIELD";

    private final static String TITLE = "HAS_TITLE";

    /**
     * 创建空表格
     *
     * @param targetClass
     * @return
     */
    public HSSFWorkbook createHead(Class<?> targetClass) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        createHead(workbook, targetClass);
        return workbook;
    }

    /**
     * 导出表格
     *
     * @param data
     * @return
     */
    public HSSFWorkbook createSheet(List<?> data) {

        if (data == null || data.get(0) == null) {
            return null;
        }
        Class<?> entityType = data.get(0).getClass();
        HSSFWorkbook workbook = new HSSFWorkbook();
        Map<String, Object> head = createHead(workbook, entityType);
        createCount(workbook, data, head);
        return workbook;

    }

    /**
     * 设置表头
     */
    private Map<String, Object> createHead(HSSFWorkbook workbook, Class<?> targetClass) {

        ExcelHead excelHead = targetClass.getAnnotation(ExcelHead.class);
        workbook.createSheet(excelHead != null ? excelHead.sheetName() : "sheet");
        //若有标题则第一行用于放标题
        String title = null;
        int startRowNumber = 0;
        if (excelHead != null && !"".equals(excelHead.title())) {
            title = excelHead.title();
            startRowNumber++;
        }

        HSSFSheet sheet = workbook.getSheetAt(0);
        int column = 0;
        Map<String, Object> headInfo = new LinkedHashMap<>();
        headInfo.put(LIST_FIELD, null);
        HSSFRow startRow = sheet.createRow(startRowNumber);
        for (Field field : targetClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelCell.class)) {
                ExcelCell cellAnnotation = field.getAnnotation(ExcelCell.class);
                //行高
                startRow.setHeight((short) 500);
                //列宽
                if (cellAnnotation.wight() > 0) {
                    sheet.setColumnWidth(column, cellAnnotation.wight() * 1024);
                } else {
                    sheet.setColumnWidth(column, cellAnnotation.name().getBytes().length * 500);
                }
                HSSFCell cell = startRow.createCell(column++);
                setHeadStyle(workbook, cell);
                cell.setCellValue(cellAnnotation.name());
                boolean isList = cellAnnotation.isList();
                if (headInfo.get(LIST_FIELD) == null && isList) {
                    headInfo.put(LIST_FIELD, field.getName());
                }
                headInfo.put(field.getName(), cellAnnotation.isList());
            }
        }

        if (title != null) {
            headInfo.put(TITLE, title);
            HSSFCell cell = createTitle(sheet, title, headInfo.size() - 2);
            cell.getRow().setHeight((short) 800);
            setHeadStyle(workbook, cell);
        }


        return headInfo;

    }

    private HSSFCell createTitle(HSSFSheet sheet, String title, int column) {
        HSSFCell hssfCell = setValue(sheet, 0, 0, title);
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, column);
        sheet.addMergedRegion(cellRangeAddress);
        setRegionBorder(cellRangeAddress, sheet);
        return hssfCell;
    }

    /**
     * 设置表内容
     */
    @SuppressWarnings("unchecked")
    private void createCount(HSSFWorkbook workbook, List<?> data, Map<String, Object> headInfo) {

        HSSFSheet sheet = workbook.getSheetAt(0);
        String listField = (String) headInfo.remove(LIST_FIELD);
        int row = 1;
        if (headInfo.remove(TITLE) != null) {
            row++;
        }
        for (Object object : data) {
            int column = 0;
            Integer size = null;
            if (listField != null) {
                List values = (List) BeanUtils.readValue(object, listField);
                size = values.size();
            }
            for (Map.Entry<String, Object> entry : headInfo.entrySet()) {
                Object value = BeanUtils.readValue(object, entry.getKey());
                if (value == null) {
                    value = "-";
                }
                //是否有List
                if (size != null) {
                    //是否是List
                    if ((boolean) entry.getValue()) {
                        List<String> values = (List) value;
                        if (values.size() == 0) {
                            HSSFCell cell = setValue(sheet, row, column, "-");
                            setCountStyle(workbook, cell);
                        } else {
                            for (int i = 0; i < size; i++) {
                                HSSFCell cell = setValue(sheet, row + i, column, values.get(i));
                                setCountStyle(workbook, cell);
                            }
                        }
                    } else {
                        HSSFCell cell = setValue(sheet, row, column, value.toString());
                        if (size > 0) {
                            CellRangeAddress cellRangeAddress = new CellRangeAddress(row, row + size - 1, column, column);
                            sheet.addMergedRegion(cellRangeAddress);
                            setRegionBorder(cellRangeAddress, sheet);
                        }
                        setCountStyle(workbook, cell);
                    }
                } else {
                    HSSFCell cell = setValue(sheet, row, column, value.toString());
                    setCountStyle(workbook, cell);
                }
                column++;
            }
            if (size == null || size == 0) {
                row++;
            } else {
                row += size;
            }
        }

    }

    /**
     * 设置表头样式
     *
     * @param workbook
     * @param cell
     */
    private void setHeadStyle(HSSFWorkbook workbook, HSSFCell cell) {
        HSSFCellStyle headStyle = workbook.createCellStyle();
        setCenterStyle(headStyle, cell);
        setBorder(headStyle, cell);
        setBoldFont(headStyle, workbook, cell);
    }

    /**
     * 设置内容样式
     *
     * @param workbook
     * @param cell
     */
    private void setCountStyle(HSSFWorkbook workbook, HSSFCell cell) {
        HSSFCellStyle countStyle = workbook.createCellStyle();
        setCenterStyle(countStyle, cell);
        setBorder(countStyle, cell);
    }

    /**
     * 设置居中样式
     *
     * @param cellStyle
     * @param cell
     */
    private void setCenterStyle(HSSFCellStyle cellStyle, HSSFCell cell) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 设置边框样式
     *
     * @param cellStyle
     * @param cell
     */
    private void setBorder(HSSFCellStyle cellStyle, HSSFCell cell) {
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cell.setCellStyle(cellStyle);
    }

    private void setRegionBorder(CellRangeAddress cellRangeAddress, HSSFSheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
    }

    /**
     * 设置黑体
     *
     * @param cellStyle
     * @param workbook
     * @param cell
     */
    private void setBoldFont(HSSFCellStyle cellStyle, HSSFWorkbook workbook, HSSFCell cell) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 设置单元格内容
     */
    private HSSFCell setValue(HSSFSheet sheet, int row, int column, String value) {

        HSSFRow nowRow = sheet.getRow(row) == null ? sheet.createRow(row) : sheet.getRow(row);
        HSSFCell cell = nowRow.getCell(column) == null ? nowRow.createCell(column) : nowRow.getCell(column);
        cell.setCellValue(value);
        return cell;

    }

}
