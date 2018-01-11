package POIUtils.TypeSwitchChain;

/**
 * Double类型转换器
 */
public class DoubleSwitch extends BaseTypeSwitch {

    public DoubleSwitch() {
    }

    public DoubleSwitch(BaseTypeSwitch next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T transform(Class<T> target, Object value) {
        if (Double.class.isAssignableFrom(target)) {
            return (T) Double.valueOf(value.toString());
        }
        return hasNext() ? getNext().transform(target, value) : null;
    }

}
