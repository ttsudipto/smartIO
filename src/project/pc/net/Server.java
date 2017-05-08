package project.pc.net;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.awt.AWTException;

import project.pc.device.KeyboardController;
import project.pc.device.MouseController;

/**
 * Server data structure.
 *
 * <p>
 *     It encapsulates the {@link java.net.ServerSocket} used for
 *     establishing TCP connection with the client.
 * </p>
 * <p>
 *     When the <code>Server</code> starts, the {@link #listen()}
 *     method is called which listens on the port in order to accept
 *     incoming client connections. Upon accepting connection it
 *     instantiates and starts a {@link ServerThread} for each
 *     connection.
 * </p>
 * <p>
 *     It also instantiates the {@link project.pc.device.MouseController}
 *     and {@link project.pc.device.KeyboardController} which is used for
 *     mouse and keyboard control respectively.
 * </p>
 *
 * @see java.net.ServerSocket
 * @see project.pc.net.ServerThread
 * @see #listen()
 * @see project.pc.device.MouseController
 * @see project.pc.device.KeyboardController
 */

class Server {

    private ServerSocket mServerSocket;
    private BroadcastThread mBroadcastThread;
    private MouseController mMouseController;
    private KeyboardController mKeyboardController;
    private NetworkState mState;

    private boolean mStopFlag;
    private static final int TCP_PORT = 1234;

    /**
     * Constructor.
     *
     * Initializes the server.
     *
     * @param state {@link NetworkState} of this network.
     * @param broadcastThread the {@link BroadcastThread}.
     * @throws IOException
     * @throws AWTException
     */
    Server(NetworkState state, BroadcastThread broadcastThread) throws IOException, AWTException {
        mState = state;
        mBroadcastThread = broadcastThread;
        mMouseController = new MouseController();
        mKeyboardController = new KeyboardController();
        mServerSocket = new ServerSocket(TCP_PORT);
        mServerSocket.setSoTimeout(200);
        mStopFlag = false;
    }

    /**
     * Sets the stop flag.
     *
     * Used to exit from the {@link #listen()} method.
     *
     * @see #listen()
     */
    void setStopFlag() { mStopFlag = true; }

    /**
     * Returns the timeout of the {@link java.net.ServerSocket} used
     * by this <code>Server</code>.
     *
     * @return the timeout in milliseconds of this <code>Server</code>.
     * @throws IOException
     */
    int getTimeout() throws IOException { return mServerSocket.getSoTimeout(); }

    /**
     * Listens continuously on the <i>TCP PORT</i>.
     *
     * <p>
     *     Once the server accepts a connection, a separate
     *     {@link ServerThread} is started for each connection. The
     *     <i>listening</i> can be stopped by invoking the
     *     {@link #setStopFlag()}  method.
     * </p>
     *
     * @throws IOException
     * @throws InterruptedException
     * @see project.pc.net.ServerThread
     * @see #setStopFlag()
     */
    void listen() throws IOException,InterruptedException {
        while(!mStopFlag) {
            try {
                Socket clientSocket = mServerSocket.accept();

                ServerThread st = new ServerThread(mState, clientSocket, mMouseController,
                        mKeyboardController, mBroadcastThread);
                Thread t = new Thread(st);
                t.start();
            } catch (SocketTimeoutException | SocketException ignored) {}
        }
    }

    /**
     * Closes the <code>ServerSocket</code> used by this
     * <code>Server</code>.
     *
     * @throws IOException
     */
    void close() throws IOException {
        mServerSocket.close();
    }
}