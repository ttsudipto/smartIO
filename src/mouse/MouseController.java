package mouse;

import java.awt.Robot;
import java.awt.AWTException;

/**
 * @author Sudipto Bhattacharjee
 */

public class MouseController {

    private Robot mRobot;

    public MouseController() throws AWTException {
        mRobot = new Robot();
    }
    
    public void move(int x, int y) { mRobot.mouseMove(x, y); }
    public void wait(int m) { mRobot.delay(1000); }
}