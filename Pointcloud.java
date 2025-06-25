import java.util.ArrayList;
import java.util.*;
import java.io.*;

public class Pointcloud {

    private ArrayList<Double[]> pointcloud;

    /**
     * Constructor.
     */
    public Pointcloud() {
        pointcloud = new ArrayList<>();
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
    public void printCloudToFile(String path) // output cloud to given path
    {

    }

    /**
     * Reduces the number of points to exactly 2048 for passing to scaler.
     */
    public void decimate() {
        int length = pointcloud.size();

        for (int i = 0; i < length; i++) {
            if (i % 10 == 0) {
                pointcloud.remove(i);
            }
        }
    }

    /**
     * Scales model to fit inscribed into unit spehere.
     */
    public void scale() {

    }

    public static void parse(String gcode, String out) {

    }

}
