package different.processor.annotation;

import different.processor.CheckDiffEnum;
import different.processor.CheckResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;

/*diff结果跳过处理*/
public enum ResultSkipStrategy {

    /**
     * 忽略空值与实体对象的比对结果
     */
    IGNORE_NULL_OBJ((o1, o2) -> o1 == null || o2 == null),

    /**
     * 忽略空值与空字符串的比较
     */
    IGNORE_NULL_EMPTY((o1, o2) -> (o1 == null && "".equals(o2.toString())) || (o2 == null && "".equals(o1.toString()))),

    /**
     * 直接跳过
     */
    IGNORE((o1, o2) -> true),

    /**
     * 不跳过
     */
    NO_SKIP((o1, o2) -> false);

    BiFunction<Object, Object, Boolean> function;

    ResultSkipStrategy(BiFunction<Object, Object, Boolean> function) {
        this.function = function;
    }

    public static Map<String, ResultSkipStrategy> skipPropertyStrategy = new HashMap<>();

    public static CheckResult skip(CheckResult checkResult) {
        Iterator<CheckDiffEnum> iterator = checkResult.getCheckEnumList().iterator();
        while (iterator.hasNext()) {
            CheckDiffEnum checkDiff = iterator.next();
            String keyName = checkDiff.getKeyName();
            ResultSkipStrategy resultSkipStrategy = skipPropertyStrategy.get(keyName);
            if (resultSkipStrategy != null) {
                Object diffValue = checkDiff.getDiffValue();
                Object nextValue = checkDiff.getNextValue();
                if (resultSkipStrategy.function.apply(diffValue, nextValue)) {
                    iterator.remove();
                }
            }
        }
        return checkResult;
    }
}
