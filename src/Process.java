public class Process extends Thread {
    public int arrivalTime;
    public int burstTime;
    public int processId;
    public int burstTimeRemaining;

    public int timePerProcess;

    public int lastAcessTime;

    public Process(int _arrivalTime, int _burstTime, int _id) {
        arrivalTime = _arrivalTime;
        burstTime = _burstTime;
        processId = _id;
        burstTimeRemaining = _burstTime;
    }


    //Process class that drains the burstTime
    public void run() {
        while (true) {
            if (burstTimeRemaining != 0)
                burstTimeRemaining--;

            try {
                Thread.sleep(1300L);
            } catch (InterruptedException e) {
                System.out.println("Process Sleep Exception");
            }
        }
    }

    public boolean isFinished(){
        if (burstTimeRemaining == 0)
            return true;

        return false;
    }
}