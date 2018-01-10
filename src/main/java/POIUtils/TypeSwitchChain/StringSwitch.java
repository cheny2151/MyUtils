package POIUtils.TypeSwitchChain;

public class StringSwitch extends BaseTypeSwitch {

    @Override
    public Object transform(Class target, Object value) {
        if (String.class.isAssignableFrom(target)) {
            return value.toString();
        }
        return hasNext() ? getNext().transform(target, value) : null;
    }

}
