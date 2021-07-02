package ml.pkom.playerfarms.bungee;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Functions {
    public static int OFFLINE = 0;
    public static int ONLINE = 1;    
    public static int OTHER = 2;

    public static int getServerState(String address, int port){
        try {
            Socket socket = new Socket(address, port);
            socket.close();
            return ONLINE;
        } catch (UnknownHostException e) {
            return OFFLINE;
        } catch (IOException e) {
            return OFFLINE;
        }
    }
}
