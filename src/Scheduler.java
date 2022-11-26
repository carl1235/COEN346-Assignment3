import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Scheduler extends Thread {
    public static int timeQuantum;

    static ArrayList<Process> processes;

    //HashMap for the ReadyQueue where it prepares all the ready users and processes
    static Map<Character, ArrayList<Process>> mainList = new HashMap<>();

    //Constructor
    public Scheduler(int _timeQuantum, ArrayList<Process> _process) {
        timeQuantum = _timeQuantum;
        processes = new ArrayList<>(_process);
    }

    public void run() {
        File outputFile = new File("/Users/Carl/Desktop/Uni/Fall_2022/COEN_346/ProcessScheduling/src/output.txt");

        try {

            FileWriter outputFileWrite = new FileWriter(outputFile, false);
            PrintWriter outputFileWriter = new PrintWriter(outputFileWrite);

            Clock c = new Clock();
            c.start();

            while (true) {

                if (mainList.isEmpty()) break;
                    for (Process p : processes) {
                        //Starts or resumes process
                        if (!p.isAlive()) {
                            outputFileWriter.println("Time " + c.time + ", User " + i + ", Process " + p.processId + ", Started");
                            System.out.println("Time " + c.time + ", User " + i + ", Process " + p.processId + ", Started ");
                            p.start();
                        } else {
                            outputFileWriter.println("Time " + c.time + ", User " + i + ", Process " + p.processId + ", Resumed");
                            System.out.println("Time " + c.time + ", User " + i + ", Process " + p.processId + ", Resumed ");
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

                        outputFileWriter.println("Time " + c.time + ", User " + i + ", Process " + p.processId + ", Paused");
                        System.out.println("Time " + c.time + ", User " + i + ", Process " + p.processId + ", Paused ");
                        p.suspend();

                        //Checks if the process drained all its time then it stops
                        if (p.isFinished()) {
                            outputFileWriter.println("Time " + c.time + ", User " + i + ", Process " + p.processId + ", Finished");
                            System.out.println("Time " + c.time + ", User " + i + ", Process " + p.processId + ", Finished");
                            p.stop();

                        }
                }
            }
            outputFileWriter.close();
            c.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }