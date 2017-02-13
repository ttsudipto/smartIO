package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.google.gson.Gson;
import device.KeyboardController;
import device.MouseController;
import security.EKEProvider;

/**
 * @author Sudipto Bhattacharjee
 * @author Sayantan Majumdar
 */

class ServerThread implements Runnable {

    private Socket mClientSocket;
    private MouseController mMouseController;
    private KeyboardController mKeyboardController;
    private boolean mStopFlag;
    private NetworkState mState;
    private EKEProvider mEKEProvider;

    ServerThread(NetworkState state, Socket skt, MouseController mc, KeyboardController kc,
                 byte[] clientPublicKey, String pairingKey)
            throws IOException {
        mClientSocket = skt;
        mMouseController = mc;
        mKeyboardController = kc;
        mStopFlag = false;
        this.mState = state;
        mClientSocket.setSoTimeout(100);
        mEKEProvider = new EKEProvider(pairingKey, clientPublicKey);
    }

    @Override
    public void run() {
        try {
            while(!mStopFlag) {
                receiveData();
            }
            mState.remove(mClientSocket);
            System.out.println(mClientSocket.getInetAddress() + " is now disconnected!");
            if(!mClientSocket.isClosed()) {
                PrintWriter printWriter = new PrintWriter(mClientSocket.getOutputStream(), true);
                if (mEKEProvider != null) {
                    printWriter.println(mEKEProvider.encryptString("Stop"));
                }
                mClientSocket.close();
            }
        }
        catch(Exception e) {
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
                        mMouseController.move(dataWrapper.getQuaternionObject());
                        break;

                    case "Mouse_Button":
                        mMouseController.doOperation(dataWrapper.getData());
                        break;

                    case "Key":
                        //length of "Key" = 3 and next char is space. Hence beginIndex = 4
                        mKeyboardController.doKeyOperation(dataWrapper.getData());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setStopFlag() { mStopFlag = true; }

    int getTimeout() throws IOException {
        if(!mClientSocket.isClosed())   return mClientSocket.getSoTimeout();
        return 0;
    }
}