package occam.edu.occambus.bus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author：琚涛
 * @date：2018/09/06
 * @description：
 */
public class OccamBus {

    private OccamBus() {
    }

    public static OccamBus getInstance() {
        return OccamBusHolder.occamBus;
    }

    private static class OccamBusHolder {

        private static OccamBus occamBus = new OccamBus();
    }

    /**
     * 缓存一个类里的所有加了 Subscribe 注解的方法
     * key 为 注册订阅者对象 Class , value为 订阅的标签、订阅者(方法)、方法参数
     */
    private static final Map<Class<?>, List<SubscriberMethod>> METHOD_CACHE = new HashMap<>();

    /**
     * 订阅集合
     * 发送事件的时候 通过Key(标签)查找所有对应的订阅者
     * key 为 订阅的标签 , value为 [订阅者(函数所在的对象)、[订阅的标签、订阅者(函数)、(函数)参数]]
     */
    private static final Map<String, List<Subscription>> SUBSCRIBES = new HashMap<>();

    /**
     * 对应对象中所有需要回调的标签 方便注销
     * key是订阅者(函数)所在类对象， value是该类中所有的订阅标签
     */
    private static final Map<Class<?>, List<String>> REGISTERS = new HashMap<>();

    public void clear() {
        METHOD_CACHE.clear();
        SUBSCRIBES.clear();
        REGISTERS.clear();
    }

    /**
     * 反注册
     */
    public void unregister(Object subscriber) {
        List<String> labels = REGISTERS.remove(subscriber.getClass());
        METHOD_CACHE.remove(subscriber.getClass());
        if (null != labels) {
            for (String label : labels) {
                //根据标签查找记录
                List<Subscription> subscriptions = SUBSCRIBES.get(label);
                if (null != subscriptions) {
                    Iterator<Subscription> iterator = subscriptions.iterator();
                    while (iterator.hasNext()) {
                        Subscription subscription = iterator.next();
                        //对象是同一个 则删除
                        if (subscription.getSubscriber() == subscriber) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    /**
     * 注册对象，寻找订阅的方法
     *
     * @param subscriber 订阅者
     */
    public void register(Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        //寻找订阅者对象加了订阅注解的方法
        List<SubscriberMethod> subscriberMethods = findSubscribe(subscriberClass);

        //为了方便注销
        List<String> labels = REGISTERS.get(subscriberClass);
        if (null == labels) {
            labels = new ArrayList<>();
        }

        //加入注册集合  key:标签 value:对应标签的所有函数
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
            String label = subscriberMethod.getLabel();
            if (!labels.contains(label)) {
                labels.add(label);
            }
            List<Subscription> subscriptions = SUBSCRIBES.get(label);
            if (subscriptions == null) {
                subscriptions = new ArrayList<>();
                SUBSCRIBES.put(label, subscriptions);
            }
            Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
            subscriptions.add(newSubscription);
        }
        REGISTERS.put(subscriberClass, labels);
    }

    /**
     * 找到被Subscribe注解的函数 并记录缓存
     *
     * @param subscriberClass
     */
    private List<SubscriberMethod> findSubscribe(Class<?> subscriberClass) {
        //先尝试去拿
        List<SubscriberMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
        if (null == subscriberMethods) {
            //如果拿不到就去存
            subscriberMethods = new ArrayList<>();
            //拿到这个类的所有方法
            Method[] methods = subscriberClass.getDeclaredMethods();
            //遍历所有方法
            for (Method method : methods) {
                //拿方法的 Subscribe 注解
                Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                //如果有标签
                if (null != subscribeAnnotation) {
                    //注解上的标签
                    String[] values = subscribeAnnotation.value();
                    //方法的参数类型
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (String value : values) {
                        //提前帮他开光，省的私有方法在 post 的时候报错
                        method.setAccessible(true);
                        SubscriberMethod subscriberMethod = new SubscriberMethod(value, method, parameterTypes);
                        subscriberMethods.add(subscriberMethod);
                    }
                }
            }
            METHOD_CACHE.put(subscriberClass, subscriberMethods);
        }
        return subscriberMethods;
    }

    /**
     * 发送事件给所有订阅者
     *
     * @param label
     * @param params
     */
    public void post(String label, Object... params) {
        //获得所有对应的订阅者
        List<Subscription> subscriptions = SUBSCRIBES.get(label);
        if(subscriptions==null){
            return;
        }
        for (Subscription subscription : subscriptions) {
            //组装参数，执行函数
            SubscriberMethod subscriberMethod = subscription.getSubscriberMethod();
            Class<?>[] parameterTypes = subscriberMethod.getParameterTypes();
            Object[] realParams = new Object[parameterTypes.length];
            if (null != params) {
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (i < params.length && parameterTypes[i].isInstance(params[i])) {
                        realParams[i] = params[i];
                    } else {
                        realParams[i] = null;
                    }
                }
            }
            try {
                subscriberMethod.getMethod().invoke(subscription.getSubscriber(), realParams);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
