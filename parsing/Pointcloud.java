/*
 * JPLabsystems
 * Justinas Petkauskas
 * FRAIM 0.0.0.a
 * 
 * Generative AI Statement:
 * The use of Generative AI to generate code has been limited to line-by-line syntax queries. All logic and program architecture is original. 
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
     * Getter for the pointcloud ArrayList.
     */
    public ArrayList<Double[]> getList() {
        return pointcloud;
    }

    /**
     * Add point (Double[3]) to the ArrayList
     */
    public void append(Double[] point) {
        pointcloud.add(point);
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
            int DCS = decimate(); 
            if(DCS != numPoints)
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
    public int decimate() {
        ArrayList<Double[]> decimatedCloud = new ArrayList<>();
        
        int pointcloudSize = pointcloud.size();
        if(pointcloudSize < numPoints)
        {
            return 0;
        
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

        return pointcloud.size();
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
