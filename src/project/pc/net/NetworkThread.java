package project.pc.net;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Implementation of the {@link java.lang.Runnable} for the thread that
 * executes the network operations.
 *
 * <p>
 *     This keeps away the network operations from the <i>Swing Event
 *     Dispatch Thread</i> and prevents the GUI from becoming unresponsive.
 *     This thread gets started by {@link project.pc.gui.MainWindow} while
 *     performing the action of starting the server - the first possible
 *     network operation.
 * </p>
 *
 * @see java.lang.Runnable
 * @see project.pc.gui.MainWindow#actionPerformed(ActionEvent)
 */
public class NetworkThread implements Runnable {

    private NetworkManager manager;

    /**
     * Constructor.
     *
     * Initializes the thread.
     *
     * @param manager the {@link NetworkManager} for this network.
     * @see project.pc.net.NetworkManager
     */
    public NetworkThread(NetworkManager manager) { this.manager = manager; }

    /**
     * Operations performed by this <code>NetworkThread</code>.
     */
    @Override
    public void run() {
        try {
            manager.startServer();
        } catch (IOException | AWTException | InterruptedException e) { e.printStackTrace(); }
    }
}