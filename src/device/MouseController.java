package device;

import sensor.SensorDataHandler;
import sensor.representation.Cartesian2D;
import sensor.representation.Quaternion;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.event.InputEvent;

/**
 * Utility for performing mouse input operations.
 *
 * <p>
 *    It uses a {@link java.awt.Robot} object to generate native
 *    system input events. This differs from posting events to
 *    the AWT event queue or AWT components in that the events are
 *    generated in the platform's native input queue.
 * </p>
 * <p>
 *     The {@link #move(Quaternion, boolean, float)} and
 *     {@link #move(int, int, float)} methods are invoked for moving the
 *     mouse pointer on the basis of 3-D and 2-D inputs from the
 *     mobile device respectively. The {@link #move(Quaternion, boolean, float)}
 *     method, in turn, calls the
 *     {@link MouseMove#moveRelatively(Robot, int, int, float)} method.
 *     The mouse button inputs are performed by invoking the
 *     {@link #doOperation(String)} method.
 * </p>
 *
 * @see java.awt.Robot
 * @see sensor.representation.Quaternion
 * @see device.MouseMove#moveRelatively(Robot, int, int, float)
 */

public class MouseController {

    private Robot mRobot;
    private SensorDataHandler mSensorDataHandler;

    private static Quaternion sQuaternion = new Quaternion();

    /**
     * Constructor. <br/>
     * Initializes this <code>MouseController</code> by
     * instantiating a {@link java.awt.Robot} object.
     *
     * @throws AWTException
     */
    public MouseController() throws AWTException {
        mRobot = new Robot();
    }

    /**
     * Method to perform mouse move on the basis of 3-D input from
     * the mobile device.
     *
     * @param quaternion a {@link sensor.representation.Quaternion}
     *                   object.
     * @param isInitQuat <code>true</code>, if <code>quaternion</code> is
     *                   initial, <br/>
     *                   <code>false</code>, otherwise.
     */
    public void move(Quaternion quaternion, boolean isInitQuat, float sensitivity) {
        sQuaternion = quaternion;
        if(isInitQuat) {
            mSensorDataHandler = new SensorDataHandler(quaternion,sensitivity);
        }
        Cartesian2D cartesian2D = mSensorDataHandler.pointerUpdate(quaternion);
        int x = MouseInfo.getPointerInfo().getLocation().x;
        int y = MouseInfo.getPointerInfo().getLocation().y;
        x += (int) cartesian2D.getX();
        y += (int) cartesian2D.getY();
        mRobot.mouseMove(x, y);
    }

    /**
     * Method to perform mouse move on the basis of 2-D input from
     * the mobile device.
     *
     * @param relativeX displacement along x-axis.
     * @param relativeY displacement along y-axis.
     */
    public void move(int relativeX, int relativeY, float sensitivity) { MouseMove.moveRelatively(mRobot, relativeX, relativeY, sensitivity); }

    /**
     * Method to perform mouse button click operations.
     * <p>
     *     <i>viz.</i>
     *     <ul>
     *         <li>Left click.</li>
     *         <li>Right click.</li>
     *         <li>Middle click.</li>
     *         <li>Scrolling</li>
     *     </ul>
     * </p>
     *
     * @param operation <code>String</code> representing the type
     *                  of operation.
     */
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
                mRobot.mouseWheel(-2);
                break;

            case "downscroll":
                mRobot.mouseWheel(2);
        }
    }
    public void wait(int m) { mRobot.delay(1000); }
    public static Quaternion getQuaternion() {
        return sQuaternion;
    }
}