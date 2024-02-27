package Algorithms;

import DataEntities.*;

import java.util.*;

public abstract class AbstractTaskAllocationAlgorithm implements TaskAllocationAlgorithm {
    protected final List<Task> tasks;
    protected final List<Task> failedTasks;
    // Key - processor index; Value - processor
    protected final HashMap<Integer, Processor> processors;
    protected final InputParams inputParams;
    private int taskMigrations;

    protected AbstractTaskAllocationAlgorithm(List<Task> tasks, HashMap<Integer, Processor> processors, InputParams inputParams) {
        if (tasks == null)
            throw new NullPointerException("The tasks should not be null.");
        if (processors == null)
            throw new NullPointerException("The processors should not be null.");
        if (processors.size() == 0)
            throw new IllegalArgumentException("The processors should not be empty.");
        if (inputParams == null)
            throw new NullPointerException("The inputParams should not be null.");

        Collections.sort(tasks, new ArrivalTimeComparator());

        this.tasks = tasks;
        this.failedTasks = new ArrayList<>();
        this.inputParams = inputParams;
        this.processors = processors;
        this.taskMigrations = 0;
    }

    protected Processor selectRandomProcessor(HashSet<Integer> triedProcessorIndices) {
        if (triedProcessorIndices == null)
            throw new NullPointerException("The triedProcessorIndices cannot be null.");

        // If all existing processors have been tried, there is no sense to continue.
        if (triedProcessorIndices.size() == processors.size())
            return null;

        // Select processor indices which have not been tried yet.
        List<Integer> indices = processors.keySet().stream()
                .filter(i -> !triedProcessorIndices.contains(i)).toList();

        if (indices.size() == 0) // Just in case
            throw new NullPointerException("Cannot select a random processor properly.");

        Random random = new Random();
        int index = random.nextInt(indices.size());
        int processorIndex = indices.get(index);

        triedProcessorIndices.add(processorIndex);
        Processor processor = processors.get(processorIndex);

        if (processor == null)
            throw new NullPointerException("Cannot select a random processor properly.");

        return processor;
    }

    protected List<Task> getArrivedTasks(int time) {
        if (time < 0)
            throw new IllegalArgumentException("The time cannot be negative.");

        return tasks.stream().filter(t -> t.getArrivalTime() == time).toList();
    }

    // The processor can take a new task if the CPU load is less than the max threshold.
    // But it is assumed that (maxThresholdCpu + the task CPU) is always not more than 100%.
    protected boolean canExecuteAsBelowMax(Processor processor, int time) {
        if (processor == null)
            throw new NullPointerException("The processor cannot be null.");
        if (time < 0)
            throw new IllegalArgumentException("The time cannot be negative.");

        return processor.getCpuLoad(time) < inputParams.getMaxCpuThreshold();
    }

    protected void executeWithTaskMigration(Processor processor, Task task, int time) {
        if (processor == null)
            throw new NullPointerException("The processor cannot be null.");
        if (task == null)
            throw new NullPointerException("The task cannot be null.");
        if (time < 0)
            throw new IllegalArgumentException("The time cannot be negative.");

        processor.executeTask(task);
        taskMigrations++;
    }

    protected void executeWithValidation(Processor processor, Task task, int time) {
        if (processor == null)
            throw new NullPointerException("The processor cannot be null.");
        if (task == null)
            throw new NullPointerException("The task cannot be null.");
        if (time < 0)
            throw new IllegalArgumentException("The time cannot be negative.");

        if (inputParams.getMode() == Mode.FAIL) {
            if (processor.getCpuLoad(time) + task.getCpu() <= Processor.MAX_CPU_LOAD) {
                processor.executeTask(task);
            } else {
                failedTasks.add(task);
            }
        } else {
            processor.executeTask(task);
        }
    }

    @Override
    public double getAverageCpu() {
        double total = 0.0;

        if (processors.size() == 0)
            return total;

        for (Processor processor : processors.values()) {
            total += processor.getAverageCpu(inputParams.getTotalTime());
        }

        return total / processors.size();
    }

    @Override
    public double getAverageDeviation() {
        double total = 0.0;

        if (processors.size() == 0)
            return total;

        for (Processor processor : processors.values()) {
            total += processor.getAverageDeviation(inputParams.getTotalTime());
        }

        return total / processors.size();
    }

    @Override
    public int getTaskMigrations() {
        return taskMigrations;
    }

    @Override
    public int getLoadQueries() {
        int total = 0;

        for (Processor processor : processors.values()) {
            total += processor.getLoadQueries();
        }

        return total;
    }

    @Override
    public int getFailedTasks() {
        return failedTasks.size();
    }

    @Override
    public String getName() {
        return getAlgorithmType().toString();
    }
}