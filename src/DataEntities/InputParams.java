package DataEntities;

public class InputParams {
    protected static final Mode MODE = Mode.FAIL;
    protected static final boolean PRINT_FAILED = false;
    public static final int MIN_TASK_CPU = 1;
    public static final int MAX_TASK_CPU = 10;
    public static final int MIN_ARRIVAL_TIME = 0;
    public static final int MIN_EXECUTION_TIME = 5;
    public static final int MAX_EXECUTION_TIME = 10;
    protected int maxCpuThreshold; // p max
    protected int minCpuThreshold; // p min
    protected int tasksQty; // r
    protected int attemptsQty; // z
    protected int processorsQty; // N
    protected int totalTime;

    public int getMaxCpuThreshold() {
        return maxCpuThreshold;
    }

    public int getMinCpuThreshold() {
        return minCpuThreshold;
    }

    public int getTasksQty() {
        return tasksQty;
    }

    public int getAttemptsQty() {
        return attemptsQty;
    }

    public int getProcessorsQty() {
        return processorsQty;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public Mode getMode() {
        return MODE;
    }

    public boolean getPrintFailed() {
        return PRINT_FAILED;
    }

    @Override
    public String toString() {
        return "InputParams{" +
                "maxCpuThreshold=" + maxCpuThreshold +
                ", minCpuThreshold=" + minCpuThreshold +
                ", tasksQty=" + tasksQty +
                ", attemptsQty=" + attemptsQty +
                ", processorsQty=" + processorsQty +
                ", totalTime=" + totalTime +
                '}';
    }
}
