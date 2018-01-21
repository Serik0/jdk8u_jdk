import javax.swing.*;
import java.awt.*;

public class StringEater extends JFrame {
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new StringEater();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        JTextArea area = new JTextArea(10, 60);
        frame.add(area);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Thread.sleep(10000);
        System.err.println(area.getText());
        frame.dispose();
    }
}
