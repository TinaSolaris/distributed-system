package Generator;

import DataEntities.InputParams;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TaskGenerator {
    private static final String NAME_PREFIX = "t";
    private final int tasksQty;
    private final int totalTime;

    public TaskGenerator(int tasksQty, int totalTime) {
        if (tasksQty <= 0)
            throw new IllegalArgumentException("The tasksQty should be positive.");
        if (totalTime <= 0)
            throw new IllegalArgumentException("The totalTime should be positive.");

        this.tasksQty = tasksQty;
        this.totalTime = totalTime;
    }

    public void writeToFile(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank())
            throw new IllegalArgumentException("The filePath should not be null, empty, or consist of white-space characters only.");

        FileWriter writer = new FileWriter(filePath);
        writer.write("name arrivalTime executionTime cpu");

        for (int taskIndex = 0; taskIndex < tasksQty; taskIndex++) {
            writer.write("\n" + createTaskName(taskIndex) + " " +
                    generateRandom(InputParams.MIN_ARRIVAL_TIME, totalTime - 1) + " " +
                    generateRandom(InputParams.MIN_EXECUTION_TIME, InputParams.MAX_EXECUTION_TIME) + " " +
                    generateRandom(InputParams.MIN_TASK_CPU, InputParams.MAX_TASK_CPU));
        }

        writer.close();
    }

    private int generateRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static String createTaskName(int index) {
        return NAME_PREFIX + index;
    }

    private static final String DATA_FILE = "F:\\Path\\data.txt";

    public static void main(String[] args) {
        try {
            var generator = new TaskGenerator(10, 20);
            generator.writeToFile(DATA_FILE);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
