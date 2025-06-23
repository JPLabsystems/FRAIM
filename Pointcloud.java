import java.util.ArrayList;

public class Pointcloud {

    private ArrayList<Double[]> pointcloud;

    public Pointcloud() {
        pointcloud = new ArrayList<>();
    }

    public void append(Double[] point)
    {
        pointcloud.add(point);
    }

    public void printCloud()
    {
        // for(Double[] p : pointcloud)
        // {
        //     System.out.printf("point: %.3f, %.3f, %.3f%n", p[0], p[1], p[2]);
        // }

        for(int i = 1000; i < 1010; i++)
        {
            System.out.printf("point: %.3f, %.3f, %.3f%n", pointcloud.get(i)[0], pointcloud.get(i)[1], pointcloud.get(i)[2]);
        }
    }
}
