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
    final String label;

    /**
     * 方法
     */
    final Method method;

    /**
     * 参数的类型
     */
    final Class<?>[] parameterTypes;

    final ThreadMode threadMode;

    public SubscriberMethod(String label, Method method, Class<?>[] parameterTypes, ThreadMode threadMode) {
        this.label = label;
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.threadMode = threadMode;
    }
}
