package device;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;

/**
 * Provide static methods used by {@link device.MouseController} .
 *
 * <p>
 *     The {@link #moveRelatively(Robot, int, int)} method takes
 *     into account the dimension of the screen and handles the
 *     input displacement in such a manner that the pointer
 *     movement is proportional to the screen size.
 * </p>
 *
 * @see device.MouseController
 */

class MouseMove {
    private static int sScreenWidth;
    private static int sScreenHeight;

    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        sScreenWidth = (int) screenSize.getWidth();
        sScreenHeight = (int) screenSize.getHeight();
    }

    /**
     * Method for performing mouse movement proportional to the
     * screen size. This method is invoked by
     * {@link device.MouseController#move(int, int)} .
     *
     * @param robot the {@link java.awt.Robot} object.
     * @param relativeX displacement along x-axis.
     * @param relativeY displacement along y-axis.
     */
    static void moveRelatively(Robot robot, int relativeX, int relativeY) {
        int curX = MouseInfo.getPointerInfo().getLocation().x;
        int curY = MouseInfo.getPointerInfo().getLocation().y;
        int moveX = (int) (curX + (/*ratio*/1.5 * relativeX));
        int moveY = (int) (curY + (/*ratio*/1.5 * relativeY));
        moveX = moveX < 0 ? 0 : moveX;
        moveY = moveY < 0 ? 0 : moveY;
        moveX = moveX >= sScreenWidth ? sScreenWidth - 1 : moveX;
        moveY = moveY >= sScreenHeight ? sScreenHeight - 1 : moveY;
        robot.mouseMove(moveX, moveY);
    }
}