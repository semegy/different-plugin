package different.processor;

public class DiffMapper {
    protected  String packageName;

    public static <S> CheckResult differ(S t1, S t2) {
        try {
            assert t1 != null;
            assert t2 != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CheckDiff checkDiffer = CheckDiff.buildCheckDiff(t1);
        checkDiffer.keyName = t1.getClass().getSimpleName();
        return checkDiffer.diff(t1, t2);
    }

}
