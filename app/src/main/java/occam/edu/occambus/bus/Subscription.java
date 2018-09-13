package occam.edu.occambus.bus;

/**
 * @author：琚涛
 * @date：2018/09/06
 * @description：订阅
 */
public class Subscription {

    final Object subscriber;

    final SubscriberMethod subscriberMethod;

    public Subscription(Object subscriber, SubscriberMethod subscriberMethod) {
        this.subscriber = subscriber;
        this.subscriberMethod = subscriberMethod;
    }

}
