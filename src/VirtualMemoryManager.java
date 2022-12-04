import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class VirtualMemoryManager extends Thread {
    static HashMap<String, Integer> variable = new HashMap<>();
    static LinkedList<String> variableLinkedList = new LinkedList<>();
    static File diskFile = new File("src/TextFiles/vm.txt");
    static int memoryDiskSpace = 0;
    static int maximumPages;

    VirtualMemoryManager(int _maximumPages) {
        maximumPages = _maximumPages;
    }

    public static synchronized void store(String variableId, int value) {
        try {
            sleep(randomTime());
        } catch (InterruptedException e) {
            System.out.println("Scheduler Sleep Exception");
        }

        if (memoryDiskSpace < maximumPages) {
            variable.put(variableId, value);
            variableLinkedList.add(variableId);

            memoryDiskSpace++;
            System.out.println("Store (mem): Variable " + variableId + ", Value: " + value);
        } else {
            writeToFile(variableId, value);
        }
    }

    public static synchronized void release(String variableId) {
        try {
            sleep(randomTime());
        } catch (InterruptedException e) {
            System.out.println("Scheduler Sleep Exception");
        }

        if (memoryDiskSpace != 0) {
            if (lookup(variableId) == -1)
                System.out.println("Variable " + variableId + " doesn't exist");
            else {
                variable.remove(variableId);
                variableLinkedList.remove(variableId);

                memoryDiskSpace--;
                System.out.println("Release: Variable " + variableId);
            }
        }
    }

    public static synchronized int lookup(String variableId) {
        try {
            sleep(randomTime());
        } catch (InterruptedException e) {
            System.out.println("Scheduler Sleep Exception");
        }

        if (variable.get(variableId) != null) {
            String temp_variable = variableLinkedList.removeFirst();
            variableLinkedList.add(temp_variable);
            System.out.println("Lookup: Variable " + variableId + ", Value: " + variable.get(variableId));
            return variable.get(variableId);
        } else {
            try {
                Scanner scanner = new Scanner(diskFile);

                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split("\\s+");

                    if (line[0].equals(variableId)) {
                        if (memoryDiskSpace < maximumPages) {
                            store(line[0], Integer.parseInt(line[1]));
                            swap(line[0], Integer.parseInt(line[1]), "", 0);
                        } else {
                            String LA_variableId = variableLinkedList.removeFirst();

                            swap(line[0], Integer.parseInt(line[1]), LA_variableId, variable.get(LA_variableId));
                            variableLinkedList.add(line[0]);

                        }
                        System.out.println("Lookup: Variable " + line[0] + ", Value: " + Integer.parseInt(line[1]));
                        return Integer.parseInt(line[1]);

                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("Couldn't read file");
            }

        }
        System.out.println("Variable " + variableId + " not found");
        return -1;
    }

    public static void swap(String d_variableId, int d_value, String m_variableId, int m_value) {
        File newFile = new File("src/TextFiles/temp.txt");
        try {
            FileWriter newWriter = new FileWriter(newFile, false);
            Scanner reader = new Scanner(diskFile);

            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();
                if (currentLine.equals(d_variableId + "\t" + d_value)) {
                    if (m_variableId.equals("")) {
                        newWriter.write("");
                    } else
                        newWriter.write(m_variableId + "\t" + m_value + "\n");
                    continue;
                }
                newWriter.write(currentLine + "\n");
            }

            newFile.renameTo(diskFile);

            reader.close();
            newWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Cannot write to file");
        }


        variable.remove(m_variableId);
        variable.put(d_variableId, d_value);
        System.out.println("SWAP: Variable " + d_variableId + " with Variable " + m_variableId);
    }

    public static void writeToFile(String variableId, int value) {
        try {
            FileWriter writer = new FileWriter(diskFile, true);
            writer.write(variableId + "\t" + value + "\n");
            System.out.println("Store (disk): Variable " + variableId + ", Value: " + value);

            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't write to file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static int randomTime() {
        int time = 0;

        //Generate random time between 1, 1000

        return time;
    }
}
