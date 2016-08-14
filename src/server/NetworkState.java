package server;

import javax.swing.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class NetworkState {

    private static HashMap<Socket, ServerThread> connectionMap = new HashMap<>();
    private static HashMap<InetAddress, Socket> addressMap = new HashMap<>();
    private DefaultListModel<String> listModel;

    public NetworkState() {
        listModel = new DefaultListModel<>();
    }

    public DefaultListModel getListModel() {
        return listModel;
    }

    public HashMap<Socket, ServerThread> getConnectionMap() { return connectionMap; }

    public ServerThread getServerThread(InetAddress ia) { return connectionMap.get(addressMap.get(ia)); }

    public void add(Socket skt, ServerThread serverThread) {
        connectionMap.put(skt, serverThread);
        addressMap.put(skt.getInetAddress(), skt);
        if(!listModel.contains(skt.getInetAddress().getHostAddress()))
            listModel.addElement(skt.getInetAddress().getHostAddress());
    }

    public void remove(Socket skt) {
        if(connectionMap.containsKey(skt))
            connectionMap.remove(skt);
        if(addressMap.containsKey(skt.getInetAddress()))
            connectionMap.remove(skt.getInetAddress());
        if(listModel.contains(skt.getInetAddress().getHostAddress()))
            listModel.removeElement(skt.getInetAddress().getHostAddress());
    }
}