import java.io.IOException;
import java.awt.AWTException;

import security.EKEProvider;
import server.NetworkManager;
import gui.MainWindow;

/**
 * @author Sudipto Bhattacharjee
 * @author Sayantan Majumdar
 */

class Main {

    public static void main(String args[]) throws IOException,AWTException,InterruptedException {

        byte[] publicKey = new EKEProvider().getBase64EncodedPubKey();

        NetworkManager manager = new NetworkManager(publicKey);
        new MainWindow(manager);
    }
}