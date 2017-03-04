package server;

import java.awt.AWTException;
import java.io.IOException;

/**
 * @author Sudipto Bhattacharjee
 */
public class NetworkThread implements Runnable {

    private NetworkManager manager;

    public NetworkThread(NetworkManager manager) { this.manager = manager; }

    @Override
    public void run() {
        try {
            manager.startServer();
        } catch (IOException | AWTException | InterruptedException e) { e.printStackTrace(); }
    }
}