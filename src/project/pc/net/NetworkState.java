package project.pc.net;

import javax.swing.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

/**
 * Stores the state information about the network.
 *
 * <p>
 *     It stores the information about the client connections with the help of
 *     two data structures :
 *     <ol>
 *         <li>
 *             A <i>Connection map</i> which is a <code>HashMap</code> from a
 *             client <code>Socket</code> to the {@link ServerThread} handling
 *             that client.
 *         </li>
 *         <li>
 *             An <i>Address map</i> which is a <code>HashMap</code> from the
 *             <code>InetAddress</code> of a client to the <code>Socket</code>.
 *         </li>
 *     </ol>
 *     The methods to manipulate these data structures are provided in the class.
 *     The {@link #add(ClientInfo, ServerThread)} and {@link #remove(ClientInfo)}
 *     methods are invoked by {@link ServerThread} once it successfully
 *     establishes and closes a client connection respectively.
 * </p>
 * <p>
 *     It also stores a {@link javax.swing.DefaultListModel} which is used by
 *     {@link project.pc.gui.MainWindow} to populate a list displaying the client connections
 *     in the GUI.
 * </p>
 *
 * @see java.util.HashMap
 * @see project.pc.net.ServerThread
 * @see project.pc.gui.MainWindow
 * @see javax.swing.DefaultListModel
 */
public class NetworkState {

    private static HashMap<Socket, ServerThread> sConnectionMap = new HashMap<>();
    private static HashMap<InetAddress, Socket> sAddressMap = new HashMap<>();
    private DefaultListModel<String> mListModel;

    /**
     * Constructor.
     *
     * Initializes the state of the network.
     */
    NetworkState() { mListModel = new DefaultListModel<>(); }

    /**
     * Returns the {@link javax.swing.ListModel}.
     *
     * It is used by the GUI to populate a list displaying the client
     * connections.
     *
     * @return a {@link javax.swing.DefaultListModel}.
     * @see project.pc.gui.MainWindow
     */
    public DefaultListModel getListModel() { return mListModel; }

    /**
     * Returns the <i>Connection Map</i>.
     *
     * @return <code>HashMap<Socket, ServerThread></code>.
     * @see java.util.HashMap
     */
    HashMap<Socket, ServerThread> getConnectionMap() { return sConnectionMap; }

    /**
     * Returns the <i>Address Map</i>.
     *
     * @return <code>HashMap<InetAddress, Socket></code>.
     * @see java.util.HashMap
     */
    HashMap<InetAddress, Socket> getsAddressMap() { return sAddressMap; }

    /**
     * Returns the {@link ServerThread} for a client connection.
     *
     * @param ia {@link java.net.InetAddress} of the client.
     * @return <code>ServerThread</code> of the client connection.
     */
    ServerThread getServerThread(InetAddress ia) { return sConnectionMap.get(sAddressMap.get(ia)); }

    /**
     * Checks if any client is connected.
     *
     * @return <code>true</code>, if no client is connected, <br/>
     *         <code>false</code>, otherwise.
     */
    public boolean isNoClientConnected() { return mListModel.isEmpty(); }


    /**
     * Adds a client connection to this <code>NetworkState</code>.
     *
     * @param clientInfo the {@link ClientInfo}.
     * @param serverThread <code>ServerThread</code> of the client connection.
     */
    void add(ClientInfo clientInfo, ServerThread serverThread) {
        Socket socket = clientInfo.getSocket();
        sConnectionMap.put(socket, serverThread);
        sAddressMap.put(socket.getInetAddress(), socket);
        String client = clientInfo.getClientInfo() + " (" + socket.getInetAddress().getHostAddress() + ")";
        if(!mListModel.contains(client))    mListModel.addElement(client);
    }

    /**
     * Removes a client connection from this <code>NetworkState</code>.
     *
     * @param clientInfo the {@link ClientInfo}.
     */
    void remove(ClientInfo clientInfo) {
        Socket socket = clientInfo.getSocket();
        if(sConnectionMap.containsKey(socket)) sConnectionMap.remove(socket);
        if(sAddressMap.containsKey(socket.getInetAddress()))   sAddressMap.remove(socket.getInetAddress());
        String client = clientInfo.getClientInfo() + " (" + socket.getInetAddress().getHostAddress() + ")";
        if(mListModel.contains(client)) mListModel.removeElement(client);
    }
}