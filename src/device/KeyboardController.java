package device;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * Utility for performing keyboard input operations.
 *
 * <p>
 *    It uses a {@link java.awt.Robot} object to generate native
 *    system input events. This differs from posting events to
 *    the AWT event queue or AWT components in that the events
 *    are generated in the platform's native input queue.
 * </p>
 *
 * @see java.awt.Robot
 */
public class KeyboardController {
    private Robot mRobot;

    /**
     * Constructor. <br/>
     * Initializes this <code>KeyboardController</code> by
     * instantiating a {@link java.awt.Robot} object.
     *
     * @throws AWTException
     */
    public KeyboardController() throws AWTException {
        mRobot = new Robot();
    }

    /**
     * Method to perform the keyboard input operations.
     *
     * @param s <code>String</code> representing the sequence of
     *          characters to be typed.
     */
    public void doKeyOperation(String s) {

        if(s.length() == 0) {
            performAction('\n');
        } else if(s.equals("*b")) {
            mRobot.keyPress(KeyEvent.VK_BACK_SPACE);
            mRobot.keyRelease(KeyEvent.VK_BACK_SPACE);
        } else {
            char[] chars = s.toCharArray();
            for (char c : chars)    performAction(c);
        }
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