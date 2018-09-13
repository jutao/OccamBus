package occam.edu.occambus.bus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：琚涛
 * @date：2018/09/13
 * @description：未决定的发送
 */
public class PendingPost {

    /**
     * 避免重复创建
     */
    private final static List<PendingPost> pendingPostPool = new ArrayList<PendingPost>();
    public final static int MAX_POOL_SIZE = 1000;

    Object[] realParams;
    Subscription subscription;
    String label;
    PendingPost next;

    public PendingPost(Object[] realParams, Subscription subscription, String label) {
        this.realParams = realParams;
        this.subscription = subscription;
        this.label = label;
    }

    /**
     * 获取 PendingPost 对象
     *
     * @param realParams
     * @param subscription
     * @param label
     * @return
     */
    static PendingPost obtainPendingPost(Object[] realParams, Subscription subscription, String label) {
        synchronized (pendingPostPool) {
            int size = pendingPostPool.size();
            if (size > 0) {
                PendingPost pendingPost = pendingPostPool.remove(size - 1);
                pendingPost.realParams = realParams;
                pendingPost.subscription = subscription;
                pendingPost.label = label;
                pendingPost.next = null;
                return pendingPost;
            }
        }
        return new PendingPost(realParams, subscription, label);
    }

    /**
     * 释放
     * @param pendingPost
     */
    static void releasePendingPost(PendingPost pendingPost) {
        pendingPost.realParams = null;
        pendingPost.label = null;
        pendingPost.subscription = null;
        pendingPost.next = null;
        synchronized (pendingPostPool) {
            if (pendingPostPool.size() < MAX_POOL_SIZE) {
                pendingPostPool.add(pendingPost);
            }
        }
    }
}
