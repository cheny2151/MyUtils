package POIUtils.TypeSwitchChain;

public abstract class BaseTypeSwitch {

    private BaseTypeSwitch next;

    public BaseTypeSwitch() {
    }

    public BaseTypeSwitch(BaseTypeSwitch next) {
        this.next = next;
    }

    public abstract <T> T transform(Class<T> target,Object value);

    public BaseTypeSwitch getNext() {
        return next;
    }

    public void setNext(BaseTypeSwitch next) {
        this.next = next;
    }

    public boolean hasNext() {
        return next != null;
    }

}
