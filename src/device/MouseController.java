package device;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;

/**
 * @author Sayantan Majumdar
 * @author Sudipto Bhattacharjee
 */

public class MouseController {

    private Robot mRobot;

    public MouseController() throws AWTException {
        mRobot = new Robot();
    }
    
    public void move(int x, int y) { mRobot.mouseMove(x, y); }

    public void doOperation(String operation) {
        switch (operation) {
            case "left":
                mRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                mRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                break;

            case "right":
                mRobot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                mRobot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                break;

            case "middle":
                mRobot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                mRobot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                break;

            case "upscroll":
                mRobot.setAutoDelay(100);
                mRobot.mouseWheel(-40);
                break;

            case "downscroll":
                mRobot.setAutoDelay(100);
                mRobot.mouseWheel(40);
        }
    }
    public void wait(int m) { mRobot.delay(1000); }
}