package POIUtils.TypeSwitchChain;

/**
 * String类型转换器
 */
public class StringSwitch extends BaseTypeSwitch {

    public StringSwitch() {
    }

    public StringSwitch(BaseTypeSwitch next) {
        super(next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T transform(Class<T> target, Object value) {
        if (String.class.isAssignableFrom(target)) {
            return (T) value.toString();
        }
        return hasNext() ? getNext().transform(target, value) : null;
    }

}
