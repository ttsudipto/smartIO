package net;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.awt.AWTException;

import device.KeyboardController;
import device.MouseController;

/**
 * Server data structure.
 *
 * <p>
 *     It encapsulates the {@link ServerSocket} used for establishing
 *     TCP connection with the client.
 * </p>
 * <p>
 *     When the {@code Server} starts, the {@link #listen()} method is
 *     called which listens on the port in order to accept incoming
 *     client connections. Upon accepting connection it instantiates
 *     and starts a {@link ServerThread} for each connection.
 * </p>
 * <p>
 *     It also instantiates the {@link MouseController} and
 *     {@link KeyboardController} which is used for mouse and keyboard
 *     control respectively.
 * </p>
 *
 * @see ServerSocket
 * @see ServerThread
 * @see #listen()
 * @see MouseController
 * @see KeyboardController
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
     * Constructor. <br/>
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
     * Sets the stop flag. <br/>
     * Used to exit from the {@link #listen()} method.
     *
     * @see #listen()
     */
    void setStopFlag() { mStopFlag = true; }

    /**
     * Returns the timeout of the {@link ServerSocket} used in this {@code Server}.
     *
     * @return the timeout of the {@code ServerSocket} used in this {@code Server}
     *         in milliseconds..
     * @throws IOException
     */
    int getTimeout() throws IOException { return mServerSocket.getSoTimeout(); }

    /**
     * <p>
     *     Listens continuously on the {@link #TCP_PORT}. Once the server accepts
     *     a connection, a separate {@link ServerThread} is started for each
     *     connection.
     * </p>
     *
     * @throws IOException
     * @throws InterruptedException
     * @see #TCP_PORT
     * @see ServerThread
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
     * Closes the {@code ServerSocket} used in this {@code Server}.
     *
     * @throws IOException
     */
    void close() throws IOException {
        mServerSocket.close();
    }
}