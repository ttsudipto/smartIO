package gui;

import server.NetworkManager;
import server.NetworkState;
import server.NetworkThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class MainWindow extends JFrame implements ActionListener{

    private JButton disconnectButton;
    private JRadioButton onButton, offButton;
    private JPanel radioPanel;
    private JList<String> list;
    private boolean lastSelectedOption;

    private NetworkManager manager;
    private NetworkThread networkThread;
    private Thread nThread;
    private NetworkState state;

    public MainWindow(NetworkManager manager) throws IOException, InterruptedException {
        this.manager = manager;
        this.state = manager.getNetworkState();
        networkThread = new NetworkThread(manager);

        this.setTitle("SmartIO");
        this.setMinimumSize(new Dimension(640,480));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        disconnectButton = new JButton("Disconnect");
        disconnectButton.setMargin(new Insets(5,5,5,5));
        disconnectButton.setActionCommand("disconnect_clicked");
        disconnectButton.addActionListener(this);

        onButton = new JRadioButton();
        onButton.setText("Server On");
        onButton.setActionCommand("on");
        offButton = new JRadioButton();
        offButton.setActionCommand("off");
        offButton.setText("Server Off");
        offButton.setSelected(true);
        lastSelectedOption = false;
        ButtonGroup bg = new ButtonGroup();
        bg.add(offButton);
        bg.add(onButton);
        onButton.addActionListener(this);
        offButton.addActionListener(this);
        radioPanel = new JPanel(new GridLayout(1, 0));
        radioPanel.add(offButton);
        radioPanel.add(onButton);

        list = new JList<String>(state.getListModel());
        list.setLayoutOrientation(JList.VERTICAL);
        list.setFixedCellHeight(50);

        this.add(disconnectButton, BorderLayout.SOUTH);
        this.add(radioPanel, BorderLayout.NORTH);
        this.add(list, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {

        if(event.getActionCommand().equals("disconnect_clicked")) {
            List<String> selectedValues = list.getSelectedValuesList();
            try {
                for (int i = 0; i < selectedValues.size(); ++i)
                    manager.disconnect(InetAddress.getByName(selectedValues.get(i)));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("disconnect clicked");
        }

        else if(event.getActionCommand().equals("on") && lastSelectedOption == false) {
            lastSelectedOption = true;
            System.out.println("on clicked");
            nThread = new Thread(networkThread);
//            System.out.println(nThread.getState().toString());
            nThread.start();
//            System.out.println(nThread.getState().toString());
        }

        else if(event.getActionCommand().equals("off") && lastSelectedOption == true) {
            lastSelectedOption = false;
            System.out.println("off clicked");
            try {
                manager.stopServer();
                nThread = null;
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

//    public void updateList(String[] data) {
//        listModel.removeAllElements();
//        for(int i=0; i<data.length; ++i)
//            listModel.addElement(data[i]);
//    }
}