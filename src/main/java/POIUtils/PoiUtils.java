package POIUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.util.List;

/**
 * create by cheny
 * V1.5
 */
public class PoiUtils {

    /**
     * 单例模式
     */
    private static class HSSFWorkbookBuilderHolder {
        private final static HSSFWorkbookBuilder HSSF_WORKBOOK_BUILDERK_BUILDER = new HSSFWorkbookBuilder();
    }

    /**
     * 单例模式
     */
    private static class WorkbookReaderHolder {
        private final static WorkBookReader WORKBOOK_READER = new WorkBookReader();
    }

    public static WorkBookReader getWorkBookReader() {
        return WorkbookReaderHolder.WORKBOOK_READER;
    }


    private static HSSFWorkbookBuilder getHSSFWorkbookBuilder() {
        return HSSFWorkbookBuilderHolder.HSSF_WORKBOOK_BUILDERK_BUILDER;
    }

    public static HSSFWorkbook createSheet(List<?> data) {
        return getHSSFWorkbookBuilder().createSheet(data);
    }

    public static <T> List<T> readFormFile(File file, Class<T> targetClass) {
        return getWorkBookReader().reader(file, targetClass);
    }


}
