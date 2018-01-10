package POIUtils.TypeSwitchChain;

public class StringSwitch extends BaseTypeSwitch {

    @Override
    public Object transform(Class target, Object value) {
        if (target.equals(String.class)) {
            return value.toString();
        }
        return hasNext() ? getNext().transform(target, value) : null;
    }

}
