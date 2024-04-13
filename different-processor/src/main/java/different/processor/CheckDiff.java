package different.processor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class CheckDiff<T> {

    public T t1;
    public T t2;
    public String keyName;

    public CheckResult checkResult;

    public List<Supplier<Boolean>> checkDiffList = new ArrayList<>();

    public CheckDiff() {
        checkResult = new CheckResult();
    }
    public CheckDiff(CheckResult checkResult) {
        this.checkResult = checkResult;
    }

    public CheckDiff(CheckResult checkResult, String keyName) {
        this.checkResult = checkResult;
        this.keyName = this.keyName == null ? keyName : this.keyName + keyName;
    }
    public static <S> CheckResult differ(S t1, S t2) {
        CheckDiff checkDiffer = CheckDiff.buildCheckDiff(t1);
        checkDiffer.keyName = t1.getClass().getSimpleName();
        return checkDiffer.diff(t1, t2);
    }

    static <S> CheckDiff buildCheckDiff(S t1) {
        try {
            Class<?> aClass = Class.forName(t1.getClass().getName() + "CheckDiff");
            Constructor<?> constructor = aClass.getConstructor(null);
            return (CheckDiff) constructor.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public CheckResult diff(T t1, T t2) {
        if (t1 == null && t2 == null) {
            return checkResult;
        }
        if (t1 == null) {
            addDiff(keyName, null, t2.toString());
            return checkResult;
        }
        if (t2 == null) {
            addDiff(keyName, t1.toString(), null);
            return checkResult;
        }

        this.t1 = t1;
        this.t2 = t2;
        checkDiffList.forEach(Supplier::get);
        return checkResult;
    };

    <S> boolean addDiff(S s, S s1, String diffKey) {
        checkResult.addDiff(new CheckDiffEnum(diffKey, s, s1));
        return false;
    }

    public <S> boolean childDiff(S s, S s1, String diffKey) {
        if (s == null && s1 == null) {
            return true;
        } else if (s == null || s1 == null) {
            addDiff(s, s1, diffKey);
            return false;
        } else if (((Comparable) s).compareTo(s1) == 0) {
            return true;
        } else {
            addDiff(s, s1 , diffKey);
            return false;
        }
    }
}
