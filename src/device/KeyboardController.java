package device;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * @author Sayantan Majumdar
 */
public class KeyboardController {
    private Robot mRobot;
    private static int sSpecialKeyCode;
    private static boolean sSpecialKeyPressed;
    public KeyboardController() throws AWTException {
        mRobot = new Robot();
    }

    public void doKeyOperation(String key) {

        if(key.length() > 1) {
            switch (key) {
                case "backspace":
                    mRobot.keyPress(KeyEvent.VK_BACK_SPACE);
                    mRobot.keyRelease(KeyEvent.VK_BACK_SPACE);
                    break;

                case "shift":
                    mRobot.keyPress(KeyEvent.VK_SHIFT);
                    sSpecialKeyCode = KeyEvent.VK_SHIFT;
                    sSpecialKeyPressed = true;
                    break;

                case "alton":
                    mRobot.keyPress(KeyEvent.ALT_MASK);
                    sSpecialKeyCode = KeyEvent.ALT_MASK;
                    sSpecialKeyPressed = true;
                    break;

                case "control":
                    mRobot.keyPress(KeyEvent.VK_CONTROL);
                    sSpecialKeyCode = KeyEvent.VK_CONTROL;
                    sSpecialKeyPressed = true;
                    break;

                case "caps":
                    mRobot.keyPress(KeyEvent.VK_CAPS_LOCK);
                    sSpecialKeyCode = KeyEvent.VK_CAPS_LOCK;
                    sSpecialKeyPressed = true;
                    break;

                case "space":
                    mRobot.keyPress(KeyEvent.VK_SPACE);
                    mRobot.keyRelease(KeyEvent.VK_SPACE);
            }
        }
        else {
            System.out.println(key);
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
            mRobot.keyPress(KeyEvent.KEY_TYPED);
            try {
                mRobot.keyPress(keyCode);
                mRobot.keyRelease(keyCode);
            } catch (IllegalArgumentException e) {}
            if(sSpecialKeyPressed) mRobot.keyRelease(sSpecialKeyCode);
        }
    }
}
