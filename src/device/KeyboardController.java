package device;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * @author Sayantan Majumdar
 */
public class KeyboardController {
    private Robot mRobot;

    public KeyboardController() throws AWTException {
        mRobot = new Robot();
    }

    public void doKeyOperation(String s) {

        if (s.equals("backspace")) {
            mRobot.keyPress(KeyEvent.VK_BACK_SPACE);
            mRobot.keyRelease(KeyEvent.VK_BACK_SPACE);
            return;
        }
        if (s.equals("enter")) {
            mRobot.keyPress(KeyEvent.VK_ENTER);
            mRobot.keyRelease(KeyEvent.VK_ENTER);
            return;
        }
        int keyLength = s.length();
        if(keyLength == 0) {
            performAction('\n');
            return;
        }
        for (int index = 0; index < keyLength; ++index) performAction(s.charAt(index));
    }
    private void performAction(char key) {

        if(KeyCode.isShiftKey(key)) {
            int keyCode = KeyCode.getShiftCode(key);
            if(keyCode == -1)   System.out.println("Invalid char! " + key);
            else doShift(keyCode);
            return;
        }
        int keyCode = KeyCode.getCodeByChar(key);
        if (keyCode == -1) System.out.println("Invalid char! " + key);
        else {
            if (Character.isUpperCase(key))   doShift(keyCode);
            else {
                mRobot.keyPress(keyCode);
                mRobot.keyRelease(keyCode);
            }
        }
    }
    private void doShift(int keyCode) {
        mRobot.keyPress(KeyEvent.VK_SHIFT);
        mRobot.keyPress(keyCode);
        mRobot.keyRelease(keyCode);
        mRobot.keyRelease(KeyEvent.VK_SHIFT);
    }
}