package school_oa.data;

import POIUtils.PoiUtils;
import importDataUtils.mysql.SimplePool;
import jsonUtils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @SuppressWarnings("unchecked")
    @Test
    public void createTeacher2() {

        HashMap<String, Object> gender = new HashMap<>();
        gender.put("男", 1);
        gender.put("女", 2);
        Map<String, Object> subMap = (Map<String, Object>) JsonUtils.toMap("{\"行政\":1,\"生物\":2,\"历史\":3,\"语文\":4,\"英语\":5," +
                "\"美术\":6,\"音乐\":7,\"政治\":8,\"电脑\":9,\"物理\":10,\"体育\":11,\"数学\":12,\"化学\":13,\"地理\":14}");
        ArrayList selectTeachers = JsonUtils.toJavaBean("[\"梁旭\",\"梁银萍\",\"李宇坤\",\"郑雅\",\"翁一正\",\"陈华海\",\"王晓燕\", \"李晶雯\", \"曾海娣\", \"黄静雯\", \"李琪琪\", " +
                "\"吴小燕\", \"赖舒芸\", \"梁春华\", \"陈玉清\", \"黄广珍\", \"方斯美\", \"庞景华\", \"黎祖战\", \"严培培\", \"陈列平\", \"袁育\", \"何环玲\", \"李广伟\", \"宋小清\", \"李靖\"" +
                ", \"李静\", \"何佩玲\", \"黎迪\", \"张小华\", \"江姗\", \"周詹辉\", \"陈怡敏\", \"刘亨维\", \"李豪贤\", \"刘夏婷\", \"陈丹\", \"陈思静\", \"戴曾红\", \"黄小红\", \"吴丹云\"" +
                ", \"严沛亮\", \"苏国豪\", \"谭龙飞\", \"李永前\", \"吴钰莹\", \"黄诗敏\", \"黄雪梅\", \"陈艳梅\"]", ArrayList.class);

        SimplePool simplePool = new SimplePool();
        try (Connection connection = simplePool.getConnection()) {
            connection.setAutoCommit(false);
            List<Teacher2> entities = PoiUtils.readFormFile(new File("C:\\Users\\Cheney\\Documents\\Project\\化州中学oa\\数据库\\teacher2.xlsx"), Teacher2.class);
            PreparedStatement preparedStatement
                    = connection.prepareStatement("insert into t_teacher(nickname,subject,phone,short_phone,create_date,tid)" +
                    " values(?,?,?,?,sysdate(),?)");
            entities = entities.stream().filter(e -> selectTeachers.contains(e.getName().trim())).
                    collect(Collectors.toList());
            int i = 342;
            for (Teacher2 entity : entities) {
                System.out.println(entity);
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setInt(2, (Integer) subMap.get(entity.getSubject()));
                preparedStatement.setString(3, entity.getPhone());
                if (entity.getShortPhone() == null || entity.getShortPhone().equals("null")) {
                    preparedStatement.setNull(4,Types.INTEGER);
                }else {
                    preparedStatement.setInt(4, Integer.parseInt(entity.getShortPhone()));
                }
                preparedStatement.setInt(5,i);
                preparedStatement.addBatch();
                i++;
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createTeacherSchedule() {

        HashMap<String, Object> gradeMap = new HashMap<>();
        gradeMap.put("初一", 1);
        gradeMap.put("初二", 2);
        gradeMap.put("初三", 3);
        gradeMap.put("高一", 4);
        gradeMap.put("高二", 5);
        gradeMap.put("高三", 6);

        Map<String, Object> subMap = (Map<String, Object>) JsonUtils.toMap("{\"行政\":1,\"生物\":2,\"历史\":3,\"语文\":4,\"英语\":5," +
                "\"美术\":6,\"音乐\":7,\"政治\":8,\"电脑\":9,\"物理\":10,\"体育\":11,\"数学\":12,\"化学\":13,\"地理\":14,\"班主任\":0,\"通技\":15}");

        SimplePool simplePool = new SimplePool();
        HashSet<String> classError = new HashSet<>();
        HashSet<String> snameError = new HashSet<>();
        try (Connection connection = simplePool.getConnection()) {
            connection.setAutoCommit(false);
            List<Schedule> entities = PoiUtils.readFormFile(new File("C:\\Users\\Cheney\\Documents\\Project\\化州中学oa\\数据库\\任课表.xlsx"), Schedule.class);
//            PreparedStatement preparedStatement
//                    = connection.prepareStatement("insert into t_teacher(tid,nickname,gender,birthday,subject,phone,short_phone,card_id,education,political,married,create_date)" +
//                    " values(?,?,?,?,?,?,?,?,?,?,?,sysdate())");
            List<Field> fields = Stream.of(Schedule.class.getDeclaredFields()).filter(e -> e.getDeclaredAnnotation(Name.class) != null).collect(Collectors.toList());
                PreparedStatement classQuery = connection.prepareStatement("select code from t_class where name = ?");
                PreparedStatement teacherQuery = connection.prepareStatement("select tid from t_teacher where nickname = ?");
            PreparedStatement insertSql = connection.prepareStatement("insert into t_teach_schedule(tid,grade_code,class_code,subject,semester,create_date) values(?,?,?,?,201901,sysdate())");
            for (Schedule entity : entities) {
                Integer grade = (Integer) gradeMap.get(entity.getGrade().trim());
                String classStr = entity.getClassStr().trim();
                classQuery.setString(1, classStr);
                ResultSet resultSet = classQuery.executeQuery();
                String classCode;
                try {
                    resultSet.next();
                    classCode = resultSet.getString("code");
                } catch (Exception e) {
                    classError.add(classStr);
                    continue;
                }
                for (Field e : fields) {
                    e.setAccessible(true);
                    String subjectStr = e.getDeclaredAnnotation(Name.class).value();
                    Integer subjectCode = (Integer) subMap.get(subjectStr);
                    String tName = ((String) e.get(entity)).trim();
                    if (StringUtils.isEmpty(tName)) continue;
                    System.out.println(tName);
                    teacherQuery.setString(1, tName);
                    ResultSet resultSet1 = teacherQuery.executeQuery();
                    resultSet1.next();
                    try {
                        String tid = resultSet1.getString("tid");
                        System.out.println("年级:" + grade + ",班级:" + classCode + ",科目:" + subjectCode + ",老师:" + tid);
                        insertSql.setString(1,tid);
                        insertSql.setInt(2,grade);
                        insertSql.setString(3,classCode);
                        insertSql.setInt(4,subjectCode);
                        insertSql.addBatch();
                    } catch (Exception e1) {
                        snameError.add(tName);
                        System.out.println(e1.getMessage() + ":" + tName);
                    }
                }
            }
            insertSql.executeBatch();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(snameError);
        System.out.println(classError);
    }

}
