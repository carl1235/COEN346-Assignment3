public class Process extends Thread {
    public int arrivalTime;
    public int burstTime;
    public int processId;
    public int burstTimeRemaining;

    public Process(int _arrivalTime, int _burstTime, int _id) {
        arrivalTime = _arrivalTime;
        burstTime = _burstTime;
        processId = _id;
        burstTimeRemaining = _burstTime;
    }

    //Process class that drains the burstTime
    public void run() {
        int previousTime = Clock.time;

        while (true) {
            if (previousTime + Clock.time <= previousTime)
                if (burstTimeRemaining != 0)
                    burstTimeRemaining--;


        }
    }

    public boolean isFinished() {
        if (burstTimeRemaining == 0)
            return true;

        return false;
    }
}