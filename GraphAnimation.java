import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class GraphAnimation extends JFrame {
    private List<Point.Double> vertices = new ArrayList<>();
    private List<List<Integer>> paths = new ArrayList<>();
    private int currentPathIndex = 0;
    private int TIME_STEP = 200;
    private int TIME_START = 1000;

    public GraphAnimation(String filePath) throws IOException {
        setTitle("Graph Animation");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        readDataFromFile(filePath);

        Timer timer = new Timer(TIME_STEP, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPathIndex++;
                if (currentPathIndex >= paths.size()) {
                    ((Timer) e.getSource()).stop();
                }
                repaint();
            }
        });
        timer.start();
    }
    
    private double scaleX(double s){
        return (s-20)*30+100;
    }

    private double scaleY(double s){
        return (s-110)*30-300;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Point.Double vertex : vertices) {
            int rad = 5;
            g.fillOval((int) scaleX(vertex.x) - rad/2 , (int) scaleY(vertex.y)- rad/2, rad, rad);
        }
        if (currentPathIndex < paths.size()) {
            List<Integer> currentPath = paths.get(currentPathIndex);
            for (int i = 0; i < currentPath.size() - 1; i++) {
                Point.Double p1 = vertices.get(currentPath.get(i));
                Point.Double p2 = vertices.get(currentPath.get(i + 1));
                g.setColor(Color.RED);
                g.drawLine((int) scaleX(p1.x), (int) scaleY(p1.y),(int) scaleX(p2.x), (int) scaleY(p2.y));
                g.setColor(Color.BLACK);
            }
        }
    }

    private void readDataFromFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(new File(filePath).toPath());
        int n = Integer.parseInt(lines.get(0).split(" ")[0]);
        int l = Integer.parseInt(lines.get(0).split(" ")[1]);

        for (int i = 1; i <= n; i++) {
            String[] parts = lines.get(i).split(" ");
            vertices.add(new Point.Double(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])));
        }

        for (int i = n + 1; i <= n + l; i++) {
            String[] parts = lines.get(i).split(" ");
            List<Integer> path = new ArrayList<>();
            for (String part : parts) {
                path.add(Integer.parseInt(part));
            }
            paths.add(path);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new GraphAnimation("./scene/japanAnt.txt").setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
