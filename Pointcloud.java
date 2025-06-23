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

    public void printPoint(int index)
    {
    }
}
