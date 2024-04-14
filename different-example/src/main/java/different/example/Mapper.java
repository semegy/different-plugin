package different.example;


import different.demo.test2.Test2;
import different.processor.DiffMapper;
import different.processor.annotation.Different;
import different.processor.annotation.ResultSkipStrategy;

public class  Mapper extends DiffMapper {

    // 跳过结果的属性跳跃策略
    static {
        ResultSkipStrategy.skipPropertyStrategy.put("Test2_2", ResultSkipStrategy.IGNORE_NULL_OBJ);
    }

    @Different
    private Test2 test2;

}
