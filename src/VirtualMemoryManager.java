import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class VirtualMemoryManager extends Thread {
    HashMap<String, Integer> variable = new HashMap<>();
    int memoryDiskSpace = 0;
    int maximumPages;

    VirtualMemoryManager(int _maximumPages) {
        maximumPages = _maximumPages;
    }
    //Semaphore for release and lookup

    public void store(String variableId, int value) {
        if (memoryDiskSpace < maximumPages) {
            variable.put(variableId, value);

            memoryDiskSpace++;
            System.out.println("Store (mem): Variable " + variableId + ", Value: " + value);
        } else {
            writeToFile(variableId, value, false);
        }

    }

    public void release(String variableId) {
        if (memoryDiskSpace != 0) {
            if (lookup(variableId) == -1)
                System.out.println("Variable " + variableId + " doesn't exist");
            else {
                variable.remove(variableId);
                memoryDiskSpace--;
                System.out.println("Variable " + variableId + " released");
            }
        }
    }

    public int lookup(String variableId) {
        if (variable.get(variableId) != null) {
            //update last access of variableId
            return variable.get(variableId);
        } else {
            try {
                Scanner scanner = new Scanner(main.diskFile);

                while (scanner.hasNextLine()) {
                    String[] line = scanner.nextLine().split("\\s+");

                    if (line[0].equals(variableId)) {
                        if (memoryDiskSpace < maximumPages)
                            store(line[0], Integer.parseInt(line[1]));
                        else {
                            //TEMPORARY//
                            String LA_variableId = "4";
                            //check for smallest value of last access

                            swap(line[0], Integer.parseInt(line[1]), LA_variableId, variable.get(LA_variableId));
                            return Integer.parseInt(line[1]);
                        }

                    }
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("Couldn't read file");
            }

        }
        return -1;
    }

    public void swap(String d_variableId, int d_value, String m_variableId, int m_value) {
        File newFile = new File("/Users/Carl/Desktop/Uni/Fall_2022/COEN_346/Assignment3/src/temp.txt");
        try {
            FileWriter newWriter = new FileWriter(newFile, false);
            Scanner reader = new Scanner(main.diskFile);

            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();
                if (currentLine.equals(d_variableId + "\t" + d_value)) {
                    newWriter.write(m_variableId + "\t" + m_value + "\n");
                    continue;
                }
                newWriter.write(currentLine + "\n");
            }

            newFile.renameTo(main.diskFile);

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

    public void writeToFile(String variableId, int value, boolean replace) {
        try {
            FileWriter writer = new FileWriter(main.diskFile, true);
            writer.write(variableId + "\t" + value + "\n");
            System.out.println("Store (disk): Variable " + variableId + ", Value: " + value);

            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't write to file");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
