package DataEntities;

import java.util.Scanner;

public class InputParamsCreator {
    private static final String EXIT = "exit";
    private static final int DEFAULT_MAX_THRESHOLD_CPU = 80;
    private static final int DEFAULT_MIN_THRESHOLD_CPU = 50;
    private static final int DEFAULT_TASKS_QTY = 2000;
    private static final int DEFAULT_ATTEMPTS_QTY = 5;
    private static final int DEFAULT_PROCESSORS_QTY = 50;
    private static final int DEFAULT_TOTAL_TIME = 20;
    private final Scanner scanner; // The input stream

    public InputParamsCreator(Scanner scanner) {
        if (scanner == null)
            throw new NullPointerException("The scanner should not be null.");

        if (DEFAULT_MAX_THRESHOLD_CPU + InputParams.MAX_TASK_CPU > Processor.MAX_CPU_LOAD)
            throw new IllegalArgumentException("The DEFAULT_MAX_THRESHOLD_CPU=" + DEFAULT_MAX_THRESHOLD_CPU +
                    " is too large. It would not allow to add a new task with the MAX_CPU=" + InputParams.MAX_TASK_CPU);
        if (DEFAULT_MAX_THRESHOLD_CPU <= DEFAULT_MIN_THRESHOLD_CPU)
            throw new IllegalArgumentException("The DEFAULT_MAX_THRESHOLD_CPU=" + DEFAULT_MAX_THRESHOLD_CPU +
                    " is less than the DEFAULT_MIN_THRESHOLD_CPU=" + DEFAULT_MIN_THRESHOLD_CPU);

        this.scanner = scanner;
    }

    public InputParams getParams() {
        var params = new InputParams();
        System.out.println();
        System.out.println("Enter execution params or \"exit\".");

        params.minCpuThreshold = readPositiveInteger("Min Threshold CPU (p min)", DEFAULT_MIN_THRESHOLD_CPU);
        if (params.minCpuThreshold == Integer.MIN_VALUE)
            return null;

        if (params.minCpuThreshold + InputParams.MAX_TASK_CPU > Processor.MAX_CPU_LOAD) {
            System.out.println("\t\tThe minThresholdCpu is too large. It would not allow to add a new task with the max CPU: " + InputParams.MAX_TASK_CPU);
            System.out.println("\t\tSetting the Default=" + DEFAULT_MIN_THRESHOLD_CPU + " instead.");
            params.minCpuThreshold = DEFAULT_MIN_THRESHOLD_CPU;
        }

        params.maxCpuThreshold = readPositiveInteger("Max Threshold CPU (p max)", DEFAULT_MAX_THRESHOLD_CPU);
        if (params.maxCpuThreshold == Integer.MIN_VALUE)
            return null;

        if (params.maxCpuThreshold + InputParams.MAX_TASK_CPU > Processor.MAX_CPU_LOAD) {
            System.out.println("\t\tThe maxThresholdCpu is too large. It would not allow to add a new task with the max CPU: " + InputParams.MAX_TASK_CPU);
            System.out.println("\t\tSetting the Default=" + DEFAULT_MAX_THRESHOLD_CPU + " instead.");
            params.maxCpuThreshold = DEFAULT_MAX_THRESHOLD_CPU;
        }

        if (params.maxCpuThreshold <= params.minCpuThreshold) {
            System.out.println("\t\tThe maxThresholdCpu is less than or equal to the minThresholdCpu=" + params.minCpuThreshold);
            System.out.println("\t\tSetting the Default values instead.");
            params.minCpuThreshold = DEFAULT_MIN_THRESHOLD_CPU;
            params.maxCpuThreshold = DEFAULT_MAX_THRESHOLD_CPU;
        }

        params.tasksQty = readPositiveInteger("Tasks Quantity (r)", DEFAULT_TASKS_QTY);
        if (params.tasksQty == Integer.MIN_VALUE)
            return null;

        params.attemptsQty = readPositiveInteger("Attempts Quantity (z)", DEFAULT_ATTEMPTS_QTY);
        if (params.attemptsQty == Integer.MIN_VALUE)
            return null;

        params.processorsQty = readPositiveInteger("Processors Quantity (N)", DEFAULT_PROCESSORS_QTY);
        if (params.processorsQty == Integer.MIN_VALUE)
            return null;

        params.totalTime = readPositiveInteger("Total Time", DEFAULT_TOTAL_TIME);
        if (params.totalTime == Integer.MIN_VALUE)
            return null;

        return params;
    }

    private int readPositiveInteger(String caption, int defaultValue) {
        return readInteger(caption, defaultValue, true);
    }

    private int readInteger(String caption, int defaultValue, boolean isPositive) {
        System.out.print("\t" + caption + " or press Enter (for Default=" + defaultValue + "): ");
        String line = scanner.nextLine();
        if (line.equalsIgnoreCase(EXIT))
            return Integer.MIN_VALUE;

        if (line.isBlank())
            return defaultValue;

        try {
            int value = Integer.parseInt(line);

            if (isPositive && value <= 0)
                throw new IllegalArgumentException("The value should be positive.");

            return value;
        } catch (Exception ex) {
            System.out.println("\t\tError: " + ex);
            System.out.println("\t\tSetting the Default=" + defaultValue + " instead.");

            return defaultValue;
        }
    }
}