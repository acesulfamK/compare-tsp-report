import javax.swing.*;
import java.awt.*;

public class LineDrawingExample extends JFrame {

    public LineDrawingExample() {
        // ウィンドウの設定
        setTitle("Line Drawing Example");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 描画するパネルを追加
        add(new LinePanel());
    }

    // 線を描画するためのカスタムパネル
    class LinePanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 線の色を設定
            g.setColor(Color.RED);

            // 線を描画 (x1, y1) から (x2, y2) まで
            g.drawLine(50, 50, 350, 350);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LineDrawingExample().setVisible(true);
        });
    }
}
