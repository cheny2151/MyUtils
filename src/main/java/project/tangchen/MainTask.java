package project.tangchen;

import POIUtils.PoiUtils;
import POIUtils.annotation.ExcelCell;
import POIUtils.annotation.ExcelHead;
import POIUtils.entity.ExcelReadInfo;
import POIUtils.entity.ReadResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import httpClient.HttpClientUtils;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author by chenyi
 * @date 2022/3/25
 */
public class MainTask {


    private static List<String> filters = Arrays.asList("5-hydroxytryptophan",
            "acerola",
            "acesulfame k",
            "acetyl dl methionine (n  acetyl.)",
            "agave",
            "algae oil",
            "aloe vera",
            "alpha lipoic acid",
            "anchovy",
            "angelica",
            "artichoke",
            "ashwagandha",
            "ashwagandha extract",
            "asparagus",
            "astaxanthin",
            "astragalus",
            "bacopa",
            "beetroot",
            "beta-carotene",
            "bifidobacterium bifidum",
            "bifidobacterium breve",
            "bifidobacterium lactis",
            "bifidobacterium longum",
            "bilberry",
            "black pepper",
            "black pepper extract",
            "blackcurrant",
            "blueberry",
            "broccoli",
            "caffeine",
            "caffeine anhydrous",
            "carnitine(l-)",
            "cbd(cannabidiol)",
            "chamomile",
            "chicory root",
            "chlorella",
            "choline",
            "choline bitartrate",
            "chromium picolinate",
            "citicoline",
            "citrus bioflavonoids",
            "coconut",
            "cod",
            "cod liver oil",
            "coenzyme q10",
            "collagen hydrolysate",
            "cranberry",
            "cranberry extract",
            "curcumin",
            "docosahexaenoic acid(dha)",
            "dong quai",
            "eicosapentaenoic acid(epa)",
            "eleutherococcus",
            "eleutherococcus extract",
            "evening primrose",
            "evening primrose oil",
            "fennel",
            "fish oil",
            "gamma aminobutyric acid",
            "ginger",
            "ginger extract",
            "ginkgo biloba",
            "ginkgo biloba extract",
            "ginkgo extract",
            "ginseng",
            "ginseng extract",
            "glutamine",
            "goji",
            "gotu kola",
            "gotu kola extract",
            "grape seed extract",
            "green tea extract",
            "guarana",
            "guarana extract",
            "hemp",
            "hemp extract",
            "hops",
            "hyaluronic acid",
            "inulin",
            "krill",
            "lactobacillus acidophilus",
            "lactobacillus casei",
            "lactobacillus plantarum",
            "lactobacillus rhamnosus",
            "lecithin",
            "lemon balm",
            "lion's mane mushroom",
            "liver",
            "l-theanine",
            "maca",
            "maca extract",
            "mackerel",
            "matcha tea",
            "medium chain triglycerides",
            "medium chain triglycerides oil",
            "melatonin",
            "microalgae",
            "mono-and diglycerides",
            "moringa",
            "nettle",
            "oat",
            "oligofructose",
            "omega-3",
            "pepper extract",
            "phosphatidylcholine",
            "phosphatidylserine",
            "pyrroloquinoline quinone",
            "raspberry",
            "reishi mushroom",
            "rhodiola rosea",
            "rhodiola rosea extract",
            "rosemary",
            "rosemary extract",
            "saffron",
            "sage",
            "schizochytrium",
            "soy lecithin",
            "spirulina",
            "stevia",
            "streptococcus thermophilus",
            "sunflower lecithin",
            "sunflower oil",
            "taigaroot extract",
            "taurine",
            "tea extract",
            "turmeric",
            "turmeric extract",
            "valerian");

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/chenyi/Downloads/海外市场调研");
        File[] files = file.listFiles();
        for (File file1 : files) {
            File[] files1List = file1.listFiles();
            if (files1List == null) {
                continue;
            }
            String name = file1.getName();
            for (File listFile : files1List) {
                if (listFile.getName().endsWith("xls")) {
                    run(listFile, name);
                }
            }
        }
    }

    @SneakyThrows
    private static void run(File file, String dir) {
        String name = file.getName();
        name = FilenameUtils.getBaseName(name);
        File rsFile = new File("/Users/chenyi/Downloads/runresult/" + dir + "/" + name + "_result.xls");

        ReadResult<Map<String, Object>> result = PoiUtils.readAsMap(file, ExcelReadInfo.readInfo());
        List<Set<String>> list = result.getData().stream().filter(row -> row.get("Ingredients") != null)
                .map(row -> (String) row.get("Ingredients"))
                .map(MainTask::split)
                .collect(Collectors.toList());
//        Map<String, Integer> map = collect.stream().collect(Collectors.groupingBy(t -> t, Collectors.reducing(0, t -> 1, Integer::sum)));
//        List<TextCount> data = map.entrySet().stream().map(entry -> new TextCount(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        Apriori apriori = new Apriori(list, 0.1, 0.1);
        Map<Integer, Map<Set<String>, Integer>> allFrequentMap = apriori.allFrequentMap;

        HSSFWorkbook workbook = null;
        for (int i = 1; i < 5; i++) {
            Map<Set<String>, Integer> map = allFrequentMap.get(i);
            if (map != null && map.size() > 0) {
                List<TextCount> collect = map.entrySet().stream().map(entry -> new TextCount(String.join("｜", trans(entry.getKey())), entry.getValue()))
                        .sorted(Comparator.comparingInt(TextCount::getCount).reversed())
                        .collect(Collectors.toList());
                String sheetName = String.valueOf(i + 1);
                if (workbook == null) {
                    workbook = PoiUtils.createSheet(collect, sheetName);
                } else {
                    PoiUtils.createSheet(workbook, collect, sheetName);
                }
            }
        }


        if (workbook != null) {
            workbook.write(rsFile);
        }
    }

    private static Set<String> split(String text) {
        Set<String> texts = new HashSet<>();
        char split = ',';
        boolean needClose = false;
        char[] chars = text.toCharArray();
        int start = 0;
        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];
            if (needClose) {
                if (c == ')') {
                    needClose = false;
                }
            }
            if (c == '(') {
                needClose = true;
                continue;
            }
            if (c == split && !needClose) {
                texts.add(new String(chars, start, i - start).toLowerCase().trim());
                start = i + 1;
            }
        }
        return texts;
    }

    private static Collection<String> trans(Collection<String> text) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sourceLanguage", "en");
        map.put("targetLanguage", "zh");
        map.put("type", 4);
        map.put("texts", text);
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("aiAppId", "test");
        String resp = HttpClientUtils.sendPost(map, httpHeaders, "http://localhost:10881/v1/in", "utf-8", true);
        return JSON.parseObject(resp).getJSONObject("data").getJSONArray("texts").toJavaList(JSONObject.class).stream().map(e -> e.getString("target"))
                .collect(Collectors.toList());
    }

    @Data
    @ExcelHead
    private static class TextCount {
        @ExcelCell(name = "Ingredient")
        private String text;
        @ExcelCell(name = "Count")
        private Integer count;

        public TextCount(String text, Integer count) {
            this.text = text;
            this.count = count;
        }
    }

}
