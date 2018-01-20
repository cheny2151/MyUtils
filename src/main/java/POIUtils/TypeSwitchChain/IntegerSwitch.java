package POIUtils.TypeSwitchChain;

/**
 * Integer类型转换器
 */
public class IntegerSwitch extends BaseTypeSwitch {

    public IntegerSwitch() {
    }

    public IntegerSwitch(BaseTypeSwitch next) {
        super(next);
    }

    /**
     * 统一在转换链前做value的非空判断
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T transform(Class<T> target, Object value) {
        if (Integer.class.isAssignableFrom(target)) {
            String transform = value.toString();
            if (transform.contains(".")) {
                transform = transform.substring(0, transform.indexOf("."));
            }
            return (T) Integer.valueOf(transform);
        }
        return hasNext() ? getNext().transform(target, value) : null;
    }

}
