/*
 * questions.
 */

/* @test
 * @bug 6639507
 * @summary sdfjhgsdjf
 * @author sdfkjh kjhsdf ksjdhf
 */

import sun.awt.SunToolkit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Random;

public class schebotarev {
    private static final int SHOULD_BE_ENTERED_OUTSIDE = 100;
    private JFrame frame;
    private JTextArea area;
    private Robot robot;

    public static void main(String[] args) throws Exception {
        final schebotarev test = new schebotarev();
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

    public schebotarev() throws AWTException {
        robot = new Robot();
    }

    private void setupUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

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

    private void test() throws IOException {
        robot.mouseMove(700, 700);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        realSync();
        enterTextLongTime();
        realSync();
        String entered = area.getText();
        System.out.println("Entered text: " + entered);
        System.out.println("Entered text length: " + entered.length());
        if (entered.length() != 0) {
            throw new RuntimeException("Some of characters were catched");
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
