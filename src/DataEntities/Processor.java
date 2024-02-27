package DataEntities;

import java.util.HashSet;
import java.util.List;

public class Processor {
    public static final int MAX_CPU_LOAD = 100;
    private final String name;
    private final int index;
    private final HashSet<Task> tasks;
    private int loadQueries;

    public Processor(int index) {
        this.index = index;
        this.name = "Processor " + index;
        this.tasks = new HashSet<>();
        this.loadQueries = 0;
    }

    public void executeTask(Task task) {
        if (task == null)
            throw new NullPointerException("The task should not be null.");

        tasks.add(task);
    }

    private List<Task> getActiveTasks(int time) {
        List<Task> tasks = this.tasks.stream().filter(t ->
            t.getArrivalTime() <= time &&
            t.getFinishTime() > time).toList();

        return tasks;
    }

    public int getCpuLoad(int time) {
        loadQueries++;

        List<Task> activeTasks = getActiveTasks(time);

        if (activeTasks.size() == 0)
            return 0;

        return activeTasks.stream().mapToInt(i -> i.getCpu()).sum();
    }

    public int getLoadQueries() {
        return loadQueries;
    }

    public double getAverageCpu(int finishTime) {
        if (finishTime < 0)
            throw new IllegalArgumentException("The finishTime should not be negative");

        double total = 0.0;

        if (finishTime == 0)
            return total;

        for (int time = 0; time < finishTime; time++) {
            total += getCpuLoad(time);
        }

        return total / finishTime;
    }

    public double getAverageDeviation(int finishTime) {
        if (finishTime < 0)
            throw new IllegalArgumentException("The finishTime should not be negative.");

        double total = 0.0;

        if (finishTime == 0)
            return total;

        double averageCpu = getAverageCpu(finishTime);

        for (int time = 0; time < finishTime; time++) {
            double delta = getCpuLoad(time) - averageCpu;

            if (delta < 0)
                delta = -delta;

            total += delta;
        }

        return total / finishTime;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Processor{" +
                "name='" + name + '\'' +
                ", index=" + index +
                ", loadQueries=" + loadQueries +
                ", tasks=" + tasks.size() +
                '}';
    }
}