package different.example.test;

import different.example.Mapper;
import different.demo.test.Test;
import different.demo.test2.Test2;
import different.processor.CheckResult;

public class TestDiff {

    public static void main(String[] args) {
        Test test1 = new Test("1", "2", 3, new Test("a", null, null));
        Test test2 = new Test("3", "2", 3, new Test("a", "4", null));
        Test2 test21 = new Test2(1, 2, 4, 7, test1);
        Test2 test22 = new Test2(1, 4, 4, 6, test2);
        test22.objectMap.put("aaa", "ddddddd");
        CheckResult differ = Mapper.differ(test21, test22);
    }
}
