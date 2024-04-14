package different.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class UniquePropertyStrategy {

    public static Map<String, Function[]> uniquePropertyStrategy = new HashMap<>();


    public static void putUniquePropertyStrategy(String propertyPath, Function ...uniqueKey) {
        uniquePropertyStrategy.put(propertyPath, uniqueKey);
    }


}
