package occam.edu.occambus.bus.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import occam.edu.occambus.bus.EventBusException;
import occam.edu.occambus.bus.OccamBus;
import occam.edu.occambus.bus.PendingPost;
import occam.edu.occambus.bus.PendingPostQueue;

/**
 * @author：琚涛
 * @date：2018/09/13
 * @description：
 */
final public class HandlerPoster extends Handler {

    private final PendingPostQueue queue;
    private final int maxMillisInsideHandleMessage;
    private final OccamBus occamBus;
    private boolean handlerActive;

    public HandlerPoster(Looper looper, int maxMillisInsideHandleMessage, OccamBus occamBus) {
        super(looper);
        this.maxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
        this.occamBus = occamBus;
        queue = new PendingPostQueue();
    }

    public synchronized void enqueue(PendingPost pendingPost) {
        queue.enqueue(pendingPost);
        if (!handlerActive) {
            handlerActive = true;
            if (!sendMessage(obtainMessage())) {
                throw new EventBusException("Could not send handler message");
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        boolean rescheduled = false;
        try {
            long started = SystemClock.uptimeMillis();
            while (true) {
                PendingPost pendingPost = queue.poll();
                if (pendingPost == null) {
                    synchronized (this) {
                        pendingPost = queue.poll();
                        if (pendingPost == null) {
                            handlerActive = false;
                            return;
                        }
                    }
                }
                occamBus.invokeSubscriber(pendingPost);
                long timeInMethod = SystemClock.uptimeMillis() - started;
                if (timeInMethod >= maxMillisInsideHandleMessage) {
                    if (!sendMessage(obtainMessage())) {
                        throw new EventBusException("Could not send handler message");
                    }
                    rescheduled = true;
                    return;
                }
            }
        } finally {
            handlerActive = rescheduled;
        }
    }
}
