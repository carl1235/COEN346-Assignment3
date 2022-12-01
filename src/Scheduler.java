import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Scheduler extends Thread {
    public static int timeQuantum;

    static ArrayList<Process> processes;

    //HashMap for the ReadyQueue where it prepares all the ready users and processes
    static Map<Character, ArrayList<Process>> mainList = new HashMap<>();
    File diskFile = new File("src/TextFiles/vm.txt");
    //Constructor
    public Scheduler(int _timeQuantum, ArrayList<Process> _process) {
        timeQuantum = _timeQuantum;
        processes = new ArrayList<>(_process);
    }

    public void run() {
        int maximumPages = 0;
        File outputFile = new File("src/TextFiles/output.txt");


        try {
            FileWriter fw = new FileWriter(diskFile, false);
            Scanner scanner = new Scanner(new File("src/TextFiles/memconfig.txt"));

            while (scanner.hasNextLine()){
                maximumPages = Integer.parseInt(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }

        VirtualMemoryManager VMM = new VirtualMemoryManager(maximumPages);

        VMM.start();

        try {

            FileWriter outputFileWrite = new FileWriter(outputFile, false);
            PrintWriter outputFileWriter = new PrintWriter(outputFileWrite);

            Clock c = new Clock();
            c.start();

            while (true) {

                //FIFO all processes by arrival time

                if (mainList.isEmpty()) break;
                for (Process p : processes) {
                    //Starts or resumes process
                    if (!p.isAlive()) {
                        outputFileWriter.println("Time " + c.time + ", Process " + p.processId + ", Started");
                        System.out.println("Time " + c.time + ", Process " + p.processId + ", Started ");
                        p.start();
                    } else {
                        outputFileWriter.println("Time " + c.time + ", Process " + p.processId + ", Resumed");
                        System.out.println("Time " + c.time + ", Process " + p.processId + ", Resumed ");
                        p.resume();
                    }

                    //sleeps process depending on the amount of burstTime remaining or run the full time per process allocated
                    try {
                        if (p.burstTimeRemaining <= p.timePerProcess) {
                            sleep(p.burstTimeRemaining * 1001L);
                        } else {
                            sleep(p.timePerProcess * 1001L);
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Scheduler Sleep Exception");
                    }

                    outputFileWriter.println("Time " + c.time + ", Process " + p.processId + ", Paused");
                    System.out.println("Time " + c.time + ", Process " + p.processId + ", Paused ");
                    p.suspend();

                    //Checks if the process drained all its time then it stops
                    if (p.isFinished()) {
                        outputFileWriter.println("Time " + c.time + ", Process " + p.processId + ", Finished");
                        System.out.println("Time " + c.time + ", Process " + p.processId + ", Finished");
                        p.stop();

                    }
                }
            }
            outputFileWriter.close();
            c.stop();
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }
    }

}