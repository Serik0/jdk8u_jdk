/*
 * questions.
 */

/* @test
 * @bug 237465273
 * @summary sdfjhgsdjf
 * @author sdfkjh kjhsdf ksjdhf
 */

import sun.awt.SunToolkit;
import sun.jvm.hotspot.utilities.Assert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Random;

public class schebotarev {
    private static final int SHOULD_BE_ENTERED = 100;
    private JFrame frame;
    private JTextArea area;
    private Robot robot;
//    private Process withPopup;
//    private Process withArea;
    //private IDEA87841 idea87841;

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
        //idea87841 = new IDEA87841();
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        area = new JTextArea(10, 60);
        frame.add(area);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

//        try {
//            withPopup = JavaProcess.exec(IDEA87841.class);
//            withArea = JavaProcess.exec(StringEater.class);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private void test() throws IOException {
        hitKey(KeyEvent.VK_TAB);
        realSync();
        enterTextLongTime();
        realSync();
//        withPopup.destroy();
        //idea87841.dispose();
        String entered = area.getText();
        //BufferedReader in = new BufferedReader(new InputStreamReader(withArea.getErrorStream()));
        System.out.println("Entered text: " + entered);
        System.out.println("Entered text length: " + entered.length());
        Assert.that(entered.length() == SHOULD_BE_ENTERED, "Some of characters were stolen");
    }

    private void enterTextLongTime() {
        Random rnd = new Random();
        for (int i = 0; i < SHOULD_BE_ENTERED; i++) {
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

    static class JavaProcess {
        private JavaProcess() {}

        public static Process exec(Class klass) throws IOException,
                InterruptedException {
            String javaHome = System.getProperty("java.home");
            String javaBin = javaHome +
                    File.separator + "bin" +
                    File.separator + "java";
            String classpath = System.getProperty("java.class.path");
            String className = klass.getCanonicalName();

            ProcessBuilder builder = new ProcessBuilder(
                    javaBin, "-cp", classpath, className);

            return builder.start();
        }

    }

}
