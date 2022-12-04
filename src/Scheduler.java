import java.io.*;

import java.util.*;

public class Scheduler extends Thread {
    static LinkedList<Process> processes;
    int numberOfCores;
    int numberOfProcesses;

    public Scheduler(int _numberOfCores, int _numberOfProcesses, LinkedList<Process> _process) {
        numberOfCores = _numberOfCores;
        numberOfProcesses = _numberOfProcesses;
        processes = _process;
    }

    public void run() {
        File outputFile = new File("src/TextFiles/output.txt");

        try {
            FileWriter outputFileWrite = new FileWriter(outputFile, false);
            PrintWriter outputFileWriter = new PrintWriter(outputFileWrite);

            Clock c = new Clock();
            c.start();

            while (true) {
                for (Process p : processes) {
                    //Starts or resumes process
                    if (!p.isAlive()) {
                        outputFileWriter.println("Time " + c.time + ", Process " + p.processId + ", Started");
                        System.out.println("Time " + c.time + ", Process " + p.processId + ", Started ");
                        p.start();
                    }

                    if (p.isFinished()) {
                        outputFileWriter.println("Time " + c.time + ", Process " + p.processId + ", Finished");
                        System.out.println("Time " + c.time + ", Process " + p.processId + ", Finished");
                        p.stop();
                    }
                }
                c.stop();
            }
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }
    }

}