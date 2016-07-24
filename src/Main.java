import java.io.IOException;
import java.awt.AWTException;
import java.net.*;

import server.Server;

class Main
{ 
    public static void main(String args[]) throws IOException,AWTException,InterruptedException
    {
        Server s = new Server(1234);
        s.listen();

        s.close();
    }
}