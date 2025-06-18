import java.util.*;
import java.io.*;

public class main {
    public static void main(String[] args) {

        File gcode = new File("/home/justinas/FRAIM/model.gcode");
        File pointCloud = new File("/home/justinas/FRAIM/pointcloudjava.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(gcode))) {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pointCloud))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    
                    writer.write(line + "\n");
                    System.out.println(line);

                    /*
                     * 
                     */ 

                    for(int i = 0; i < line.length() - 4; i++)
                    {
                        if(line.substring(i, i+1) == "G1"){
                            writer.write(line + "\n");
                        }
                    }

                     /* 
                     * 
                     */

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