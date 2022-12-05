import java.util.Random;
import java.util.concurrent.Semaphore;

public class Process extends Thread {
    public int arrivalTime;
    public int burstTime;
    public int processId;
    public int burstTimeRemaining;

    static Semaphore semaphore = new Semaphore(1);
    public Process(int _arrivalTime, int _burstTime, int _id) {
        arrivalTime = _arrivalTime;
        burstTime = _burstTime;
        processId = _id;
        burstTimeRemaining = _burstTime;
    }


    public void run() {
        int starttime = Clock.time;
        while (true) {
                if (Clock.time >= starttime + burstTime*1000)
                    burstTimeRemaining = 0;

            try {
                sleep(randomTime());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

                try {
                    runCommand();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        }
    }

    public static int randomTime() {
        //Generate random time between 1, 1000
        Random r = new Random();
        return r.nextInt(1,1000);
    }

    public void runCommand() throws InterruptedException {
        semaphore.acquire();

        if (VirtualMemoryManager.currentCommand != main.commands.size()) {
            String[] s = main.commands.get(VirtualMemoryManager.currentCommand);

            switch (s[0]) {
                case "Store" -> VirtualMemoryManager.store(s[1], Integer.parseInt(s[2]), processId);
                case "Release" -> VirtualMemoryManager.release(s[1], processId);
                case "Lookup" -> VirtualMemoryManager.lookup(s[1], processId);
            }

//        VirtualMemoryManager.currentCommand = (VirtualMemoryManager.currentCommand + 1)%(main.commands.size());
            VirtualMemoryManager.currentCommand++;
        }

//        System.out.println(VirtualMemoryManager.currentCommand);
        semaphore.release();

//        semaphore.acquire();
//        String[] s = main.commands.get(VirtualMemoryManager.currentCommand);
//
//        if (s[0].equals("Store")){
//            VirtualMemoryManager.store(s[1], Integer.parseInt(s[2]), processId);
//        }
//        else if (s[0].equals("Release")){
//            VirtualMemoryManager.release(s[1],processId);
//        }
//        else if (s[0].equals("Lookup")){
//            VirtualMemoryManager.lookup(s[1],processId);
//        }
//
//        VirtualMemoryManager.currentCommand = (VirtualMemoryManager.currentCommand + 1)%(main.commands.size());
//        semaphore.release();
    }




    public boolean isFinished() {
        return burstTimeRemaining <= 0;
    }
}