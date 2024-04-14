package different.processor;

import different.processor.annotation.ResultSkipStrategy;

public class DiffMapper {
    public static <S> CheckResult differ(S t1, S t2) {
        try {
            assert t1 != null;
            assert t2 != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CheckDiff checkDiffer = CheckDiff.buildCheckDiff(t1);
        checkDiffer.keyName = t1.getClass().getSimpleName();
        CheckResult diff = checkDiffer.diff(t1, t2);
        // 跳跃属性
        ResultSkipStrategy.skip(diff);
        return diff;
    }

    public static void putSkipPropertyStrategy(String propertyPath, ResultSkipStrategy skipStrategy) {
        ResultSkipStrategy.skipPropertyStrategy.put(propertyPath, skipStrategy);
    }

    public static void putUniquePropertyStrategy(String propertyPath, ResultSkipStrategy skipStrategy) {
        ResultSkipStrategy.skipPropertyStrategy.put(propertyPath, skipStrategy);

    }
}
