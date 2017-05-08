package project.pc.net;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.google.gson.Gson;
import project.pc.device.KeyboardController;
import project.pc.device.MouseController;
import project.pc.gui.MainWindow;
import project.pc.security.EKEProvider;

import javax.swing.JDialog;

/**
 * Implementation of the {@link java.lang.Runnable} for the thread that is
 * responsible for communication with each client.
 *
 * <p>
 *     The actions performed by this thread are :
 *     <ul>
 *         <li>Authenticate the client.</li>
 *         <li>According to the result
 *             <ul>
 *                 <li>
 *                     If the client is not authentic, close the connection
 *                     and terminate.
 *                 </li>
 *                 <li>
 *                     Otherwise, confirm the connection and start
 *                     communication with the client.
 *                 </li>
 *             </ul>
 *         </li>
 *         <li>
 *             Examine the data received from client and transfer it to
 *             a <i>controller</i> ({@link project.pc.device.MouseController}
 *             or {@link project.pc.device.KeyboardController}) to execute the
 *             input operations.
 *         </li>
 *     </ul>
 * </p>
 * <p>
 *     Client authentication is done by comparing the pairing key received
 *     from the client with the one displayed on the pairing key dialog
 *     window.
 * </p>
 * <p>
 *     It receives data from client and wraps it in a {@link DataWrapper}
 *     object. It, then, invokes the {@link DataWrapper#getOperationType()}
 *     method to examine the type of operation requested by the client and
 *     accordingly transfers the control to either
 *     {@link project.pc.device.MouseController} or
 *     {@link project.pc.device.KeyboardController}.
 * </p>
 * <p>
 *     This thread is started by the {@link Server#listen()} upon accepting
 *     every client connection. It gets terminated implicitly after
 *     unsuccessful authentication of the client. Explicitly, it is terminated
 *     by the {@link NetworkManager} ({@link NetworkManager#stopServer()} and
 *     {@link NetworkManager#disconnect(InetAddress)}) by invoking the
 *     {@link #setStopFlag()} method at the time of server stop or
 *     disconnection of this client A reference of this thread gets stored
 *     in the {@link NetworkState} after successful authentication of the
 *     client.
 * </p>
 *
 * @see java.lang.Runnable
 * @see project.pc.net.DataWrapper
 * @see project.pc.net.DataWrapper#getOperationType()
 * @see project.pc.device.MouseController
 * @see project.pc.device.KeyboardController
 * @see project.pc.net.NetworkManager#stopServer()
 * @see project.pc.net.NetworkManager#disconnect(InetAddress)
 */

public class ServerThread implements Runnable {

    private Socket mClientSocket;
    private MouseController mMouseController;
    private KeyboardController mKeyboardController;
    private BroadcastThread mBroadcastThread;
    private Thread mDialogThread;
    private JDialog mDialog;
    private NetworkState mState;
    private EKEProvider mEKEProvider;
    private ClientInfo mClientInfo;

    private boolean mStopFlag;
    private String mPairingKey;

    /**
     * Constructor.
     *
     * Initializes the thread.
     *
     * @param state {@link NetworkState} of this network.
     * @param skt client <code>Socket</code>.
     * @param mc the {@link project.pc.device.MouseController}.
     * @param kc the {@link project.pc.device.KeyboardController}.
     * @param bt the {@link BroadcastThread}.
     * @throws IOException
     */
    ServerThread(NetworkState state, Socket skt, MouseController mc, KeyboardController kc, BroadcastThread bt)
            throws IOException {
        mClientSocket = skt;
        mMouseController = mc;
        mKeyboardController = kc;
        mBroadcastThread = bt;
        mStopFlag = false;
        mState = state;
        mDialogThread = null;
        mDialog = null;
    }

