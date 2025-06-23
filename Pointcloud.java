import java.util.ArrayList;

public class Pointcloud {

    private ArrayList<Double[]> pointcloud;

    private ArrayList<Double> xArr;
    private ArrayList<Double> yArr;
    private ArrayList<Double> zArr;


    public Pointcloud() {
        pointcloud = new ArrayList<>();
        xArr = new ArrayList<>();
        yArr = new ArrayList<>();
        zArr = new ArrayList<>();
    }

    public void append(Double[] point)
    {
        pointcloud.add(point);
    }
    public void appendX(Double d)
    {
        xArr.add(d);
    }
    public void appendY(Double d)
    {
        yArr.add(d);
    }
    public void appendZ(Double d)
    {
        zArr.add(d);
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

    public ArrayList<Double[]> getList()
    {
        return pointcloud;
    }

    public void decimate()
    {
        int length = pointcloud.size();

        for(int i = 0; i <length; i ++)
        {
            if(i % 10 == 0)
            {
                pointcloud.remove(i);
            }
        }
    }

}
