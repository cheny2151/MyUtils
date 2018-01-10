package POIUtils.TypeSwitchChain;

import POIUtils.ReadProperty;
import org.junit.Test;

/**
 * Created by cheny on 2018/1/10.
 */
public class DoubleSwitch extends BaseTypeSwitch {

    @Override
    public Object transform(Class target, Object value) {
        if (Double.class.isAssignableFrom(target)) {
            return Double.valueOf(value.toString());
        }
        return hasNext() ? getNext().transform(target, value) : null;
    }

    @Test
    public void test(){


    }

}
