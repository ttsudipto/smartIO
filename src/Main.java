import java.io.IOException;
import java.awt.AWTException;
import java.net.InetAddress;

import security.EKEProvider;
import net.NetworkManager;
import gui.MainWindow;
import net.ServerInfo;

/**
 * @author Sudipto Bhattacharjee
 * @author Sayantan Majumdar
 */

class Main {

    public static void main(String args[]) throws IOException,AWTException,InterruptedException {

        byte[] publicKey = new EKEProvider().getBase64EncodedPubKey();
        String systemInfo = System.getProperty("user.name") + "@" + InetAddress.getLocalHost().getCanonicalHostName();
        ServerInfo serverInfo = new ServerInfo(publicKey, systemInfo);
        NetworkManager manager = new NetworkManager(serverInfo);
        new MainWindow(manager);
    }
}