package net;

import java.net.Socket;

/**
 * Encapsulation of the data received from the client at the time of
 * establishing TCP connection.
 *
 * <p>
 *     The data contains :
 *     <ul>
 *         <li>Public key of the client</li>
 *         <li>Canonical hostname of the client</li>
 *         <li>Client {@link Socket}</li>
 *     </ul>
 * </p>
 *
 * @see Socket
 * @see ServerThread
 */

public class ClientInfo {
    private String mClientInfo;
    private byte[] mPublicKey;
    private Socket mClientSocket;

    /**
     * Constructor.
     *
     * <p>
     *     Initializes the {@code ClientInfo} object. The client {@link Socket}
     *     is not initialized. It can be set by the {@link #setSocket(Socket)}
     *     method.
     * </p>
     *
     * @param clientInfo canonical hostname of the client.
     * @param publicKey public key of the client.
     * @see Socket
     * @see #setSocket(Socket)
     */
    public ClientInfo(String clientInfo, byte[] publicKey) {
        mClientInfo = clientInfo;
        mPublicKey = publicKey;
    }

    /**
     * Adds client {@link Socket}
     *
     * @param socket the client {@link Socket} to be stored.
     */
    public void setSocket(Socket socket) { mClientSocket = socket; }

    /**
     * Returns the client socket.
     *
     * @return the client {@link Socket}.
     */
    public Socket getSocket() { return mClientSocket; }

    /**
     * Returns the canonical hostname of the client.
     *
     * @return canonical hostname of the client
     */
    public String getClientInfo() { return mClientInfo; }

    /**
     * Returns the public key of the client.
     *
     * @return public key of the client.
     */
    public byte[] getPublicKey() { return mPublicKey; }
}
