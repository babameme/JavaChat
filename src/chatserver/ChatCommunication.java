package chatserver;

import java.io.DataInputStream;
import java.net.Socket;

public class ChatCommunication implements Runnable, CommonSettings{
    // Global Variable
    Thread thread;
    Socket socket;
    DataInputStream inputStream;
    String RFC;
    //ChatServer
    @Override
    public void run() {

    }
}
