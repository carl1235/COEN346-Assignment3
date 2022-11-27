import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class main {
    static File diskFile = new File("src/vm.txt");

    public static void main(String[] args){
        try {
            FileWriter fw = new FileWriter(diskFile, false);
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }

        int maximumPages = 2;

        VirtualMemoryManager VMM = new VirtualMemoryManager(maximumPages);

        VMM.start();
    }
}
