package POIUtils.demo;

import POIUtils.PoiUtils;
import POIUtils.annotation.ExcelHead;
import com.sun.istack.internal.NotNull;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        ArrayList<MemberVoPOI> list = new ArrayList<>();
        ArrayList<String> text = new ArrayList<>();
        text.add("asdf356s4");
        text.add("asdgdasg");
        text.add("是啊都就分了四个");
        for (int i = 0; i < 100; i++) {
            MemberVoPOI memberVoPOI = new MemberVoPOI();
            memberVoPOI.setPhone("136845354" + i);
            memberVoPOI.setName("Name" + i);
            memberVoPOI.setWechat("微信" + i);
            memberVoPOI.setAddress("地址地址" + i);
            list.add(memberVoPOI);
        }
        HSSFWorkbook workbook = PoiUtils.createSheet(list);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("C://360Downloads//test.xls");
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        File file = new File("C://360Downloads//test.xls");
        List<MemberVoPOI> memberVoPOIS = PoiUtils.readFormFile(file, MemberVoPOI.class);
        for (MemberVoPOI memberVoPOI : memberVoPOIS) {
            System.out.println(memberVoPOI);
        }
    }

    @Test
    public void test2() {
        HSSFWorkbook workbook = PoiUtils.createEmptySheet(MemberVoPOI.class);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("C://360Downloads//test.xls");
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3(){
        ExcelHead annotation = MemberVoPOI.class.getAnnotation(ExcelHead.class);
        System.out.println(annotation.title());
    }

}
