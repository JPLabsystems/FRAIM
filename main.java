import java.util.*;
import java.io.*;

public class main {
    public static void main(String[] args) {

        Pointcloud CLOUD = new Pointcloud();

        Scanner in = new Scanner(System.in);

        System.out.println(
            "\n***** FRAIM PARSER *****\nEnter path for gcode or leave empty for default (/home/justinas/FRAIM/model.gcode)\n************************");
        String path = in.nextLine();

        System.out.println(
            "\n************************\nEnter path for output or leave empty for default (/home/justinas/FRAIM/pointcloudjava.txt)\n************************");
        String out = in.nextLine();

        in.close();

        if (path.equals("")) {
            path = "/home/justinas/FRAIM/model.gcode";
        }
        if (out.equals("")) {
            out = "/home/justinas/FRAIM/pointcloudjava.txt";
        }

        System.out.println("gcode source path = " + path);
        System.out.println("output source path = " + out + "\n");


        File gcode = new File(path);
        File pointCloud = new File(out);

        try (BufferedReader reader = new BufferedReader(new FileReader(gcode))) {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(pointCloud))) {

                boolean moveFlag = false;

                Double zCoord;
                Double xCoord;
                Double yCoord;

                Double[] point = new Double[3];

                /* EACH LINE REPRESENTS ONE POINT */

                String line;
                while ((line = reader.readLine()) != null) {

                    for (int i = 0; i < line.length() - 11; i++) {

                        if (line.substring(i, i + 4).equals("G1 Z") && i == 0) {

                            int j = i + 4;
                            while (!line.substring(j, j + 1).equals(" ")) {
                                j++;
                            }
                            zCoord = new Double(line.substring(i + 4, j));

                            point[2] = zCoord;
                        }

                        if (line.substring(i, i + 4).equals("G1 X") && i == 0 && line.indexOf("Y") != -1 && line.indexOf("E") != -1) {

                            moveFlag = true; // plotting points only on lines where movement occurs

                            int j = i + 4;
                            while (!line.substring(j, j + 1).equals(" ")) {
                                j++;
                            }
                            xCoord = new Double(line.substring(i + 4, j));
                            point[0] = xCoord;

                            int k = j + 1;
                            while (!line.substring(k, k + 1).equals(" ") && (k <= line.length() - 2)) {
                                k++;
                            }
                            yCoord = new Double(line.substring(j+2, k));
                            point[1] = yCoord;

                        }

                    }
                    if (moveFlag) {
                        CLOUD.append(point);
                        // System.out.printf("point: %.3f, %.3f, %.3f%n", point[0], point[1], point[2]);
                        writer.write(String.format("%.3f %.3f %.3f%n", point[0], point[1], point[2]));
                        // CLOUD.appendX(point[0]);
                        // CLOUD.appendY(point[1]);
                        // CLOUD.appendZ(point[2]);
                        moveFlag = false;
                    }

                }
                // CLOUD.printCloud();
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