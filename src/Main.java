import gui.MainWindow;
import net.NetworkManager;
import net.ServerInfo;
import security.EKEProvider;

import java.awt.AWTException;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Class containing the <code>main()</code> method.
 *
 * <p>
 *     The {@link #main(String[])} method :
 *     <ul>
 *         <li>Generates the public key of the server.</li>
 *         <li>
 *             Instantiates the {@link net.NetworkManager} for
 *             handling the network operations.
 *         </li>
 *         <li>
 *             Displays the main window of the GUI.
 *         </li>
 *     </ul>
 * </p>
 *
 * @see #main(String[])
 * @see net.NetworkManager
 * @see net.ServerInfo
 */

class Main {

    /**
     * The <code>main</code> method.
     *
     * @param args the command-line arguments.
     * @throws IOException
     * @throws AWTException
     * @throws InterruptedException
     */
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