import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class VirtualMemoryManager extends Thread {
    HashMap<String, Integer> variable = new HashMap<>();
    LinkedList<String> variableLinkedList = new LinkedList<>();
    File diskFile = new File("src/TextFiles/vm.txt");
    int memoryDiskSpace = 0;
    int maximumPages;

    VirtualMemoryManager(int _maximumPages) {
        maximumPages = _maximumPages;
    }
    //Semaphore for release and lookup

    //FOR TESTING
//    public void run(){
//        store("1",5);
//        store("2",2);
//        store("5",1);
//        store("4",5);
//        lookup("5");
//        release("1");
//        lookup("4");
//    }

    public synchronized void store(String variableId, int value) {
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

    public synchronized void release(String variableId) {
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
                System.out.println("Variable " + variableId + " released");
            }
        }
    }

    public int lookup(String variableId) {
        try {
            sleep(randomTime());
        } catch (InterruptedException e) {
            System.out.println("Scheduler Sleep Exception");
        }

        if (variable.get(variableId) != null) {
            String temp_variable = variableLinkedList.removeFirst();
            variableLinkedList.add(temp_variable);
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
                        }
                        else {
                            String LA_variableId = variableLinkedList.removeFirst();

                            swap(line[0], Integer.parseInt(line[1]), LA_variableId, variable.get(LA_variableId));
                            variableLinkedList.add(line[0]);

                        }
                        return Integer.parseInt(line[0]);

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

    public void swap(String d_variableId, int d_value, String m_variableId, int m_value) {
        File newFile = new File("src/TextFiles/temp.txt");
        try {
            FileWriter newWriter = new FileWriter(newFile, false);
            Scanner reader = new Scanner(diskFile);

            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();
                if (currentLine.equals(d_variableId + "\t" + d_value)) {
                    if (m_variableId.equals("")){
                        newWriter.write("");
                    }
                    else
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

    public void writeToFile(String variableId, int value) {
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

    public int randomTime(){
        int time = 0;

        //Generate random time between 1, 1000

        return time;
    }
}
