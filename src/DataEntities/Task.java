package DataEntities;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {
    private final static Pattern valueExtract = Pattern.compile("^(?<Name>\\w+)\\s(?<ArrivalTime>\\w+)\\s(?<ExecutionTime>\\w+)\\s(?<Cpu>\\d+)$");

    private final String name; // Task/process name
    private final int arrivalTime; // Time of task/process arrival
    private final int executionTime; // Time of task/process execution or burst time
    private final int cpu; // A specific, different share of the processor's computing power, e.g., 3%, 5%, 8%, etc.

    public Task(String line) throws Exception {
        if (line == null || line.isBlank())
            throw new IllegalArgumentException("The line should not be null, empty, or consist of white-space characters only.");

        Matcher matcher = valueExtract.matcher(line);
        if (!matcher.find())
            throw new IllegalArgumentException("The line cannot be parsed as task data.");

        this.name = matcher.group("Name");
        this.arrivalTime = parseInteger(matcher.group("ArrivalTime"));
        this.executionTime = parseInteger(matcher.group("ExecutionTime"));
        this.cpu = parseInteger(matcher.group("Cpu"));
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public int getCpu() {
        return cpu;
    }

    public int getFinishTime() {
        return arrivalTime + executionTime;
    }

    private int parseInteger(String value) throws Exception {
        try {
            return Integer.parseInt(value);
        }
        catch (Exception ex) {
            throw new Exception("The value: '" + value + "' cannot be parsed as a valid integer number.");
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", executionTime=" + executionTime +
                ", finishTime=" + getFinishTime() +
                ", cpu=" + cpu +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Task task = (Task)o;

        return arrivalTime == task.arrivalTime &&
                executionTime == task.executionTime &&
                cpu == task.cpu &&
                name.equals(task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, arrivalTime, executionTime, cpu);
    }
}