/* @test
 * @summary Test which fails in Ubuntu environment due to to JRE-186
 * @author Sergei Chebotarev
 */

import sun.awt.SunToolkit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Jre186testTask {
    private static final int SHOULD_BE_ENTERED_OUTSIDE = 50;
    private JFrame frame;
    private JTextArea area;
    private Robot robot;
    private boolean popupEnabled;

    public static void main(String[] args) throws Exception {
        final Jre186testTask test = new Jre186testTask();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    test.setupUI();
                }
            });
            test.test();
        } finally {
            if (test.frame != null) {
                test.frame.dispose();
            }
        }
    }

    private Jre186testTask() throws AWTException {
        robot = new Robot();
        popupEnabled = false;
    }

    /**
     * Setup Swing frame which pops-up modal dialog after it is allowed by the test.
     */
    private void setupUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        new Thread() {
            @Override
            public void run() {
                synchronized (Jre186testTask.this) {
                    while (!Jre186testTask.this.popupEnabled) {
                        try {
                            Jre186testTask.this.wait();
                        } catch (InterruptedException e) {
                            //pass
                        }
                    }
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JDialog dialog = new JDialog(frame, true);
                        dialog.setTitle("Modal input");
                        area = new JTextArea();
                        dialog.getContentPane().add(area);
                        dialog.setSize(400, 300);
                        dialog.setVisible(true);
                    }
                });
            }
        }.start();
        frame.setVisible(true);
    }

    /**
     * Test algorithm:<br>
     * <ul>
     * <li>[STEPS]
     * <ul>
     * <li>Click outside of frame, type text about 5 seconds</li>
     * <li>Allow popup dialog</li>
     * <li>Type text again about 5 seconds</li>
     * </ul>
     * </li>
     * <li>[ASSERTS]
     * <ul><li>Check that no text was put into popped-up dialog.</li></ul>
     * </li>
     * </ul>
     */
    private void test() {
        robot.mouseMove(700, 700);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        realSync();
        enterTextLongTime();
        realSync();
        synchronized (this) {
            popupEnabled = true;
            this.notifyAll();
        }
        enterTextLongTime();
        realSync();
        if (area.getText().length() != 0) {
            throw new RuntimeException("Some of entered characters were put into dialog");
        }
    }

    private void enterTextLongTime() {
        Random rnd = new Random();
        for (int i = 0; i < SHOULD_BE_ENTERED_OUTSIDE; i++) {
            realSync();
            int offset = rnd.nextInt(20);
            hitKey(KeyEvent.VK_A + offset);
        }
    }

    private static void realSync() {
        ((SunToolkit) (Toolkit.getDefaultToolkit())).realSync();
    }

    private void hitKey(int keycode) {
        robot.keyPress(keycode);
        robot.keyRelease(keycode);
        delay();
    }

    private void delay() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
