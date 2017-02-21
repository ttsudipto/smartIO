package device;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;

/**
 * @author Sayantan Majumdar
 */

class MouseMove {
    private static int sScreenWidth;
    private static int sScreenHeight;

    static {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        sScreenWidth = (int) screenSize.getWidth();
        sScreenHeight = (int) screenSize.getHeight();
    }

    static void moveRelatively(final Robot robot, final long duration, int relativeX, int relativeY) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + duration;
        int curX = MouseInfo.getPointerInfo().getLocation().x;
        int curY = MouseInfo.getPointerInfo().getLocation().y;
//        while (System.currentTimeMillis() < endTime) {
//            long curTime = System.currentTimeMillis();
//            float ratio = (float) (curTime - startTime) / duration;
            int moveX = (int) (curX + (/*ratio*/1.5 * relativeX));
            int moveY = (int) (curY + (/*ratio*/1.5 * relativeY));
            moveX = moveX < 0 ? 0 : moveX;
            moveY = moveY < 0 ? 0 : moveY;
            moveX = moveX >= sScreenWidth ? sScreenWidth - 1 : moveX;
            moveY = moveY >= sScreenHeight ? sScreenHeight - 1 : moveY;
            robot.mouseMove(moveX, moveY);
//            System.out.println(moveX+" "+moveY);
//        }
    }
}