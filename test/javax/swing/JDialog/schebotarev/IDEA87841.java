import javax.swing.*;
import java.awt.*;

/**
 * @author Konstantin Bulenkov
 */
public class IDEA87841 extends JFrame {
    public IDEA87841() throws HeadlessException {
        super("Test with dialog popped up in 7 seconds");
        setSize(300, 300);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException ignore) {
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JDialog dialog = new JDialog(IDEA87841.this, true);
                        dialog.setTitle("Modal input");
                        dialog.getContentPane().add(new JTextArea());
                        dialog.setSize(400, 300);
                        dialog.setVisible(true);
                    }
                });
            }
        }.start();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new IDEA87841();
    }
}
