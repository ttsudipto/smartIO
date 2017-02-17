package gui;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt. Insets;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

import server.NetworkManager;
import server.NetworkState;
import server.NetworkThread;
import server.ServerThread;

/**
 * @author Sudipto Bhattacharjee
 */

public class MainWindow extends JFrame implements ActionListener, WindowListener {

    private JList mList;
    private boolean mLastSelectedOption;

    private NetworkManager mManager;
    private NetworkState mState;
    private NetworkThread mNetworkThread;
    private Thread mThread;

    private static final long DIALOG_TIMEOUT = 2000;

    public MainWindow(NetworkManager manager) {
        this.mManager = manager;
        NetworkState mState = manager.getNetworkState();
        mNetworkThread = new NetworkThread(manager);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception e) {System.out.println("Error in loading look-and-feel ...");}

        this.setTitle("SmartIO");
        this.setMinimumSize(new Dimension(640,480));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(this);

        this.setLayout(new BorderLayout());

        JButton mDisconnectButton = new JButton("Disconnect");
        mDisconnectButton.setMargin(new Insets(5,5,5,5));
        mDisconnectButton.setActionCommand("disconnect_clicked");
        mDisconnectButton.addActionListener(this);

        JRadioButton mOnButton = new JRadioButton();
        mOnButton.setText("Server On");
        mOnButton.setActionCommand("on");
        JRadioButton mOffButton = new JRadioButton();
        mOffButton.setActionCommand("off");
        mOffButton.setText("Server Off");
        mOffButton.setSelected(true);
        mLastSelectedOption = false;
        ButtonGroup bg = new ButtonGroup();
        bg.add(mOffButton);
        bg.add(mOnButton);
        mOnButton.addActionListener(this);
        mOffButton.addActionListener(this);
        JPanel mRadioPanel = new JPanel(new GridLayout(1, 0));
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

//    public static int showConnectionRequestDialog(String data) {
//
//        String message = data + " wants to connect.\n Do you agree ?";
//        String title = "Connection confirmation";
//        Object[] options = {"Yes", "No"};
//        //final JOptionPane optionPane = new JOptionPane(message,JOptionPane.QUESTION_MESSAGE,JOptionPane.YES_NO_OPTION);
//        JOptionPane.getRootFrame().dispose();
//        int n = JOptionPane.showOptionDialog(null,
//                message,
//                title,
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                options,
//                options[1]);
////        optionPane.createInternalFrame(this, )
//        System.out.println(n);
//        return n;
//    }

    public static void showPairingKeyDialog(String title, String pairingKey, ServerThread st) {
        String message = "Type the following pairing key to connect your phone: " + pairingKey;
        JOptionPane option = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = st.getDialog();
        try {
            dialog.dispose();
        } catch (NullPointerException e) {}
        dialog = option.createDialog(title);
        dialog.setModal(false);
        st.setDialog(dialog);
        dialog.setVisible(true);
    }

    public static void showIncorrectPKeyDialog(String title, ServerThread st) {
        JOptionPane option = new JOptionPane("Incorrect Pairing Key!",JOptionPane.ERROR_MESSAGE);
        JDialog dialog = st.getDialog();
        try {
            dialog.dispose();
        } catch(NullPointerException e) {}
        dialog = option.createDialog(title);
        dialog.setModal(false);
        st.setDialog(dialog);
        dialog.setVisible(true);
        try {
            Thread.sleep(DIALOG_TIMEOUT);
            dialog.dispose();
        } catch (InterruptedException | NullPointerException e) {}
    }

    public static void showConnectionConfirmationDialog(String title, ServerThread st) {
        String message = "Connected !!";
        JOptionPane option = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = st.getDialog();
        try {
            dialog.dispose();
        } catch(NullPointerException e) {}
        dialog = option.createDialog(title);
        dialog.setModal(false);
        st.setDialog(dialog);
        dialog.setVisible(true);
        try {
            Thread.sleep(DIALOG_TIMEOUT);
            dialog.dispose();
        } catch (InterruptedException | NullPointerException e) {}
    }

    public void actionPerformed(ActionEvent event) {

        if(event.getActionCommand().equals("disconnect_clicked")) {
            List<String> selectedValues = mList.getSelectedValuesList();
            try {
                for (String values: selectedValues) {
                    mManager.disconnect(InetAddress.getByName(values));
                }
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

    @Override
    public void windowOpened(WindowEvent windowEvent) {}

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        try {
            mManager.stopServer();
            mThread = null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {}

    @Override
    public void windowIconified(WindowEvent windowEvent) {}

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {}

    @Override
    public void windowActivated(WindowEvent windowEvent) {}

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {}

//    public void updateList(String[] data) {
//        listModel.removeAllElements();
//        for(int i=0; i<data.length; ++i)
//            listModel.addElement(data[i]);
//    }
}