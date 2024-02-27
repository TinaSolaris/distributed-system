package Algorithms;

import DataEntities.InputParams;
import DataEntities.Processor;
import DataEntities.Task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class RandomTaskAllocation extends AbstractTaskAllocationAlgorithm {
    public RandomTaskAllocation(List<Task> tasks, HashMap<Integer, Processor> processors, InputParams inputParams) {
        super(tasks, processors, inputParams);
    }

    @Override
    public TaskAllocationAlgorithmType getAlgorithmType() {
        return TaskAllocationAlgorithmType.RANDOM;
    }

    @Override
    public void process() {
        for (int time = 0; time < inputParams.getTotalTime(); time++) {
            List<Task> arrivedTasks = getArrivedTasks(time);

            for (Task task : arrivedTasks) {
                HashSet<Integer> triedProcessorIndices = new HashSet<>();

                // The task receiver processor is the processor which receives the task initially.
                // It just tries to send the task to another processor in this algorithm at first.
                Processor taskReceiver = selectRandomProcessor(triedProcessorIndices);

                int attempt = 0;
                boolean isExecuted = false;

                // Try to find another processor with CPU load less than the max threshold
                // for the allowed quantity of attempts maximum.
                while (attempt < inputParams.getAttemptsQty()) {
                    Processor processor = selectRandomProcessor(triedProcessorIndices);

                    // Null means that all processors have been tried and
                    // all have their CPU load more than the max threshold.
                    if (processor == null)
                        break;

                    // Check if the selected random processor has CPU load less than the max threshold.
                    // If so, redirect and execute the task there.
                    if (canExecuteAsBelowMax(processor, time)) {
                        executeWithTaskMigration(processor, task, time);
                        isExecuted = true;
                        break;
                    }

                    attempt++;
                }

                // If the task has not been executed, execute on the task receiver processor.
                if (!isExecuted)
                    executeWithValidation(taskReceiver, task, time);
            }
        }
    }
}
