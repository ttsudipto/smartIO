package gui;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt. Insets;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JList;
import javax.swing.ButtonGroup;

import server.NetworkManager;
import server.NetworkState;
import server.NetworkThread;


public class MainWindow extends JFrame implements ActionListener {

    private JButton mDisconnectButton;
    private JRadioButton mOnButton, mOffButton;
    private JPanel mRadioPanel;
    private JList<String> mList;
    private boolean mLastSelectedOption;

    private NetworkManager mManager;
    private NetworkThread mNetworkThread;
    private Thread mThread;
    private NetworkState mState;

    public MainWindow(NetworkManager manager) {
        this.mManager = manager;
        this.mState = manager.getNetworkState();
        mNetworkThread = new NetworkThread(manager);

        this.setTitle("SmartIO");
        this.setMinimumSize(new Dimension(640,480));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        mDisconnectButton = new JButton("Disconnect");
        mDisconnectButton.setMargin(new Insets(5,5,5,5));
        mDisconnectButton.setActionCommand("disconnect_clicked");
        mDisconnectButton.addActionListener(this);

        mOnButton = new JRadioButton();
        mOnButton.setText("Server On");
        mOnButton.setActionCommand("on");
        mOffButton = new JRadioButton();
        mOffButton.setActionCommand("off");
        mOffButton.setText("Server Off");
        mOffButton.setSelected(true);
        mLastSelectedOption = false;
        ButtonGroup bg = new ButtonGroup();
        bg.add(mOffButton);
        bg.add(mOnButton);
        mOnButton.addActionListener(this);
        mOffButton.addActionListener(this);
        mRadioPanel = new JPanel(new GridLayout(1, 0));
        mRadioPanel.add(mOnButton);
        mRadioPanel.add(mOffButton);

        mList = new JList<>(mState.getListModel());
        mList.setLayoutOrientation(JList.VERTICAL);
        mList.setFixedCellHeight(50);

        this.add(mDisconnectButton, BorderLayout.SOUTH);
        this.add(mRadioPanel, BorderLayout.NORTH);
        this.add(mList, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {

        if(event.getActionCommand().equals("disconnect_clicked")) {
            List<String> selectedValues = mList.getSelectedValuesList();
            try {
                for (int i = 0; i < selectedValues.size(); ++i)
                    mManager.disconnect(InetAddress.getByName(selectedValues.get(i)));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("disconnect clicked");
        }

        else if(event.getActionCommand().equals("on") && !mLastSelectedOption) {
            mLastSelectedOption = true;
            System.out.println("on clicked");
            mThread = new Thread(mNetworkThread);
//            System.out.println(nThread.getState().toString());
            mThread.start();
//            System.out.println(nThread.getState().toString());
        }

        else if(event.getActionCommand().equals("off") && mLastSelectedOption) {
            mLastSelectedOption = false;
            System.out.println("off clicked");
            try {
                mManager.stopServer();
                mThread = null;
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

//    public void updateList(String[] data) {
//        listModel.removeAllElements();
//        for(int i=0; i<data.length; ++i)
//            listModel.addElement(data[i]);
//    }
}