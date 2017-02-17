package device;

import sensor.SensorDataHandler;
import sensor.representation.Cartesian2D;
import sensor.representation.Quaternion;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.event.InputEvent;

/**
 * @author Sayantan Majumdar
 * @author Sudipto Bhattacharjee
 */

public class MouseController {

    private Robot mRobot;
    private static boolean sInitialQuat = true;
    private static SensorDataHandler sSensorDataHandler;
    public MouseController() throws AWTException {
        mRobot = new Robot();
    }
    
    public void move(Quaternion quaternion) {
        if(sInitialQuat) {
            sSensorDataHandler = new SensorDataHandler(quaternion,750.0f);
            sInitialQuat = false;
        }
        Cartesian2D cartesian2D = sSensorDataHandler.pointerUpdate(quaternion);
        int x = MouseInfo.getPointerInfo().getLocation().x;
        int y = MouseInfo.getPointerInfo().getLocation().y;
        x += (int) cartesian2D.getX();
        y += (int) cartesian2D.getY();
        mRobot.mouseMove(x, y);
    }

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
                mRobot.mouseWheel(-2);
                break;

            case "downscroll":
                mRobot.setAutoDelay(100);
                mRobot.mouseWheel(2);
        }
    }
    public void wait(int m) { mRobot.delay(1000); }
}