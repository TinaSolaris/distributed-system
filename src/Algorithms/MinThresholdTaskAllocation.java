package Algorithms;

import DataEntities.InputParams;
import DataEntities.Processor;
import DataEntities.Task;

import java.util.*;

public class MinThresholdTaskAllocation extends AbstractTaskAllocationAlgorithm {
    public MinThresholdTaskAllocation(List<Task> tasks, HashMap<Integer, Processor> processors, InputParams inputParams) {
        super(tasks, processors, inputParams);
    }

    @Override
    public TaskAllocationAlgorithmType getAlgorithmType() {
        return TaskAllocationAlgorithmType.MIN_THRESHOLD;
    }

    @Override
    public void process() {
        for (int time = 0; time < inputParams.getTotalTime(); time++) {
            List<Task> arrivedTasks = getArrivedTasks(time);

            for (Task task : arrivedTasks) {
                HashSet<Integer> triedProcessorIndices = new HashSet<>();

                // The task receiver processor is the processor which receives the task initially.
                Processor taskReceiver = selectRandomProcessor(triedProcessorIndices);

                // The task receiver checks itself as first.
                if (canExecuteAsBelowMax(taskReceiver, time)) {
                    taskReceiver.executeTask(task);
                    continue;
                }

                boolean isExecuted = false;

                // Try to find another processor with CPU load less than the min threshold.
                while (true) {
                    Processor processor = selectRandomProcessor(triedProcessorIndices);

                    // Null means that all processors have been tried and
                    // all have their CPU load more than the min threshold
                    if (processor == null)
                        break;

                    // Check if the selected random processor has CPU load less than the min threshold.
                    // If so, redirect and execute the task there.
                    if (canExecuteAsBelowMin(processor, time)) {
                        executeWithTaskMigration(processor, task, time);
                        isExecuted = true;
                        break;
                    }
                }

                // If the task has not been executed, execute on the task receiver processor.
                if (!isExecuted)
                    executeWithValidation(taskReceiver, task, time);
            }
        }
    }

    // The processor can take a new task if the CPU load is less than the mix threshold.
    private boolean canExecuteAsBelowMin(Processor processor, int time) {
        return processor.getCpuLoad(time) < inputParams.getMinCpuThreshold();
    }
}
