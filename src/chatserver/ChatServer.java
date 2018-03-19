package chatserver;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class ChatServer extends Frame implements Serializable, ActionListener, Runnable, CommonSettings{
    // Global Variable
    Button cmdStart, cmdStop;
    ServerSocket serverSocket;
    Socket socket;
    Properties DBProperties;
    ArrayList userArrayList, messageArrayList;
    Thread thread;
    ChatCommunication chatCommunication;
    DataOutputStream dataOutputStream;
    int G_ILoop;
    ClientObject clientObject;
    String roomList;
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void run() {

    }
}
