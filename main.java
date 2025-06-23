import java.util.*;
import java.io.*;

public class main {
    public static void main(String[] args) {

        Pointcloud CLOUD = new Pointcloud();

        Scanner in = new Scanner(System.in);

        System.out.println("\n***** FRAIM PARSER *****\nEnter path for gcode or leave empty for default (/home/justinas/FRAIM/modle.gcode)\n************************");
        String path = in.nextLine();

        in.close();

        if(path.equals(""))
        {
            path = "/home/justinas/FRAIM/model.gcode";
        }

        System.out.println("gcode source path = " + path + "\n");

        File gcode = new File("/home/justinas/FRAIM/model.gcode");
        File pointCloud = new File("/home/justinas/FRAIM/pointcloudjava.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(gcode))) {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pointCloud))) {

                Double zCoord; 
                Double xCoord;
                Double yCoord;

                String toWrite = "";

                Double[] point = new Double[3];

                /* EACH LINE REPRESENTS ONE POINT */

                String line;
                while ((line = reader.readLine()) != null) { 

                    for (int i = 0; i < line.length() - 11; i++) {

                        if(line.substring(i, i + 4).equals("G1 Z") && i == 0)
                        {
                            writer.write(line + "\n");


                            /*
                             * EXTRACT VALUE OF TYPE Double
                             */

                            int j = i+4;
                            while(!line.substring(j, j+1).equals(" "))
                            {
                                j++;
                            }
                            zCoord = new Double(line.substring(i+4, j));
                            // System.out.println(String.format("zCoord = %.3f", zCoord));

                            point[2] = zCoord;
                        }

                        if (line.substring(i, i + 4).equals("G1 X") && i == 0) {

                            writer.write(line + "\n");
                            int j = i+4;
                            while(!line.substring(j, j+1).equals(" "))
                            {
                                j++;
                            }
                            xCoord = new Double(line.substring(i+4, j));
                            // System.out.println(String.format("xCoord = %.3f", xCoord));

                            point[0] = xCoord;

                        }


                    }
                    
                    CLOUD.append(point);
                    System.out.println("point: " + point[0] + ", " + point[1] + ", " +  point[2]);

                }

                System.out.println("\nRead/Write parsing complete");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Writing to a file

    }
}