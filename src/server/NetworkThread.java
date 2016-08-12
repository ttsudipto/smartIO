package server;

/**
 * Created by Sudipto Bhattacharjee on 12/8/16.
 */
public class NetworkThread implements Runnable {
    private NetworkManager manager;

    public NetworkThread(NetworkManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
                manager.startServer();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
