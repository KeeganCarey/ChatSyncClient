import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.security.Key;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class Input {
    Robot robot;
    public Input() throws Exception {
        robot = new Robot();
    }

    public void pasteString(String text) {
        setClipboard(text);

        robot.keyPress(Main.CHAT_KEY);
        robot.keyRelease(Main.CHAT_KEY);
        robot.delay(10);
        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);
        robot.delay(10);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_V);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(10);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

    }



    public void typeString(String text) {
        for (char c : text.toCharArray()) {
            typeChar(c);
            robot.delay(10); // optional delay between keystrokes
        }
    }

    public void typeChar(char c) {
        if (c == '\\') {
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            return;
        } else if (c == '消') {
            robot.keyPress(KeyEvent.VK_BACK_SPACE);
            robot.keyRelease(KeyEvent.VK_BACK_SPACE);
            return;
        }

        boolean shift = (Character.toUpperCase(c) == c) && Character.isAlphabetic(c);// check if i need to shift to make it uppercase

        c = Character.toUpperCase(c);
        String t = shift ? "A":"B";
        System.out.println("Char: '" + c + "', Value: " + ((int)c)+ (shift ? ", Shift: true":""));

        if (shift) {
            robot.keyPress(KeyEvent.VK_SHIFT);
        }
        robot.keyPress(c);
        robot.keyRelease(c);
        if (shift) {
            robot.keyRelease(KeyEvent.VK_SHIFT);
        }
    }

    public String getClipboard() throws Exception {
        try {
            sleep(20); // Sleeping allows clipboard time to refresh so it wont be blank(or crash) every now and then
        } catch(Exception e) {
            System.out.println("Exception: " + e);
        }

        while (true) {
            try {
                // Create a Clipboard object using getSystemClipboard() method
                Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                // Get data stored in the clipboard that is in the form of a string (text)
                return c.getData(DataFlavor.stringFlavor).toString();
            } catch (Exception e) {
                System.out.println("Error grabbing clipboard: " + e);
                System.out.println("retrying");
            }
        }
    }

    public static void setClipboard(String str) {
        try {
            sleep(20); // Sleeping allows clipboard time to refresh so it wont be blank(or crash) every now and then
        } catch(Exception e) {
            System.out.println("Exception: " + e);
        }

        while (true) {
            try {
                // Create a Clipboard object using getSystemClipboard() method
                Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                // Get data stored in the clipboard that is in the form of a string (text)
                StringSelection sel = new StringSelection(str);
                c.setContents(sel, sel);
                break;
            } catch (Exception e) {
                System.out.println("Error grabbing clipboard: " + e);
                System.out.println("retrying");
            }
        }
    }
}
