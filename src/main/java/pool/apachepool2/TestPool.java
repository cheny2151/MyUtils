package pool.apachepool2;

/**
 * @author by chenyi
 * @date 2022/1/11
 */
public class TestPool {

    public static void main(String[] args) throws Exception {
        StringPool stringPool = new StringPool();
        for (int i = 0; i < 8; i++) {
            String s = stringPool.borrowObject(1000);
            System.out.println(s);
            stringPool.returnObject(s);
        }
        stringPool.close();
    }

}
