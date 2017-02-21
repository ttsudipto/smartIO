package device;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;

/**
 * @author Sayantan Majumdar
 * @author Sudipto Bhattacharjee
 */

class MouseMove {
    private static int sScreenWidth;
    private static int sScreenHeight;

    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        sScreenWidth = (int) screenSize.getWidth();
        sScreenHeight = (int) screenSize.getHeight();
    }

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
