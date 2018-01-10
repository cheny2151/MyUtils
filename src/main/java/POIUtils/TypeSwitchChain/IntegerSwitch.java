package POIUtils.TypeSwitchChain;

public class IntegerSwitch extends BaseTypeSwitch {

    @Override
    public Object transform(Class target, Object value) {
        if (target.equals(Integer.class)) {
            return Integer.valueOf(value.toString());
        }
        return hasNext() ? getNext().transform(target, value) : null;
    }
}
