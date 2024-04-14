package different.demo.test2;


import different.demo.test.Test;
import different.processor.annotation.Skip;

import java.util.HashMap;
import java.util.Map;

public class Test2 {

    public Integer a;

    @Skip
    public Integer c;

    public Integer d;

    public long e;

    public Test test;

    public Map<String, Object> objectMap;

    public Test2(Integer a, Integer c, Integer d, long e) {
        this.a = a;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    public Test2(Integer a, Integer c, Integer d, long e, Test test) {
        this.a = a;
        this.c = c;
        this.d = d;
        this.e = e;
        this.test = test;
        this.objectMap = new HashMap<>();
        this.objectMap.put("tttt", test);
        this.objectMap.put("aaa", this.a);
        this.objectMap.put("bbbb", this.d);
    }

}
