package server;

import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.DefaultListModel;

public class NetworkState {

    private static HashMap<Socket, ServerThread> sConnectionMap = new HashMap<>();
    private static HashMap<InetAddress, Socket> sAddressMap = new HashMap<>();
    private DefaultListModel<String> mListModel;

    NetworkState() { mListModel = new DefaultListModel<>(); }

    public DefaultListModel getListModel() { return mListModel; }

    HashMap<Socket, ServerThread> getConnectionMap() { return sConnectionMap; }

    HashMap<InetAddress, Socket> getsAddressMap() { return sAddressMap; }

    ServerThread getServerThread(InetAddress ia) { return sConnectionMap.get(sAddressMap.get(ia)); }

    void add(Socket skt, ServerThread serverThread) {
        sConnectionMap.put(skt, serverThread);
        sAddressMap.put(skt.getInetAddress(), skt);
        if(!mListModel.contains(skt.getInetAddress().getHostAddress()))
            mListModel.addElement(skt.getInetAddress().getHostAddress());
    }

    void remove(Socket skt) {
        if(sConnectionMap.containsKey(skt)) sConnectionMap.remove(skt);
        if(sAddressMap.containsKey(skt.getInetAddress()))   sConnectionMap.remove(skt.getInetAddress());
        if(mListModel.contains(skt.getInetAddress().getHostAddress()))  mListModel.removeElement(skt.
                getInetAddress().getHostAddress());
    }
}