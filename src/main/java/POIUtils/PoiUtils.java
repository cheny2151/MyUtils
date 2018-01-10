package POIUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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

    public static HSSFWorkbook createSheet(List<?> data) {
        return getHSSFWorkbookBuilder().createSheet(data);
    }


    private static HSSFWorkbookBuilder getHSSFWorkbookBuilder() {
        return HSSFWorkbookBuilderHolder.HSSF_WORKBOOK_BUILDERK_BUILDER;
    }

}
