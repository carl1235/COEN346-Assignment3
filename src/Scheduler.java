import java.util.*;

public class Scheduler extends Thread {
    static LinkedList<Process> processes;
    static LinkedList<Process> readyQueue = new LinkedList<>();
    int numberOfCores;
    static int numberOfProcesses;
    int processesDone = 0;
    public Scheduler(int _numberOfCores, int _numberOfProcesses, LinkedList<Process> _process) {
        numberOfCores = _numberOfCores;
        numberOfProcesses = _numberOfProcesses;
        processes = _process;
    }

    public void run() {
            Clock c = new Clock();
            c.start();

            while (true) {
                setReadyQueue(c.time);


                for (Process p : readyQueue) {
                    //Starts or resumes process
                    if (!p.isAlive()) {
                        System.out.println("Clock: " + c.time + ", Process " + p.processId + ": Started.");
                        p.start();
                    }

                    if (p.isFinished()) {
                        System.out.println("Clock: " + c.time + ", Process " + p.processId + ": Finished.");
                        p.stop();
                        readyQueue.remove(p);
                        processesDone++;
                        break;
                    }
                }
                if (readyQueue.isEmpty() && numberOfProcesses == processesDone) {
                    c.stop();
                    break;
                }
            }

    }

    void setReadyQueue(int time) {
        int numofprocesses = 0;
        for (int i = 0; i < numberOfProcesses; i++) {
            if (processes.get(i).arrivalTime * 1000 <= time && processes.get(i).burstTime != 0 && !processes.get(i).isAlive() && !processes.get(i).isFinished() && numofprocesses <= numberOfCores) {
                readyQueue.add(processes.get(i));
                numofprocesses++;
            }
        }
    }
}