package different.processor;

import java.util.ArrayList;
import java.util.List;

public class CheckResult {

    List<CheckDiffEnum> checkEnumList;

    {
        checkEnumList = new ArrayList<>();
    }

    public List<CheckDiffEnum> getCheckEnumList() {
        return checkEnumList;
    }

    public void setCheckEnumList(List<CheckDiffEnum> checkEnumList) {
        this.checkEnumList = checkEnumList;
    }

    public void addDiff(CheckDiffEnum checkDiffEnum) {
        checkEnumList.add(checkDiffEnum);
    }

    public boolean success() {
        return checkEnumList.isEmpty();
    }
}
