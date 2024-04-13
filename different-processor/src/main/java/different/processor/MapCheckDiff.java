package different.processor;

import java.util.Map;
import java.util.Set;

public class MapCheckDiff extends CheckDiff<Map> {

    public MapCheckDiff(CheckResult checkResult) {
        super(checkResult);
    }

    public MapCheckDiff() {
    }

    public MapCheckDiff(CheckResult checkResult, String keyName) {
        super(checkResult);
        this.keyName = keyName;
    }

    @Override
    public CheckResult diff(Map t1, Map t2) {
        if (t1 == null && t2 == null) {
            return this.checkResult;
        } else if (t1 == null || t1.isEmpty()) {
            if (t2.isEmpty()) {
                return this.checkResult;
            } else {
                t2.forEach((key, value) -> {
                    addDiff(null, value, this.keyName + "_" + key);
                });
            }
        } else if (t2 == null || t2.isEmpty()) {
            if (t1.isEmpty()) {
                return this.checkResult;
            } else {
                t1.forEach((key, value) -> {
                    addDiff(value, null, this.keyName + "_" + key);
                });
            }
        } else {
            Set set = t1.keySet();
            t2.keySet().forEach(key -> {
                if (!set.contains(key)) {
                    set.add(key);
                }
            });
            set.forEach(key -> {
                Object value = t1.get(key);
                Object value2 = t2.get(key);
                if (value == null && value2 == null) {
                } else if (value == null || value2 == null) {
                    addDiff(value, value2, keyName + "_" + key);
                } else if (!value.getClass().getTypeName().equals(value2.getClass().getTypeName())) {
                    addDiff(value, value2, keyName + "_" + key);
                } else if (value instanceof Comparable && value2 instanceof Comparable) {
                    if (((Comparable) value).compareTo(value2) != 0) {
                        addDiff(value, value2, keyName + "_" + key);
                    } else {
                        return;
                    }
                } else if (value instanceof Map) {
                    MapCheckDiff mapCheckDiff = new MapCheckDiff(this.checkResult);
                    mapCheckDiff.keyName = keyName + "_" + key;
                    mapCheckDiff.differ(value, value2);
                } else {
                    CheckDiff checkDiff = CheckDiff.buildCheckDiff(value);
                    checkDiff.checkResult = this.checkResult;
                    checkDiff.keyName = keyName + "_" + key;
                    checkDiff.diff(value, value2);
                }
            });
        }
        return this.checkResult;
    }
}
