package different.processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static different.processor.UniquePropertyStrategy.uniquePropertyStrategy;

public class ArraysCheckDiff extends CheckDiff<Object[]>{

    public ArraysCheckDiff() {
        super();
    }
    public ArraysCheckDiff(CheckResult checkResult) {
        super(checkResult);
    }

    public ArraysCheckDiff(CheckResult checkResult, String keyName) {
        super(checkResult, keyName);
    }

    @Override
    public CheckResult diff(Object[] t1, Object[] t2) {
        Function[] strings = uniquePropertyStrategy.get(this.keyName);
        HashMap<String, Object> objectObjectHashMap1 = new HashMap<>();
        HashMap<String, Object> objectObjectHashMap2 = new HashMap<>();
        if (strings != null) {
            for (Object o : t1) {
                String uniqueKey = Arrays.stream(strings).map(s -> s.apply(o).toString()).reduce((o1, o2) -> o1 + o2).get();
                objectObjectHashMap1.put(uniqueKey, o);
            }
            for (Object o : t2) {
                String uniqueKey = Arrays.stream(strings).map(s -> s.apply(o).toString()).collect(Collectors.toList()).toString();
                objectObjectHashMap2.put(uniqueKey, o);
            }
            objectObjectHashMap1.forEach((key, value) -> {
                Object value2 = objectObjectHashMap2.get(key);
                if (value2 == null) {
                    addDiff(value, value2, this.keyName + key);
                } else {
                    if (value2 == null) {
                        addDiff(value, value2, keyName + "_" + key);
                    } else if ( value instanceof Map && value2 instanceof Map) {
                        MapCheckDiff mapCheckDiff = new MapCheckDiff(this.checkResult, this.keyName = key);
                        mapCheckDiff.diff((Map) value, (Map) value2);
                    } else if (!value.getClass().getTypeName().equals(value2.getClass().getTypeName())) {
                        addDiff(value,value2, keyName + "_" + key);
                    } else if (value instanceof Comparable && value2 instanceof Comparable) {
                        if (((Comparable) value).compareTo(value2) != 0) {
                            addDiff(value, value2, keyName + "_" + key);
                        }
                    } else if (value.getClass().isArray() && value2.getClass().isArray()){
                        ArraysCheckDiff arraysCheckDiff = new ArraysCheckDiff(this.checkResult, key);
                        arraysCheckDiff.diff((Object[])value, (Object[])value2);
                    } else {
                        CheckDiff checkDiff = CheckDiff.buildCheckDiff(value);
                        checkDiff.checkResult = this.checkResult;
                        checkDiff.keyName = keyName + "_" + key;
                        checkDiff.diff(value, value2);
                    }
                }
                objectObjectHashMap2.remove(key);
            });
            objectObjectHashMap2.forEach((key, value2) -> {
                addDiff(null, value2, this.keyName + key);
            });
        } else {
            boolean isMax = t1.length >= t2.length ? true : false;
            Object[] max = t1.length >= t2.length ? t1 : t2;
            Object[] min = t1.length >= t2.length ? t2 : t1;
            for (int i = 0; i < max.length; i++) {
                Object value = max[i];
                Object value2 = i >= min.length ? null : min[i];
                if (isMax) {
                    extracted(i, value, value2);
                } else {
                    extracted(i, value2, value);
                }
            }
        }

        return this.checkResult;
    }

    private void extracted(int i, Object value, Object value2) {
        if (value == null && value2 == null) {
        } else if (value == null || value2 == null) {
            addDiff(value, value2, keyName + "_" + "[" + i + "]");
        } else if (!value.getClass().getTypeName().equals(value2.getClass().getTypeName())) {
            addDiff(value, value2, keyName + "_" + "[" + i + "]");
        } else if (value instanceof Comparable && value2 instanceof Comparable) {
            if (((Comparable) value).compareTo(value2) != 0) {
                addDiff(value, value2, keyName + "_" + "[" + i + "]");
            }
        } else if (value instanceof Map) {
            MapCheckDiff mapCheckDiff = new MapCheckDiff(this.checkResult);
            mapCheckDiff.keyName = keyName + "_" + "[" + i + "]";
            mapCheckDiff.differ(value, value2);
        } else if (value.getClass().isArray()) {
            ArraysCheckDiff arraysCheckDiff = new ArraysCheckDiff(this.checkResult);
            arraysCheckDiff.keyName = keyName + "_" + "[" + i + "]";
            arraysCheckDiff.differ(value, value2);
        } else {
            CheckDiff checkDiff = CheckDiff.buildCheckDiff(value);
            checkDiff.checkResult = this.checkResult;
            checkDiff.keyName = keyName + "_" + "[" + i + "]";
            checkDiff.diff(value, value2);
        }
    }

}
