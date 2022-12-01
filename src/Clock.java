public class Clock extends Thread {
    int time = 1000;

    //Clock class for keeping track of time
    public void run() {
        while (true) {
            try {
                Thread.sleep(10L);
                time++;
            } catch (InterruptedException e) {
                System.out.println("Clock Sleep Exception");
            }

        }
    }

}

