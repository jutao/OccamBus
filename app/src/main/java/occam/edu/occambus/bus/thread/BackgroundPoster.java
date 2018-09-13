package occam.edu.occambus.bus.thread;

import android.util.Log;

import occam.edu.occambus.bus.OccamBus;
import occam.edu.occambus.bus.PendingPost;
import occam.edu.occambus.bus.PendingPostQueue;

/**
 * @author：琚涛
 * @date：2018/09/13
 * @description：
 */
final public class BackgroundPoster implements Runnable {

    private final PendingPostQueue queue;
    private final OccamBus occamBus;

    private volatile boolean executorRunning;

    public BackgroundPoster(OccamBus occamBus) {
        this.occamBus = occamBus;
        queue = new PendingPostQueue();
    }

     synchronized public void enqueue(PendingPost pendingPost) {
        queue.enqueue(pendingPost);
        if (!executorRunning) {
            executorRunning = true;
            occamBus.getExecutorService().execute(this);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                PendingPost pendingPost = queue.poll(PendingPost.MAX_POOL_SIZE);
                if (pendingPost == null) {
                    synchronized (this) {
                        pendingPost = queue.poll();
                        if (pendingPost == null) {
                            executorRunning = false;
                            return;
                        }
                    }
                }
                occamBus.invokeSubscriber(pendingPost);
            }
        }catch (InterruptedException e){
            Log.w("Event", Thread.currentThread().getName() + " was interruppted", e);
        }finally {
            executorRunning = false;
        }

    }
}
