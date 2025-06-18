import java.util.*;
import java.io.*;

public class main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        File gcode = new File("/home/justinas/FRAIM/model.gcode");
        File pointCloud = new File("/home/justinas/FRAIM/pointcloudjava.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(gcode))) {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pointCloud))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Writing to a file

    }
}