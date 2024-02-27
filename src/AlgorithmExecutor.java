import DataEntities.*;
import Algorithms.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class AlgorithmExecutor {
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("0.0000");
    private final List<Task> tasks;
    private final InputParams inputParams;

    public AlgorithmExecutor(List<Task> tasks, InputParams inputParams) {
        if (tasks == null)
            throw new NullPointerException("The tasks should not be null.");
        if (inputParams == null)
            throw new NullPointerException("The inputParams should not be null.");

        this.tasks = tasks;
        this.inputParams = inputParams;
    }

    public void execute() {
        System.out.println();
        System.out.println("SUMMARY for: " + inputParams);

        execute(TaskAllocationAlgorithmType.RANDOM);
        execute(TaskAllocationAlgorithmType.MAX_THRESHOLD);
        execute(TaskAllocationAlgorithmType.MIN_THRESHOLD);
    }

    private void execute(TaskAllocationAlgorithmType taskAllocationAlgorithmType) {
        TaskAllocationAlgorithm algorithm = create(taskAllocationAlgorithmType);
        algorithm.process();

        var builder = new StringBuilder();
        builder.append("\tAlgorithm: " + taskAllocationAlgorithmType);
        builder.append(", Average CPU: " + DOUBLE_FORMAT.format(algorithm.getAverageCpu()));
        builder.append(", Average Deviation: " + DOUBLE_FORMAT.format(algorithm.getAverageDeviation()));
        builder.append(", Load Queries: " + algorithm.getLoadQueries());
        builder.append(", Task Migrations: " + algorithm.getTaskMigrations());
        if (inputParams.getPrintFailed())
            builder.append(", Failed Tasks: " + algorithm.getFailedTasks());

        System.out.println(builder);
    }

    private TaskAllocationAlgorithm create(TaskAllocationAlgorithmType algorithmType) {
        var processors = createProcessors();
        switch (algorithmType) {
            case RANDOM -> {
                return new RandomTaskAllocation(tasks, processors, inputParams);
            }
            case MAX_THRESHOLD -> {
                return new MaxThresholdTaskAllocation(tasks, processors, inputParams);
            }
            case MIN_THRESHOLD -> {
                return new MinThresholdTaskAllocation(tasks, processors, inputParams);
            }
            default -> {
                throw new IllegalArgumentException("The algorithmType: " + algorithmType + " is not supported.");
            }
        }
    }

    private HashMap<Integer, Processor> createProcessors() {
        var processors = new HashMap<Integer, Processor>(inputParams.getProcessorsQty());

        for (int i = 0; i < inputParams.getProcessorsQty(); i++) {
            var processor = new Processor(i);
            processors.put(i, processor);
        }

        return processors;
    }
}