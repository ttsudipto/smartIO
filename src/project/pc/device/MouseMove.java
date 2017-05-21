package project.pc.device;

import java.awt.MouseInfo;
import java.awt.Robot;

/**
 * Provides static methods used by {@link MouseController} .
 *
 * <p>
 *     The {@link #moveRelatively(Robot, int, int, float)} method takes
 *     into account the dimension of the screen and handles the
 *     input displacement in such a manner that the pointer
 *     movement is proportional to the screen size.
 * </p>
 *
 * @see project.pc.device.MouseController
 */

class MouseMove {

    /**
     * Method for performing mouse movement proportional to the
     * screen size. This method is invoked by
     * {@link MouseController#move(int, int, float)}.
     *
     * @param robot the {@link java.awt.Robot} object.
     * @param relativeX displacement along x-axis.
     * @param relativeY displacement along y-axis.
     * @param sensitivity mouse movement sensitivity.
     */
    static void moveRelatively(Robot robot, int relativeX, int relativeY, float sensitivity) {
        int curX = MouseInfo.getPointerInfo().getLocation().x;
        int curY = MouseInfo.getPointerInfo().getLocation().y;
        int moveX = (int) (curX + (/*ratio*/sensitivity * relativeX));
        int moveY = (int) (curY + (/*ratio*/sensitivity * relativeY));
        moveX = moveX < 0 ? 0 : moveX;
        moveY = moveY < 0 ? 0 : moveY;
        robot.mouseMove(moveX, moveY);
    }
}