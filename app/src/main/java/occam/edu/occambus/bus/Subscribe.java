package occam.edu.occambus.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author：琚涛
 * @date：2018/09/06
 * @description：
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    String[] value();
    ThreadMode threadMode() default ThreadMode.POSTING;
}
