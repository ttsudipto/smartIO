package server;

import javax.swing.*;
import java.net.InetAddress;
import java.util.HashSet;


public class NetworkState {

    private static HashSet<InetAddress> addressSet = new HashSet<>();
    private DefaultListModel<String> listModel;

    public NetworkState() {
        listModel = new DefaultListModel<>();
    }

    public DefaultListModel getListModel() {
        return listModel;
    }

    public void add(InetAddress ia) {
        addressSet.add(ia);
        if(!listModel.contains(ia.getHostAddress()))
            listModel.addElement(ia.getHostAddress());
    }

    public void remove(InetAddress ia) {
        if(addressSet.contains(ia))
            addressSet.remove(ia);
        if(listModel.contains(ia.getHostAddress()))
            listModel.removeElement(ia.getHostAddress());
    }
}