package different.demo.test;


public class Test {


    public String a;

    public String b;

    public Integer c;

    public Test test;

    public Test(String a, String b, Integer c, Test test) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.test = test;
    }


    public Test(String a, String b, Integer c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }


}
