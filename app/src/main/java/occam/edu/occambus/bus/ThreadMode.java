package occam.edu.occambus.bus;

/**
 * @author：琚涛
 * @date：2018/09/13
 * @description：线程
 */
public enum ThreadMode {

    /**
     * 在发送线程
     */
    POSTING,

    /**
     * 主线程
     */
    MAIN,

    /**
     * 线程池
     */
    BACKGROUND,

    /**
     * 开个子线程
     */
    ASYNC
}
