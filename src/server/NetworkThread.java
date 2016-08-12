package server;

/**
 * Created by Sudipto Bhattacharjee on 12/8/16.
 */
public class NetworkThread implements Runnable {
    private NetworkManager manager;
    private static boolean doIt, option;

    public NetworkThread(NetworkManager manager) {
        this.manager = manager;
        doIt = false;
    }

    public void updateParameters(boolean doIt, boolean option) {
        this.doIt = doIt;
        this.option = option;
        System.out.println(this.doIt + " " + this.option);
    }

    @Override
    public void run() {
        try {
//            while (true) {
////                System.out.println(doIt + " " + option);
//                if (option == true && doIt == true){
//                    doIt = false;
                    manager.startServer();
//                    Thread.currentThread().suspend();
//                }
//                if (option == false && doIt == true) {
//                    System.out.println("foo");
//                    doIt = false;
////                    manager.stopServer();
//                    break;
//                }
//            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
