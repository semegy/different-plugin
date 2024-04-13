
package different.processor;

public class CheckDiffEnum<T> {

    private String keyName;

    private T value;

    private T nextValue;

    public String getKeyName() {
        return keyName;
    }

    public T getDiffValue() {
        return value;
    }

    public void setDiffValue(T diffValue) {
        this.value = diffValue;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public T getNextValue() {
        return nextValue;
    }

    public void setNextValue(T nextValue) {
        this.nextValue = nextValue;
    }

    public CheckDiffEnum(String keyName, T value, T nextValue) {
        this.keyName = keyName;
        this.value = value;
        this.nextValue = nextValue;
    }
}
