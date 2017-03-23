import gui.MainWindow;
import net.NetworkManager;
import net.ServerInfo;
import security.EKEProvider;

import java.awt.AWTException;
import java.io.IOException;
import java.net.InetAddress;

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
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
        new MainWindow(manager);
    }
}