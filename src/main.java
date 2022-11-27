import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class main {
    static File diskFile = new File("src/vm.txt");

    public static void main(String[] args){
        int maximumPages = 2;
        try {
            FileWriter fw = new FileWriter(diskFile, false);
            Scanner scanner = new Scanner(new File("src/memconfig.txt"));

            while (scanner.hasNextLine()){
                maximumPages = Integer.parseInt(scanner.nextLine());
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }

        VirtualMemoryManager VMM = new VirtualMemoryManager(maximumPages);

        VMM.start();
    }
}
