package POIUtils.demo;

import POIUtils.PoiUtils;
import POIUtils.TypeSwitchChain.TypeSwitchChain;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
        Double aDouble = TypeSwitchChain.getTypeSwitchChain().startTransform(Double.class, "9.9");
        System.out.println(aDouble);
    }

}
