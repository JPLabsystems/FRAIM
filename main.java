import java.util.*;
import java.io.*;

public class main {
    public static void main(String[] args) {

        ArrayList<Double[]> pointcloud = new ArrayList<>();

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

                String z = "";
                Double zCoord; 
                String x = "";
                String y = "";
                String toWrite = "";

                Double[] point = new Double[3];

                /* EACH LINE REPRESENTS ONE POINT */

                String line;
                while ((line = reader.readLine()) != null) { 

                    for (int i = 0; i < line.length() - 11; i++) {

                        if(line.substring(i, i + 4).equals("G1 Z") && i == 0)
                        {

                            /*
                             * EXTRACT VALUE OF TYPE Double
                             */

                            int j = i+4;
                            while(!line.substring(j, j+1).equals(" "))
                            {
                                j++;
                            }
                            zCoord = new Double(line.substring(i+4, j));
                            System.out.println(String.format("zCoord = %.3f", zCoord));


                            writer.write(line + "\n");

                            z = line.substring(i + 4, i+6);
                            System.out.println(z);

                            // Double Z = new Double(z);
                            // System.out.println("Double value" + Z);
                        }

                        if (line.substring(i, i + 4).equals("G1 X") && i == 0) {

                            writer.write(line + "\n");

                            // int index = i;
                            // while(line.substring(index, index + 1) != "Y" && index < line.length())
                            // {
                            //     index++;
                            // }
                            // System.out.println(line.substring(index - 2, index));

                            // x = line.substring(i + 4, i + 10);
                            // // System.out.println(x);
                        }

                        // if(line.substring(i, i+4).equals("G1 Z")){
                        // writer.write(line + "\n");
                        // }
                    }
                    // toWrite = x;
                    // writer.write(toWrite + "\n");

                }

                System.out.println("Read/Write parsing complete");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Writing to a file

    }
}