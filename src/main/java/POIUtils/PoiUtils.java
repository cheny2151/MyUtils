package POIUtils;

import POIUtils.worker.HSSFWorkbookBuilder;
import POIUtils.worker.WorkBookReader;
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

    /**
     * 创建一张含有数据的表
     *
     * @param data 数据
     * @return
     */
    public static HSSFWorkbook createSheet(List<?> data) {
        return getHSSFWorkbookBuilder().createSheet(data);
    }

    /**
     * 创建一张表头
     *
     * @param targetClass 目标类型
     * @return
     */
    public static HSSFWorkbook createEmptySheet(Class<?> targetClass) {
        return getHSSFWorkbookBuilder().createHead(targetClass);
    }

    /**
     * 读取数据
     *
     * @param file        文件
     * @param targetClass 目标类型
     * @param <T>         类型
     * @return
     */
    public static <T> List<T> readFormFile(File file, Class<T> targetClass) {
        return getWorkBookReader().reader(file, targetClass);
    }


}
