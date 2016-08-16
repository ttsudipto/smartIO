import java.io.IOException;
import java.awt.AWTException;

import server.NetworkManager;
import gui.MainWindow;

class Main {
    public static void main(String args[]) throws IOException,AWTException,InterruptedException {
        NetworkManager manager = new NetworkManager();
        MainWindow win = new MainWindow(manager);
    }
}