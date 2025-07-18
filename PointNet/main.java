package PointNet;
import java.util.*;
import java.io.*;

public class main {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.print("\n\t\t++++++++++++++++\n\t\t| JPLabsystems |\n\t\t++++++++++++++++\n\n  *Firearm Risk and Interdiction Model* (FRAIM)\n\ncopyright(c) 2025 Justinas Petkauskas\ngcode parsing script\n");

        System.out.println(
                "\n***** FRAIM PARSER *****\nEnter path for gcode or leave empty for default (/home/justinas/FRAIM/model.gcode)\n************************");
        String path = in.nextLine();

        System.out.println(
                "\n************************\nEnter path for output directory or leave empty for default (/home/justinas/FRAIM/Clouds)\n************************");
        String out = in.nextLine();

        System.out.println(
                "\n************************\nEnter pointcloud size (512, 1024, or 2048)\n************************");
        int numberPoints = in.nextInt();

        in.close();

        if (path.equals("")) {
            path = "/home/justinas/FRAIM/model.gcode";
        }
        if (out.equals("")) {
            out = "/home/justinas/FRAIM/Clouds";
        }
        if (numberPoints == 0)
        {
            numberPoints = 512;
        }

        System.out.println("gcode source path = " + path);
        System.out.println("output directory path = " + out);
        System.out.println("cloud size = " + numberPoints + "\n");

        /*
         * 
         *  TESTING
         * 
         */

        File gcodeDir = new File("/home/justinas/FRAIM/trainingData/gcodes/negatives");
        File[] gcodes = gcodeDir.listFiles();


        System.out.println("PATH TO gcodes[2]: " + gcodes[64].getAbsolutePath());
        
        int count = 0;
        for(File gcode : gcodes)
        {
            Pointcloud test = new Pointcloud(gcode.getAbsolutePath(), "/home/justinas/FRAIM/trainingData/clouds/negatives", numberPoints);
            if(test.parseRegex())
            {
                count++;
                System.out.printf("\n\n**********\nobject %d parsed successcully\n**********\n\n", count);
                test.printCloudToFile();
            }
            else {
                count++;
                System.out.printf("\n\n**********\nobject %d parsed unsuccesscully\n**********\n\n", count);

            }
        }
        System.out.printf("%d objects of %d total sliced successfully", count, gcodes.length);

        // Pointcloud CLOUD = new Pointcloud("/home/justinas/FRAIM/Models/model3.gcode", "/home/justinas/FRAIM/Clouds/pointcloud1.txt", numberPoints);
        // CLOUD.parseRegex();
        // // CLOUD.parse();
        // CLOUD.printCloudToFile();

    }   
}