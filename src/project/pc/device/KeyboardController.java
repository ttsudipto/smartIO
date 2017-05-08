package project.pc.device;

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
     * Constructor.
     *
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
        } else {
            char[] chars = s.toCharArray();
            for (char c : chars)    performAction(c);
        }
    }

    /**
     * Method to perform keyboard input operations
     * for different function keys.
     *
     * @param s <code>String</code> representing the
     *          key to be pressed.
     */
    public void doSpecialKeyOperation(String s) {
        switch (s) {
            case "Ctrl_On":
                mRobot.keyPress(KeyEvent.VK_CONTROL);
                break;

            case "Ctrl_Off":
                mRobot.keyRelease(KeyEvent.VK_CONTROL);
                break;

            case "Alt_On":
                mRobot.keyPress(KeyEvent.VK_ALT);
                break;

            case "Alt_Off":
                mRobot.keyRelease(KeyEvent.VK_ALT);
                break;

            case "Shift_On":
                mRobot.keyPress(KeyEvent.VK_SHIFT);
                break;

            case "Shift_Off":
                mRobot.keyRelease(KeyEvent.VK_SHIFT);
                break;

            case "Home":
                performSpecialAction(KeyEvent.VK_HOME);
                break;

            case "End":
                performSpecialAction(KeyEvent.VK_END);
                break;

            case "Page_Up":
                performSpecialAction(KeyEvent.VK_PAGE_UP);
                break;

            case "Page_Down":
                performSpecialAction(KeyEvent.VK_PAGE_DOWN);
                break;

            case "Insert":
                performSpecialAction(KeyEvent.VK_INSERT);
                break;

            case "Del":
                performSpecialAction(KeyEvent.VK_DELETE);
                break;

            case "Esc":
                performSpecialAction(KeyEvent.VK_ESCAPE);
                break;

            case "Tab":
                performSpecialAction(KeyEvent.VK_TAB);
                break;

            case "F1":
                performSpecialAction(KeyEvent.VK_F1);
                break;

            case "F2":
                performSpecialAction(KeyEvent.VK_F2);
                break;

            case "F3":
                performSpecialAction(KeyEvent.VK_F3);
                break;

            case "F4":
                performSpecialAction(KeyEvent.VK_F4);
                break;

            case "F5":
                performSpecialAction(KeyEvent.VK_F5);
                break;

            case "F6":
                performSpecialAction(KeyEvent.VK_F6);
                break;

            case "F7":
                performSpecialAction(KeyEvent.VK_F7);
                break;

            case "F8":
                performSpecialAction(KeyEvent.VK_F8);
                break;

            case "F9":
                performSpecialAction(KeyEvent.VK_F9);
                break;

            case "F10":
                performSpecialAction(KeyEvent.VK_F10);
                break;

            case "F11":
                performSpecialAction(KeyEvent.VK_F11);
                break;

            case "F12":
                performSpecialAction(KeyEvent.VK_F12);
                break;

            case "Up":
                performSpecialAction(KeyEvent.VK_UP);
                break;

            case "Down":
                performSpecialAction(KeyEvent.VK_DOWN);
                break;

            case "Left":
                performSpecialAction(KeyEvent.VK_LEFT);
                break;

            case "Right":
                performSpecialAction(KeyEvent.VK_RIGHT);
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

    private void performSpecialAction(int keyCode) {
        mRobot.keyPress(keyCode);
        mRobot.keyRelease(keyCode);
    }
}