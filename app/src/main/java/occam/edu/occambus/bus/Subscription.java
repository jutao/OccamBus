package occam.edu.occambus.bus;

/**
 * @author：琚涛
 * @date：2018/09/06
 * @description：订阅
 */
public class Subscription {

    private Object subscriber;

    private SubscriberMethod subscriberMethod;

    public Subscription(Object subscriber, SubscriberMethod subscriberMethod) {
        this.subscriber = subscriber;
        this.subscriberMethod = subscriberMethod;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    public SubscriberMethod getSubscriberMethod() {
        return subscriberMethod;
    }
}
