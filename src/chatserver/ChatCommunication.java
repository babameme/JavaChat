package chatserver;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatCommunication implements Runnable, CommonSettings{
    // Global Variable
    Thread thread;
    Socket socket;
    DataInputStream inputStream;
    String RFC;
    ChatServer parent;

    public ChatCommunication(Socket socket, ChatServer parent) {
        this.socket = socket;
        this.parent = parent;
        try{
            inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        }catch (IOException _IOExc){}
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(thread != null)
        {
            try {
                RFC = inputStream.readLine();
                /*******RFC Checking**************/
                if(RFC.startsWith("HELO"))
                {
                    parent.AddUser(socket,RFC.substring(5));
                }

                if(RFC.startsWith("QUIT"))
                {
                    parent.RemoveUser(RFC.substring(5,RFC.indexOf("~")),RFC.substring(RFC.indexOf("~")+1),REMOVE_USER);
                    QuitConnection();
                }

                if(RFC.startsWith("KICK"))
                {
                    parent.RemoveUser(RFC.substring(5,RFC.indexOf("~")),RFC.substring(RFC.indexOf("~")+1),KICK_USER);
                    QuitConnection();
                }

                if(RFC.startsWith("CHRO"))
                {
                    parent.ChangeRoom(socket,RFC.substring(5,RFC.indexOf("~")),RFC.substring(RFC.indexOf("~")+1));
                }

                if(RFC.startsWith("MESS"))
                {
                    parent.SendGeneralMessage(socket,RFC.substring(RFC.indexOf(":")+1),RFC.substring(RFC.indexOf("~")+1,RFC.indexOf(":")),RFC.substring(5,RFC.indexOf("~")));
                }

                if(RFC.startsWith("PRIV"))
                {
                    parent.SendPrivateMessage(RFC.substring(RFC.indexOf("~")+1),RFC.substring(5,RFC.indexOf("~")));
                }

                if(RFC.startsWith("ROCO"))
                {
                    parent.GetUserCount(socket,RFC.substring(5));
                }
            }catch(Exception _Exc) { parent.RemoveUserWhenException(socket);QuitConnection();}
        }
    }

    private void QuitConnection()
    {
        thread.stop();
        thread = null;
        try {
            socket.close();
        }catch(IOException _IOExc) { }
        socket = null;
    }
}
