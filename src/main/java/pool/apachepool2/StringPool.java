package pool.apachepool2;

import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * @author by chenyi
 * @date 2022/1/11
 */
public class StringPool extends GenericObjectPool<String> {

    public StringPool() {
        super(new StringPoolObjectFactory());
    }

}
