package Algorithms;

public interface TaskAllocationAlgorithm {
    TaskAllocationAlgorithmType getAlgorithmType();

    String getName();

    void process();

    double getAverageCpu();

    double getAverageDeviation();

    int getLoadQueries();

    int getTaskMigrations();
    int getFailedTasks();
}