    private boolean isValidPairingKey() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
        String receivedKey = mEKEProvider.decryptString(in.readLine());
        return mPairingKey.equals(receivedKey);
    }

    private boolean isValidClient() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            mClientInfo = new Gson().fromJson(in.readLine(), ClientInfo.class);
            System.out.println(mClientInfo.getClientInfo() + " wants to connect.");

            mPairingKey = EKEProvider.getPairingKey();
            mEKEProvider = new EKEProvider(mPairingKey, mClientInfo.getPublicKey());

            System.out.println("Type the following pairing key to connect your phone: " + mPairingKey);
            mDialogThread = null;
            mDialogThread = new Thread(
                    () -> MainWindow.showPairingKeyDialog
                            (
                                    mClientInfo.getClientInfo(),
                                    mPairingKey,
                                    this
                            )
            );
            mDialogThread.start();
            return isValidPairingKey();
        } catch (IOException e) {
            System.out.println("Exception in ServerThread:validateClient()");
            e.printStackTrace();
            return false;
        }
    }

    private void closeConnection() {
        try {
            mState.remove(mClientInfo);
            System.out.println(mClientSocket.getInetAddress() + " is now disconnected!");
            if (!mClientSocket.isClosed()) {
                PrintWriter printWriter = new PrintWriter(mClientSocket.getOutputStream(), true);
                if (mEKEProvider != null) {
                    printWriter.println(mEKEProvider.encryptString("Stop"));
                }
                mClientSocket.close();
            }
        } catch (IOException e) {System.out.println("Exception in ServerThread:closeConnection(");}
    }

    private void doOperation() throws InterruptedException {
        while(!mStopFlag) {
            receiveData();
        }
    }

    /**
     * Performs operations for communication with a client.
     * <ul>
     *     <li>Validates the client,</li>
     *     <li>
     *         Receives data from client and encapsulates it in a
     *         {@link DataWrapper} object, and
     *     </li>
     *     <li>
     *         Examines the type of operation requested by the client and
     *         accordingly transfers the control to a <i>controller</i>
     *         ({@link project.pc.device.MouseController} or
     *         {@link project.pc.device.KeyboardController}).
     *     </li>
     * </ul>
     *
     * @see project.pc.net.DataWrapper
     * @see project.pc.device.MouseController
     * @see project.pc.device.KeyboardController
     */
    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(mClientSocket.getOutputStream(), true);
            if(!mBroadcastThread.getBroadcastFlag()) {
                if (isValidClient()) {
                    mClientInfo.setSocket(mClientSocket);
                    out.println(mEKEProvider.encryptString("1"));
                    mDialogThread = null;
                    mDialogThread = new Thread(
                            () -> MainWindow.showConnectionConfirmationDialog
                                    (
                                            mClientInfo.getClientInfo(),
                                            this
                                    )
                    );
                    mDialogThread.start();

                    mClientSocket.setSoTimeout(100);
                    mState.add(mClientInfo, this);

                    doOperation();
                    closeConnection();
                }
                else {
                    mDialogThread = null;
                    mDialogThread = new Thread(
                            () -> MainWindow.showIncorrectPKeyDialog
                                    (
                                            mClientInfo.getClientInfo(),
                                            this
                                    )
                    );
                    mDialogThread.start();

                    out.println(mEKEProvider.encryptString("0"));
                    mClientSocket.close();
                }
            }
            else {
                if(!mClientSocket.isClosed())    mClientSocket.close();
            }
        }
        catch(IOException|InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    private void receiveData() throws InterruptedException {
        try {
            if(mClientSocket.isClosed()) return;
            BufferedReader in = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            if(!in.ready()) return;
            String readData = mEKEProvider.decryptString(in.readLine());
            DataWrapper dataWrapper = new Gson().fromJson(readData, DataWrapper.class);
            if(dataWrapper != null) {
                switch (dataWrapper.getOperationType()) {
                    case "Stop":
                        setStopFlag();
                        break;

                    case "Mouse_Move":
                        mMouseController.move(dataWrapper.getQuaternionObject(), dataWrapper.isInitQuat(), dataWrapper.getSensitivity());
                        break;

                    case "Mouse_Touch":
                        mMouseController.move(dataWrapper.getX(), dataWrapper.getY(), dataWrapper.getSensitivity());
                        break;

                    case "Mouse_Button":
                        mMouseController.doOperation(dataWrapper.getData());
                        break;

                    case "Key":
                        mKeyboardController.doKeyOperation(dataWrapper.getData());
                        break;

                    case "Special_Key":
                        mKeyboardController.doSpecialKeyOperation(dataWrapper.getData());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets stop flag.
     *
     * Used to exit from the {@link #run()} method.
     */
    void setStopFlag() { mStopFlag = true; }

    /**
     * Sets a dialog window.
     *
     * @param d a {@link javax.swing.JDialog} object.
     */
    public void setDialog(JDialog d) { mDialog = d; }

    /**
     * Returns the {@link javax.swing.JDialog} object used for
     * displaying notifications.
     *
     * @return a {@link javax.swing.JDialog} object.
     */
    public JDialog getDialog() {return mDialog;}

    /**
     * Returns the timeout of the client <code>Socket</code>.
     *
     * @return timeout of client <code>Socket</code> in milliseconds.
     * @throws IOException
     */
    int getTimeout() throws IOException {
        if(!mClientSocket.isClosed())   return mClientSocket.getSoTimeout();
        return 0;
    }
}