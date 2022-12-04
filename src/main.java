import java.io.*;
import java.util.*;

public class main {

    public static void main(String[] args) {
        File path = new File("src/TextFiles/processes.txt");
        HashMap<Integer, Integer> process = new HashMap<>();
        LinkedList<Process> processes = new LinkedList<>();
        int numberOfCores = 0;
        int numberOfProcesses = 0;


        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            numberOfCores = Integer.parseInt(br.readLine());
            numberOfProcesses = Integer.parseInt(br.readLine());

            for (int i = 0; i < numberOfProcesses; i++) {
                String[] values = br.readLine().split("\\s+");
                process.put(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
            }

            int id = 0;
            for (int i : process.keySet()) {
                processes.add(new Process(i, process.get(i), id));
                id++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File could not be found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scheduler scheduler = new Scheduler(numberOfCores, numberOfProcesses, processes);
        scheduler.start();

        int maximumPages = 0;
        try {
            new FileWriter(new File("src/TextFiles/vm.txt"), false);
            Scanner scanner = new Scanner(new File("src/TextFiles/memconfig.txt"));

            while (scanner.hasNextLine()) {
                maximumPages = Integer.parseInt(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }

        VirtualMemoryManager vmm = new VirtualMemoryManager(maximumPages);
        vmm.start();
    }

}

