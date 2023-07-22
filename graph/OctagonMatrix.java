import java.io.*;
import java.util.*;

public class OctagonMatrix {
    
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
        int n = 8;
        Point[] vertices = new Point[n];

        // 正8角形の頂点を計算
        for (int i = 0; i < n; i++) {
            double t = 2 * Math.PI * i / n;
            vertices[i] = new Point(Math.cos(t), Math.sin(t));
        }

        // テキストファイルに書き込み
        try (PrintWriter out = new PrintWriter("matrix.txt")) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    out.print(vertices[i].dist(vertices[j]));
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
