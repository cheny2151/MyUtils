package pool.apachepool2;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author by chenyi
 * @date 2022/1/11
 */
public class StringPoolObjectFactory implements PooledObjectFactory<String> {

    private int time = 0;

    @Override
    public void activateObject(PooledObject<String> pooledObject) throws Exception {
        System.out.println("activateObject");
    }

    @Override
    public void destroyObject(PooledObject<String> pooledObject) throws Exception {
        System.out.println("destroyObject");
    }

    @Override
    public PooledObject<String> makeObject() throws Exception {
        return new DefaultPooledObject<>("test" + time++);
    }

    @Override
    public void passivateObject(PooledObject<String> pooledObject) throws Exception {
        System.out.println("passivateObject");
    }

    @Override
    public boolean validateObject(PooledObject<String> pooledObject) {
        System.out.println("validateObject");
        return false;
    }
}
