package protobuf;

/**
 * @author by chenyi
 * @date 2022/6/16
 */
public class Main {

    public static void main(String[] args) {
        TestEntity1 entity1 = TestEntity1.newBuilder().setId(1).setName("test").build();
        TestEntity2 entity2 = TestEntity2.newBuilder().setMessage("test message").setType("test type").build();
        TestEntity1 entity11 = entity1.toBuilder().setEntity2(entity2).build();
        System.out.println(entity1);
        System.out.println(entity2);
        System.out.println(entity11);
    }

}
