package occam.edu.occambus.bus.thread;

import occam.edu.occambus.bus.OccamBus;
import occam.edu.occambus.bus.PendingPost;
import occam.edu.occambus.bus.PendingPostQueue;

/**
 * @author：琚涛
 * @date：2018/09/13
 * @description：
 */
final public class AsyncPoster implements Runnable{
    private final PendingPostQueue queue;
    private final OccamBus occamBus;

    public AsyncPoster(OccamBus occamBus) {
        this.occamBus = occamBus;
        queue=new PendingPostQueue();
    }

    public void enqueue(PendingPost pendingPost) {
        queue.enqueue(pendingPost);
        occamBus.getExecutorService().execute(this);
    }

    @Override
    public void run() {
        PendingPost pendingPost=queue.poll();
        if(pendingPost==null){
            throw new IllegalStateException("No pending post available");
        }
        occamBus.invokeSubscriber(pendingPost);
    }
}
