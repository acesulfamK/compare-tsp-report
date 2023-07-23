import java.io.*;
import java.util.*;

public class PointsToGraph {
    
    public static class Point {
        public double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double dist(Point p) {
            return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
        }
    }

    public static void main(String[] args) {

        ArrayList<Point> vertices = new ArrayList<>();
        String filename = "./points/square.txt";
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(" ");
                vertices.add(new Point(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
                
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        
        int n = vertices.size();
        // テキストファイルに書き込み
        try (PrintWriter out = new PrintWriter("./graph/matrix.txt")) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    out.print(String.format("%.2f",vertices.get(i).dist(vertices.get(j))));
                    if (j != n - 1) {
                        out.print(" ");
                    }
                }
                out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
