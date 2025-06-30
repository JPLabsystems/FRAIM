import java.util.*;

public class main {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println(
                "\n***** FRAIM PARSER *****\nEnter path for gcode or leave empty for default (/home/justinas/FRAIM/model.gcode)\n************************");
        String path = in.nextLine();

        System.out.println(
                "\n************************\nEnter path for output directory or leave empty for default (/home/justinas/FRAIM/Clouds)\n************************");
        String out = in.nextLine();

        in.close();

        if (path.equals("")) {
            path = "/home/justinas/FRAIM/model.gcode";
        }
        if (out.equals("")) {
            out = "/home/justinas/FRAIM/Clouds";
        }

        System.out.println("gcode source path = " + path);
        System.out.println("output directory path = " + out + "\n");

        Pointcloud CLOUD = new Pointcloud("/home/justinas/FRAIM/Models/model1.gcode", "/home/justinas/FRAIM/Clouds/pointcloud1.txt");
        CLOUD.parse();
        CLOUD.printCloudToFile();
    }   
}