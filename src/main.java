import java.io.*;
import java.util.*;

public class main {

    static LinkedList<String[]> commands = new LinkedList<>();

    public static void main(String[] args) throws Exception {

        HashMap<Integer, Integer> process = new HashMap<>();
        LinkedList<Process> processes = new LinkedList<>();

        int numberOfCores = 0;
        int numberOfProcesses = 0;

        System.out.println("Program Started...");
        PrintStream sout = new PrintStream(new FileOutputStream("src/TextFiles/output.txt", false));
        PrintStream standard = System.out;
// ...
        System.setOut(sout);



        try {
            File path = new File("src/TextFiles/processes.txt");
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


        try {
            File path = new File("src/TextFiles/commands.txt");
            Scanner scanner = new Scanner(path);

            while (scanner.hasNextLine()) {
                String[] values = scanner.nextLine().split("\\s+");
                commands.add(values);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot read from commands.txt");
        }

        VirtualMemoryManager vmm = new VirtualMemoryManager(maximumPages);
        vmm.start();

        Scheduler scheduler = new Scheduler(numberOfCores, numberOfProcesses, processes);
        scheduler.start();

        try{
            scheduler.join();
            vmm.stop();
            scheduler.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.setOut(standard);
        System.out.println("Program Finished.");


    }

}

