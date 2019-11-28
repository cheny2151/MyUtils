package school_oa.data;

import POIUtils.PoiUtils;
import importDataUtils.mysql.SimplePool;
import jsonUtils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cheney
 * @date 2019-11-28
 */
public class ImportScript {

    @Test
    public void createGrades() {

        HashMap<String, Object> gradeMap = new HashMap<>();
        gradeMap.put("初一", 1);
        gradeMap.put("初二", 2);
        gradeMap.put("初三", 3);
        gradeMap.put("高一", 4);
        gradeMap.put("高二", 5);
        gradeMap.put("高三", 6);
        SimplePool simplePool = new SimplePool();
        try (Connection connection = simplePool.getConnection()) {
            List<Student> entities = PoiUtils.readFormFile(new File("C:\\Users\\Cheney\\Documents\\Project\\化州中学oa\\数据库\\学生名册.xlsx"), Student.class);
            Set<Grade> gradeSet = new HashSet<>();
            for (Student entity : entities) {
                String gradeName = entity.getC3().trim();
                Integer grade = (Integer) gradeMap.get(entity.getC2().trim());
                gradeSet.add(new Grade(grade, gradeName));
            }
            List<Grade> gradesList = gradeSet.stream().sorted((Comparator.comparing(Grade::getGrade).thenComparing(Grade::getCode))).collect(Collectors.toList());
            PreparedStatement preparedStatement = connection.prepareStatement("insert into t_class(name,grade,code,create_date) values(?,?,?,sysdate())");
            gradesList.forEach(e -> {
                try {
                    preparedStatement.setString(1, e.getName());
                    preparedStatement.setInt(2, e.getGrade());
                    preparedStatement.setString(3, e.getCode());
                    preparedStatement.addBatch();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void createStudents() {

        HashMap<String, Object> gradeMap = new HashMap<>();
        gradeMap.put("初一", 1);
        gradeMap.put("初二", 2);
        gradeMap.put("初三", 3);
        gradeMap.put("高一", 4);
        gradeMap.put("高二", 5);
        gradeMap.put("高三", 6);
        HashMap<String, Object> gender = new HashMap<>();
        gender.put("男", 1);
        gender.put("女", 2);
        HashMap<String, Object> inDorm = new HashMap<>();
        inDorm.put("外宿", 0);
        inDorm.put("外", 0);
        inDorm.put("内宿", 1);
        inDorm.put("内", 1);
        HashMap<String, Object> subCategoryMap = new HashMap<>();
        subCategoryMap.put("理科", 1);
        subCategoryMap.put("文科", 2);
        SimplePool simplePool = new SimplePool();
        try (Connection connection = simplePool.getConnection()) {
            List<Student> entities = PoiUtils.readFormFile(new File("C:\\Users\\Cheney\\Documents\\Project\\化州中学oa\\数据库\\学生名册.xlsx"), Student.class);
            PreparedStatement preparedStatement
                    = connection.prepareStatement("insert into t_student(sid,nickname,grade_code,class_code,gender,in_dorm,subject_category,category,create_date)" +
                    " values(?,?,?,?,?,?,?,?,sysdate())");
            for (Student entity : entities) {
                String gradeName = entity.getC3().trim();
                Integer grade = (Integer) gradeMap.get(entity.getC2().trim());
                Grade gradeEntity = new Grade(grade, gradeName);
                preparedStatement.setString(1, entity.getC1().trim());
                preparedStatement.setString(2, entity.getC5().trim());
                preparedStatement.setInt(3, gradeEntity.getGrade());
                preparedStatement.setString(4, gradeEntity.getCode());
                preparedStatement.setInt(5, (Integer) gender.get(entity.getC6().trim()));
                String trim = entity.getC7().trim();
                System.out.println(trim);
                preparedStatement.setInt(6, (Integer) inDorm.get(trim));
                String Subcategory = entity.getC8().trim();
                if (StringUtils.isNotEmpty(Subcategory)) {
                    Integer subCategoryInt = (Integer) subCategoryMap.get(Subcategory);
                    preparedStatement.setInt(7, subCategoryInt);
                } else {
                    preparedStatement.setNull(7, Types.TINYINT);
                }
                String category = entity.getC9().trim();
                if (StringUtils.isNotEmpty(Subcategory) && category.equals("往届")) {
                    preparedStatement.setInt(8, 2);
                } else {
                    preparedStatement.setInt(8, 1);
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createTeacher() {

        HashMap<String, Object> gender = new HashMap<>();
        gender.put("男", 1);
        gender.put("女", 2);
        Map<String, Object> subMap = (Map<String, Object>) JsonUtils.toMap("{\"行政\":1,\"生物\":2,\"历史\":3,\"语文\":4,\"英语\":5," +
                "\"美术\":6,\"音乐\":7,\"政治\":8,\"电脑\":9,\"物理\":10,\"体育\":11,\"数学\":12,\"化学\":13,\"地理\":14}");
        HashMap<String, Object> married = new HashMap<>();
        married.put("已婚", 1);
        married.put("未婚", 0);
        SimplePool simplePool = new SimplePool();
        try (Connection connection = simplePool.getConnection()) {
            connection.setAutoCommit(false);
            List<Teacher> entities = PoiUtils.readFormFile(new File("C:\\Users\\Cheney\\Documents\\Project\\化州中学oa\\数据库\\教职工信息.xlsx"), Teacher.class);
            PreparedStatement preparedStatement
                    = connection.prepareStatement("insert into t_teacher(tid,nickname,gender,birthday,subject,phone,short_phone,card_id,education,political,married,create_date)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,sysdate())");
            entities = entities.stream().filter(e -> StringUtils.isNotEmpty(e.getTid()) && !e.getTid().equals("NULL")).sorted(Comparator.comparing(e -> Integer.valueOf(e.getTid())))
                    .collect(Collectors.toList());
            for (Teacher entity : entities) {
                if (StringUtils.isEmpty(entity.getTid())) continue;
                preparedStatement.setString(1, entity.getTid());
                preparedStatement.setString(2, entity.getName());
                preparedStatement.setInt(3, (Integer) gender.get(entity.getGender()));
                preparedStatement.setInt(4, Integer.parseInt(entity.getBirthday()));
                preparedStatement.setInt(5, (Integer) subMap.get(entity.getSubject()));
                preparedStatement.setString(6, entity.getPhone());
                preparedStatement.setInt(7, Integer.parseInt(entity.getShortPhone()));
                preparedStatement.setString(8, entity.getCardId());
                preparedStatement.setString(9, entity.getEducation());
                preparedStatement.setString(10, entity.getPolitical());
                preparedStatement.setInt(11, (Integer) married.get(entity.getMarried()));
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
