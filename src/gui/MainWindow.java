package gui;

import net.NetworkManager;
import net.NetworkState;
import net.NetworkThread;
import net.ServerThread;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * Defines the main window of the application.
 *
 * <p>
 *     The contents of the main window are :
 *     <ul>
 *         <li>Couple of radio buttons for server start and stop.</li>
 *         <li>A list displaying the clients currently connected.</li>
 *         <li>
 *             A disconnect button for disconnecting the selected client.
 *         </li>
 *     </ul>
 *     It also implements <code>ActionListener</code> for these elements
 *     and <code>WindowListener</code> for graceful termination of the
 *     application upon closing of this <code>MainWindow</code>. The
 *     window is initialized and rendered at the time of instantiation
 *     by the constructor {@link #MainWindow(NetworkManager)}.
 * </p>
 *
 * @see java.awt.event.ActionListener
 * @see java.awt.event.WindowListener
 * @see #MainWindow(NetworkManager)
 */
public class MainWindow extends JFrame implements ActionListener, WindowListener {

    private JList mList;
    private boolean mLastSelectedOption;

    private NetworkManager mManager;
    private NetworkThread mNetworkThread;
    private Thread mThread;

    private static final long DIALOG_TIMEOUT = 2000;

    /**
     * Constructor. <br/>
     * Initializes the <code>MainWindow</code> and displays it.
     *
     * @param manager the {@link net.NetworkManager} for this network.
     */
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

    /**
     * Shows a dialog window displaying the pairing key. It gets closed
     * when the pairing key is sent by the client for validation.
     *
     * @param title title of the dialog window - canonical hostname of
     *              the client for which the pairing key is meant.
     * @param pairingKey the pairing key.
     * @param st the {@link net.ServerThread} which handles this client.
     * @see net.ServerThread
     */
    public static void showPairingKeyDialog(String title, String pairingKey, ServerThread st) {
        String message = "<html>Type the following pairing key to connect your phone: <b><font face=\"Monospaced\"" +
                " size=\"5\" " + "color=\"red\" >" + pairingKey + "</font></b></html>";
        JOptionPane option = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = st.getDialog();
        try {
            dialog.dispose();
        } catch (NullPointerException ignored) {}
        dialog = option.createDialog(title);
        dialog.setModal(false);
        st.setDialog(dialog);
        dialog.setVisible(true);
    }

    /**
     * Shows a dialog window displaying the error that an incorrect pairing
     * key is sent by the client.
     *
     * @param title title of the window - the canonical hostname of the
     *              client.
     * @param st the {@link net.ServerThread} which handles this client.
     * @see net.ServerThread
     */
    public static void showIncorrectPKeyDialog(String title, ServerThread st) {
        JOptionPane option = new JOptionPane("Incorrect Pairing Key!",JOptionPane.ERROR_MESSAGE);
        JDialog dialog = st.getDialog();
        try {
            dialog.dispose();
        } catch(NullPointerException ignored) {}
        dialog = option.createDialog(title);
        dialog.setModal(false);
        st.setDialog(dialog);
        dialog.setVisible(true);
        try {
            Thread.sleep(DIALOG_TIMEOUT);
            dialog.dispose();
        } catch (InterruptedException | NullPointerException ignored) {}
    }

    /**
     * Shows a dialog window displaying the information that a client is
     * successfully connected.
     *
     * @param title title of the window - the canonical hostname of the
     *              client.
     * @param st the {@link net.ServerThread} which handles this client.
     * @see net.ServerThread
     */
    public static void showConnectionConfirmationDialog(String title, ServerThread st) {
        String message = "Connected !!";
        JOptionPane option = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = st.getDialog();
        try {
            dialog.dispose();
        } catch(NullPointerException ignored) {}
        dialog = option.createDialog(title);
        dialog.setModal(false);
        st.setDialog(dialog);
        dialog.setVisible(true);
        try {
            Thread.sleep(DIALOG_TIMEOUT);
            dialog.dispose();
        } catch (InterruptedException | NullPointerException ignored) {}
    }

    /**
     * Invoked when an action occurs.
     *
     * @param event a {@link java.awt.event.ActionEvent} object.
     * @see java.awt.event.ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getActionCommand().equals("disconnect_clicked")) {
            List<String> selectedValues = mList.getSelectedValuesList();
            try {
                for (String values: selectedValues) {
                    values = values.substring(values.indexOf("(") + 1, values.length() - 1);
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
            mThread.start();
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

    /**
     * Gracefully terminates the application.
     * <p>
     *     Termination is done by stopping the broadcast disconnecting
     *     every client and finally stopping the server. It is called
     *     during the closing of this <code>MainWindow</code>.
     * </p>
     *
     * @param windowEvent a {@link java.awt.event.WindowEvent} object.
     * @see java.awt.event.WindowEvent
     */
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
}