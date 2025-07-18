package PointNet;
/*
 * JPLabsystems
 * Justinas Petkauskas
 * FRAIM 0.0.0.a
 */

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class Pointcloud {

    private ArrayList<Double[]> pointcloud; // this should probably be Double[2048][3]

    private double length, width, height;

    private String outputDir;
    private String sourceDir;

    private int numPoints;

    public static int numFile;

    /**
     * Constructor.
     */
    public Pointcloud(String sD, String oD, int nP) {
        pointcloud = new ArrayList<>();
        outputDir = oD;
        sourceDir = sD;
        numPoints = nP;
    }

    /**
     * parse the specified gcode into a pointcloud array
     */
    public boolean parse() {
        File gcode = new File(sourceDir);
        try (BufferedReader reader = new BufferedReader(new FileReader(gcode))) {

            /* EACH LINE REPRESENTS ONE POINT */

            boolean moveFlag = false;

            Double zCoord;
            Double xCoord;
            Double yCoord;

            Double[] point = new Double[3];

            String line;
            while ((line = reader.readLine()) != null) {

                for (int i = 0; i < line.length() - 8; i++) {

                    if (line.substring(i, i + 4).equals("G1 Z") && i == 0) {

                        int j = i + 4;
                        while (!line.substring(j, j + 1).equals(" ") && !line.substring(j, j + 1).equals("\n")) {
                            j++;
                        }
                        zCoord = new Double(line.substring(i + 4, j));

                        point[2] = zCoord;
                    }

                    if (line.substring(i, i + 4).equals("G1 X") && i == 0 && line.indexOf("Y") != -1
                            && line.indexOf("E") != -1 && line.indexOf("prime the nozzle") == -1) {

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
                        yCoord = new Double(line.substring(j + 2, k));
                        point[1] = yCoord;
                    }

                }
                if (moveFlag) {
                    Double[] p = new Double[3];
                    p[0] = point[0];
                    p[1] = point[1];
                    p[2] = point[2];
                    append(p);
                    moveFlag = false;
                }
            }
            decimate();
            scaleTransform();

            System.out.println("\nparsing complete");

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Parse gcode into pointcloud array using Regex patterm
     * @return returns True if GCode is valid and parses successfully. False if gcode is not valid/or if parse fails.
     */
    public boolean parseRegex()
    {
        File gcode = new File(sourceDir);
        try (BufferedReader reader = new BufferedReader(new FileReader(gcode))) {

            /* EACH <String line> REPRESENTS ONE POINT */

            boolean moveFlag = false;

            Double xCoord = 0.0;
            Double yCoord = 0.0;
            Double zCoord = 0.0;

            Pattern gcodeG1XY = Pattern.compile("^G1\\b[^;]*?\\bX([-\\d.]+)\\s+Y([-\\d.]+)");
            Pattern gcodeG1Z = Pattern.compile("^G1\\b[^;]*?\\bZ([-\\d.]+)");

            String line;
            while ((line = reader.readLine()) != null) {

                Matcher XY = gcodeG1XY.matcher(line);
                Matcher Z = gcodeG1Z.matcher(line);

                if(XY.find())
                {
                    xCoord = new Double(XY.group(1));
                    yCoord = new Double(XY.group(2));
                    moveFlag = true;
                }
                else if(Z.find())
                {
                    zCoord = new Double(Z.group(1));
                }

                if(moveFlag)
                {
                    moveFlag = false;
                    Double[] point = {xCoord, yCoord, zCoord};
                    append(point);
                }
            }
            if(!decimate())
            {
                return false;
            }
            scaleTransform();

            System.out.printf("\nparsing complete\n");

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add point (Double[3]) to the ArrayList
     */
    public void append(Double[] point) {
        pointcloud.add(point);
    }

    /**
     * Getter for the pointcloud ArrayList.
     */
    public ArrayList<Double[]> getList() {
        return pointcloud;
    }

    /**
     * Prints the contents of the pointcloud to the specified path in x y z\n
     * format.
     */
    public void printCloudToFile() // output cloud to given path
    {
        File outputFile = new File(outputDir + "/model" + numFile + ".txt");
        numFile++;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (int i = 0; i < pointcloud.size(); i++) {
                Double[] p = pointcloud.get(i);
                double x = p[0];
                double y = p[1];
                double z = p[2];
                writer.write("" + x + " " + y + " " + z + "\n");
            }
            System.out.println("Done printing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reduces the number of points to exactly <numPoints> for passing to scaler.
     */
    public boolean decimate() {
        ArrayList<Double[]> decimatedCloud = new ArrayList<>();
        
        int pointcloudSize = pointcloud.size();
        if(pointcloudSize < numPoints)
        {
            return false;
        
        }
        int factor = (pointcloudSize + (numPoints - 1)) / numPoints;
        int dif = numPoints - (pointcloudSize / factor);

        for (int i = 1; i < dif; i++) {
            decimatedCloud.add(pointcloud.get(i));
        }
        for (int i = dif; i < pointcloudSize; i += factor) {
            decimatedCloud.add(pointcloud.get(i));
        }

        System.out.println("cloud size: " + pointcloud.size());
        System.out.println("decimated cloud size:" + decimatedCloud.size());
        
        pointcloud = decimatedCloud;

        return true;
    }

    /**
     * Scales and transforms model to fit inscribed into unit spehere.
     */
    public void scaleTransform() {

        double[] maxes = new double[3];
        double[] mins = new double[3];

        /* GEOMETRIC ANALYSIS */

        for (int i = 0; i < 3; i++) {
            Double[] p = pointcloud.get(0);
            Double max = p[i];
            Double min = p[i];
            for (int j = 1; j < pointcloud.size(); j++) {
                p = pointcloud.get(j);
                if (p[i] > max) {
                    max = p[i];
                }
                if (p[i] < min) {
                    min = p[i];
                }
            }
            mins[i] = min;
            maxes[i] = max;
        }

        length = maxes[1] - mins[1];
        width = maxes[0] - mins[0];
        height = maxes[2] - mins[2];

        /* TRANSFORMATION */

        double hypotenuse = Math.sqrt(length * length + width * width + height * height);
        Double[] centroid = {(mins[0] + (maxes[0] - mins[0]) / 2), (mins[1] + (maxes[1] - mins[1]) / 2), (mins[2] + (maxes[2] - mins[2]) / 2)};
        ArrayList<Double[]> scaled = new ArrayList<>();

        for(int i = 0; i < pointcloud.size(); i++)
        {
            Double[] p = pointcloud.get(i);
            Double[] sp = new Double[3];
            for(int j = 0; j < 3; j++)
            {
                sp[j] = (p[j] - centroid[j]) / (hypotenuse / 2);
            }
            scaled.add(sp);
        }
        pointcloud = scaled;

        System.out.printf("\nX min: %.2f, X max: %.2f%nY min: %.2f, Y max: %.2f%nZ min: %.2f, Z max: %.2f%n%nlength: %.2f, width: %.2f, height: %.2f%n%nHypotenuse = %.2f%n%nCENTROID: x = %.2f, y = %.2f, z = %.2f%n", 
                  mins[0], maxes[0], mins[1], maxes[1], mins[2], maxes[2], 
                  length, width, height, hypotenuse, centroid[0], centroid[1], centroid[2]);

    }

}
