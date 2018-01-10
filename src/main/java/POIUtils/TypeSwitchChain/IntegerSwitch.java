package POIUtils.TypeSwitchChain;

public class IntegerSwitch extends BaseTypeSwitch {

    @Override
    public Object transform(Class target, Object value) {
        if (Integer.class.isAssignableFrom(target)) {
            return Integer.valueOf(value.toString());
        }
        return hasNext() ? getNext().transform(target, value) : null;
    }

}
