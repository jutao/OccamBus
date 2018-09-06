package occam.edu.occambus.bus;

import java.lang.reflect.Method;

/**
 * @author：琚涛
 * @date：2018/09/06
 * @description：
 */
public class SubscriberMethod {

    /**
     * 标签
     */
    private String label;

    /**
     * 方法
     */
    private Method method;

    /**
     * 参数的类型
     */
    private Class<?>[] parameterTypes;

    public SubscriberMethod(String label, Method method, Class<?>[] parameterTypes) {
        this.label = label;
        this.method = method;
        this.parameterTypes = parameterTypes;
    }

    public String getLabel() {
        return label;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }
}
