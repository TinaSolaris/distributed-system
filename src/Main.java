import DataEntities.InputParams;
import DataEntities.InputParamsCreator;
import DataEntities.Task;
import Generator.ScanFile;
import Generator.TaskGenerator;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "F:\\Path\\data.txt";
    private static Scanner scanner; // Input stream

    public static void main(String[] args) {
        try {
            String file = DATA_FILE;
            scanner = new Scanner(System.in);
            var inputParamsCreator = new InputParamsCreator(scanner);

            while (true) {
                InputParams params = inputParamsCreator.getParams();
                if (params == null)
                    break;

                var generator = new TaskGenerator(params.getTasksQty(), params.getTotalTime());
                generator.writeToFile(file);

                ScanFile fileScanner = new ScanFile(file);
                fileScanner.scan();

                List<Task> tasks = fileScanner.getItems();

                var executor = new AlgorithmExecutor(tasks, params);
                executor.execute();
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
