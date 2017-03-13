package net;

import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.DefaultListModel;

/**
 * Stores the state information about the network.
 *
 * <p>
 *     It stores the information about the client connections with the help of
 *     two data structures :
 *     <ol>
 *         <li>
 *             A <i>Connection map</i> which is a {@code HashMap} from a client
 *             {@code Socket} to a {@link ServerThread}.
 *         </li>
 *         <li>
 *             An <i>Address map</i> which is a {@code HashMap} from the
 *             {@code InetAddress} of a client to the {@code Socket}.
 *         </li>
 *     </ol>
 *     The methods to manipulate these data structures are provided in the class.
 *     The {@link #add(ClientInfo, ServerThread)} and {@link #remove(ClientInfo)}
 *     methods are invoked by {@link ServerThread} once it successfully
 *     establishes and closes a client connection respectively.
 * </p>
 * <p>
 *     It also stores a {@link DefaultListModel} which is used by {@link gui.MainWindow}
 *     to populate the list of connections in the GUI.
 * </p>
 *
 * @see HashMap
 * @see ServerThread
 * @see gui.MainWindow
 * @see DefaultListModel
 */
public class NetworkState {

    private static HashMap<Socket, ServerThread> sConnectionMap = new HashMap<>();
    private static HashMap<InetAddress, Socket> sAddressMap = new HashMap<>();
    private DefaultListModel<String> mListModel;

    /**
     * Constructor. <br/>
     * Initializes the state of the network.
     */
    NetworkState() { mListModel = new DefaultListModel<>(); }

    /**
     * Returns a {@code ListModel} which is used by the GUI to populate the
     * list of connections.
     *
     * @return a {@link DefaultListModel}.
     * @see gui.MainWindow
     */
    public DefaultListModel getListModel() { return mListModel; }

    /**
     * @return {@code HashMap<Socket, ServerThread>} - The <i>Connection Map</i>.
     * @see HashMap
     */
    HashMap<Socket, ServerThread> getConnectionMap() { return sConnectionMap; }

    /**
     * @return {@code HashMap<InetAddress, Socket>} - The <i>Address Map</i>.
     * @see HashMap
     */
    HashMap<InetAddress, Socket> getsAddressMap() { return sAddressMap; }

    /**
     * Returns the {@link ServerThread} for a client connection.
     *
     * @param ia {@link InetAddress} of the client.
     * @return {@code ServerThread} of the client connection.
     */
    ServerThread getServerThread(InetAddress ia) { return sConnectionMap.get(sAddressMap.get(ia)); }

    /**
     * Adds a client connection to this {@code NetworkState}.
     *
     * @param clientInfo the client {@code ClientInfo}.
     * @param serverThread {@code ServerThread} of the client connection.
     */
    void add(ClientInfo clientInfo, ServerThread serverThread) {
        Socket socket = clientInfo.getSocket();
        sConnectionMap.put(socket, serverThread);
        sAddressMap.put(socket.getInetAddress(), socket);
        String client = clientInfo.getClientInfo() + " (" + socket.getInetAddress().getHostAddress() + ")";
        if(!mListModel.contains(client))    mListModel.addElement(client);
    }

    /**
     * Removes a client connection from this {@code NetworkState}.
     *
     * @param clientInfo the client {@code ClientInfo}
     */
    void remove(ClientInfo clientInfo) {
        Socket socket = clientInfo.getSocket();
        if(sConnectionMap.containsKey(socket)) sConnectionMap.remove(socket);
        if(sAddressMap.containsKey(socket.getInetAddress()))   sAddressMap.remove(socket.getInetAddress());
        String client = clientInfo.getClientInfo() + " (" + socket.getInetAddress().getHostAddress() + ")";
        if(mListModel.contains(client)) mListModel.removeElement(client);
    }
}