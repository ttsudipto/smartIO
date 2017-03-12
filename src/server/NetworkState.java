package server;

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
 *     The {@link #add(Socket, ServerThread)} and {@link #remove(Socket)}
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
     * @param skt the client {@code Socket}.
     * @param serverThread {@code ServerThread} of the client connection.
     */
    void add(Socket skt, ServerThread serverThread) {
        sConnectionMap.put(skt, serverThread);
        sAddressMap.put(skt.getInetAddress(), skt);
        if(!mListModel.contains(skt.getInetAddress().getHostAddress()))
            mListModel.addElement(skt.getInetAddress().getHostAddress());
    }

    /**
     * Removes a client connection from this {@code NetworkState}.
     *
     * @param skt the client {@code Socket}
     */
    void remove(Socket skt) {
        if(sConnectionMap.containsKey(skt)) sConnectionMap.remove(skt);
        if(sAddressMap.containsKey(skt.getInetAddress()))   sAddressMap.remove(skt.getInetAddress());
        if(mListModel.contains(skt.getInetAddress().getHostAddress()))  mListModel.removeElement(skt.
                getInetAddress().getHostAddress());
    }
}