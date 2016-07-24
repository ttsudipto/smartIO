package mouse;

import java.awt.Robot;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;

public class MouseController
{
    public MouseController() throws AWTException
    {
        r = new Robot();
    }
    
    public void move(int x, int y)
    {
        r.mouseMove(x, y);
    }
    public void wait(int m)
    {
        r.delay(1000);
    }
    private Robot r;
}