package net;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * <p>
 *     This is the implementation of the {@link Runnable} of the thread that
 *     executes the network operations. This keeps away the network operations
 *     from the <i>Swing Event Dispatch Thread</i> and prevents the GUI to
 *     become unresponsive.
 * </p>
 * <p>
 *     This thread gets started by {@link gui.MainWindow} while performing the
 *     action of starting the server - the first possible network operation.
 * </p>
 *
 * @see Runnable
 * @see gui.MainWindow#actionPerformed(ActionEvent)
 */
public class NetworkThread implements Runnable {

    private NetworkManager manager;

    /**
     * Constructor. <br/>
     * Initializes the thread.
     *
     * @param manager the {@code NetworkManager} for this network.
     * @see NetworkManager
     */
    public NetworkThread(NetworkManager manager) { this.manager = manager; }

    /**
     * Operations performed by this {@code NetworkThread}.
     */
    @Override
    public void run() {
        try {
            manager.startServer();
        } catch (IOException | AWTException | InterruptedException e) { e.printStackTrace(); }
    }
}