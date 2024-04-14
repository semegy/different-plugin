package different.processor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 注解用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface Different {
}
