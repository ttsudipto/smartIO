package net;

import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.google.gson.Gson;
import device.KeyboardController;
import device.MouseController;
import gui.MainWindow;
import security.EKEProvider;

import javax.swing.JDialog;

/**
 * @author Sudipto Bhattacharjee
 * @author Sayantan Majumdar
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
    private String mReceivedKey;

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
        mReceivedKey = in.readLine();
        return mPairingKey.equals(mReceivedKey);
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

                    if(mReceivedKey != null && !mReceivedKey.equals("Cancelled")) {
                        out.println(mEKEProvider.encryptString("0"));
                    }
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
                        mMouseController.move(dataWrapper.getQuaternionObject(), dataWrapper.isInitQuat());
                        break;

                    case "Mouse_Touch":
                        mMouseController.move(dataWrapper.getX(), dataWrapper.getY());
                        break;

                    case "Mouse_Button":
                        mMouseController.doOperation(dataWrapper.getData());
                        break;

                    case "Key":
                        mKeyboardController.doKeyOperation(dataWrapper.getData());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setStopFlag() { mStopFlag = true; }
    public void setDialog(JDialog d) { mDialog = d; }
    public JDialog getDialog() {return mDialog;}

    int getTimeout() throws IOException {
        if(!mClientSocket.isClosed())   return mClientSocket.getSoTimeout();
        return 0;
    }
}