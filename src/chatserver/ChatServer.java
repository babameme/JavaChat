package chatserver;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public ChatServer(){
        // Khoi tao tat ca cac thanh phan
        this.setTitle("Chat Server");
        Image iconImage = Toolkit.getDefaultToolkit().getImage("/images/icon.gif");
        this.setIconImage(iconImage);
        this.setResizable(false);
        this.setBackground(Color.ORANGE);
        this.setLayout(new BorderLayout());

        // Panel tren cung
        Panel topPanel = new Panel(new BorderLayout());
        topPanel.setBackground(Color.GRAY);
        Label labelTitle = new Label("C500 Chat Server V1.0", 1);
        labelTitle.setForeground(Color.WHITE);
        labelTitle.setFont(new Font("Helvitica", Font.BOLD, 20));
        topPanel.add("Center", labelTitle);
        add("North", topPanel);

        // Panel giua
        Panel centerPanel = new Panel(null);
        cmdStart = new Button("START SERVER");
        cmdStart.setBounds(125,10,150,30);
        cmdStart.addActionListener(this);
        centerPanel.add(cmdStart);

        cmdStop = new Button("STOP SERVER");
        cmdStop.setBounds(125,50,150,30);
        cmdStop.setEnabled(false);
        cmdStop.addActionListener(this);
        centerPanel.add(cmdStop);
        add("Center",centerPanel);

        //
        this.setSize(400, 150);
        this.show();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ExitServer();
                dispose();
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if(evt.getActionCommand().equalsIgnoreCase("Start Server"))
        {
            DBProperties = GetDBProperties();
            /*********Initialize the Server Socket*********/
            try {
                roomList = "";
                if(DBProperties.getProperty("roomlist") != null)
                {
                    roomList = 	DBProperties.getProperty("roomlist");
                }

                int m_portNo = 1436;
                if(DBProperties.getProperty("portno") != null)
                    m_portNo = Integer.parseInt(DBProperties.getProperty("portno"));
                serverSocket = new ServerSocket(m_portNo);
            }catch(IOException _IOExc) { }

            /********Khoi tao array list**********/
            userArrayList = new ArrayList();
            messageArrayList = new ArrayList();

            /********Khoi tao thread*************/
            thread = new Thread(this);
            thread.start();

            cmdStart.setEnabled(false);
            cmdStop.setEnabled(true);
        }

        if(evt.getActionCommand().equalsIgnoreCase("Stop Server"))
        {
            ExitServer();
            cmdStop.setEnabled(false);
            cmdStart.setEnabled(true);
        }
    }

    private Properties GetDBProperties() {
        Properties	DBProperties = new Properties();
        try
        {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("server.properties");
            DBProperties.load(inputStream);
            inputStream.close();
        }
        catch (IOException _IOExc){ }
        finally
        {
            return (DBProperties);
        }
    }

    @Override
    public void run() {
        // Chap nhan cac ket noi tu client va tao chat communication
        while (thread != null){
            try{
                socket = serverSocket.accept();
                chatCommunication = new ChatCommunication(this.socket);
                thread.sleep(THREAD_SLEEP_TIME);
            }
            catch(InterruptedException _INExc){
                ExitServer();
            }
            catch (IOException _IOExc){
                ExitServer();
            }
        }
    }

    private void SendMessageToClient(Socket clientSocket, String message){
        try{
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dataOutputStream.write(new String(message+"\r\n").getBytes());
        } catch (IOException _IOExc){}
    }

    // Tra ve doi tuong Client voi User Name
    private ClientObject GetClientObject(String userName) {
        ClientObject returnClientObject = null;
        ClientObject TempClientObject;
        int m_userListSize = userArrayList.size();
        for (G_ILoop = 0; G_ILoop < m_userListSize; G_ILoop++) {
            TempClientObject = (ClientObject) userArrayList.get(G_ILoop);
            if (TempClientObject.getClientUserName().equalsIgnoreCase(userName)) {
                returnClientObject = TempClientObject;
                break;
            }
        }
        return returnClientObject;
    }

    // Check neu user Name ton tai
    private boolean IsUserExists(String userName)
    {
        if(GetClientObject(userName) != null){
            return true;
        }
        else {
            return false;
        }
    }

    // Lay chi so cua userName
    private int GetIndexOf(String userName)
    {
        int m_userListSize = userArrayList.size();
        for(G_ILoop = 0; G_ILoop < 	m_userListSize; G_ILoop++)
        {
            clientObject = (ClientObject) userArrayList.get(G_ILoop);
            if(clientObject.getClientUserName().equalsIgnoreCase(userName))
                return G_ILoop;
        }
        return -1;
    }

    // Them Client ung voi Socket va UserName vao Server List
    protected void AddUser(Socket clientSocket,String userName)
    {
        /***Neu da ton tai thi return**/
        if(IsUserExists(userName))
        {
            SendMessageToClient(clientSocket,"EXIS");
            return;
        }

        /****Gui di mot Room list **/
        SendMessageToClient(clientSocket,"ROOM "+roomList);

        /******Gui thong tin user moi toi tat ca User****/
        int m_userListSize = userArrayList.size();
        String m_addRFC = "ADD  "+ userName;
        StringBuffer stringbuffer = new StringBuffer("LIST ");
        for(G_ILoop = 0; G_ILoop < m_userListSize; G_ILoop++)
        {
            clientObject = (ClientObject) userArrayList.get(G_ILoop);
            /***Kiem tra ten phong*****/
            if(clientObject.getClientRoomName().equals(ROOM_NAME))
            {
                SendMessageToClient(clientObject.getClientSocket(),m_addRFC);
                stringbuffer.append(clientObject.getClientUserName());
                stringbuffer.append(";");
            }
        }

        /*****Them user vao array list***/
        clientObject = new ClientObject(clientSocket, userName, ROOM_NAME);
        userArrayList.add(clientObject);

        /********Gui userName toi list nguoi dung moi***********/
        stringbuffer.append(userName);
        stringbuffer.append(";");
        SendMessageToClient(clientSocket,stringbuffer.toString());
    }

    /**********Xoa nguoi dung khoi Server**************/
    public void RemoveUser(String userName, String roomName, int removeType)
    {
        ClientObject removeClientObject = GetClientObject(userName);
        if(removeClientObject != null)
        {
            userArrayList.remove(removeClientObject);
            userArrayList.trimToSize();
            int m_userListSize = userArrayList.size();
            String m_RemoveRFC=null;
            if(removeType == REMOVE_USER)
                m_RemoveRFC = "REMO " + userName;
            if(removeType == KICK_USER)
                m_RemoveRFC = "INKI " + userName;
            /*****Gui ma REMO toi cac user khac****/
            for(G_ILoop = 0; G_ILoop < m_userListSize; G_ILoop++)
            {
                clientObject = (ClientObject) userArrayList.get(G_ILoop);
                if(clientObject.getClientRoomName().equals(roomName))
                    SendMessageToClient(clientObject.getClientSocket(),m_RemoveRFC);
            }
        }
    }

    /**********Xoa nguoi dung khi co Exception **************/
    protected void RemoveUserWhenException(Socket clientSocket)
    {
        int m_userListSize = userArrayList.size();
        ClientObject removeclientobject;
        for(G_ILoop = 0; G_ILoop < m_userListSize; G_ILoop++)
        {
            removeclientobject = (ClientObject) userArrayList.get(G_ILoop);
            if(removeclientobject.getClientSocket().equals(clientSocket))
            {
                String m_RemoveUserName = removeclientobject.getClientUserName();
                String m_RemoveRoomName = removeclientobject.getClientRoomName();
                userArrayList.remove(removeclientobject);
                userArrayList.trimToSize();
                m_userListSize = userArrayList.size();
                String m_RemoveRFC="REMO " + m_RemoveUserName;

                /*****Gui ma REMO RFC toi cac nguoi dung khac****/
                for(int m_ILoop = 0; m_ILoop < m_userListSize; m_ILoop++)
                {
                    clientObject = 	(ClientObject) userArrayList.get(m_ILoop);
                    if(clientObject.getClientRoomName().equals(m_RemoveRoomName))
                        SendMessageToClient(clientObject.getClientSocket(),m_RemoveRFC);
                }
                return;
            }
        }
    }

    /*********Doi phong ***********/
    public void ChangeRoom(Socket clientSocket,String userName, String newRoomName)
    {
        int m_clientIndex = GetIndexOf(userName);
        if(m_clientIndex >= 0)
        {
            // Cap nhat OldRoom thanh newRoom va gui ma RFC
            ClientObject TempClientObject = (ClientObject) userArrayList.get(m_clientIndex);
            String m_oldRoomName = TempClientObject.getClientRoomName();
            TempClientObject.setClientRoomName(newRoomName);
            userArrayList.set(m_clientIndex,TempClientObject);
            SendMessageToClient(clientSocket,"CHRO " + newRoomName);

            // Gui tat ca danh sach nguoi dung tham gia phong
            int m_userListSize = userArrayList.size();
            StringBuffer stringbuffer = new StringBuffer("LIST ");
            for(G_ILoop = 0; G_ILoop < m_userListSize; G_ILoop++)
            {
                clientObject = (ClientObject) userArrayList.get(G_ILoop);
                // Kiem tra ten phong
                if(clientObject.getClientRoomName().equals(newRoomName))
                {
                    stringbuffer.append(clientObject.getClientUserName());
                    stringbuffer.append(";");
                }
            }
            SendMessageToClient(clientSocket,stringbuffer.toString());


            // Thong bao nguoi dung phong cu, phong moi
            String m_OldRoomRFC = "LERO " + userName+"~" + newRoomName;
            String m_NewRoomRFC = "JORO " + userName;
            for(G_ILoop = 0; G_ILoop < m_userListSize; G_ILoop++)
            {
                clientObject = (ClientObject) userArrayList.get(G_ILoop);
                if(clientObject.getClientRoomName().equals(m_oldRoomName))
                    SendMessageToClient(clientObject.getClientSocket(), m_OldRoomRFC);
                if((clientObject.getClientRoomName().equals(newRoomName)) && (!(clientObject.getClientUserName().equals(userName))))
                    SendMessageToClient(clientObject.getClientSocket(), m_NewRoomRFC);
            }
        }
    }

    // Gui tin nhan chung
    protected void SendGeneralMessage(Socket clientSocket, String message, String userName,String roomName)
    {
        boolean m_floodFlag = false;
        messageArrayList.add(userName);
        if(messageArrayList.size() > MAX_MESSAGE)
        {
            messageArrayList.remove(0);
            messageArrayList.trimToSize();

            /*********Kiem tra neu nguoi dung dang lam tran tin nhan*********/
            String m_firstMessage = (String) messageArrayList.get(0);
            int m_messageListSize = messageArrayList.size();
            for(G_ILoop = 1; G_ILoop < 	m_messageListSize; G_ILoop++)
            {
                if(messageArrayList.get(G_ILoop).equals(m_firstMessage))
                {
                    m_floodFlag = true;
                }
                else
                {
                    m_floodFlag = false;
                    break;
                }
            }
        }

        /*******Gui tin nhan chung toi tat ca nguoi dung # **/
        int m_userListSize = userArrayList.size();
        String m_messageRFC = "MESS " + userName+":" + message;
        for(G_ILoop = 0; G_ILoop < m_userListSize; G_ILoop++)
        {
            clientObject = (ClientObject) userArrayList.get(G_ILoop);
            if((clientObject.getClientRoomName().equals(roomName)) && (!(clientObject.getClientUserName().equals(userName))))
            {
                SendMessageToClient(clientObject.getClientSocket(), m_messageRFC);
            }
        }

        /******** :)) Kick nguoi dung neu tiep tuc lam tran tin nhan **/
        if(m_floodFlag)
        {
            SendMessageToClient(clientSocket,"KICK ");
            messageArrayList.clear();
        }
    }

    // Gui tin nhan rieng tu
    protected void SendPrivateMessage(String message , String ToUserName)
    {
        clientObject = GetClientObject(ToUserName);
        if(clientObject != null)
        {
            SendMessageToClient(clientObject.getClientSocket(),"PRIV " + message);
        }

    }




    private void ExitServer() {
        // Xoa object
        if(thread != null)
        {
            thread.stop();
            thread = null;
        }
        try {
            if(serverSocket != null)
            {
                serverSocket.close();
                serverSocket = null;
            }
        }catch(IOException _IOExc) { }
        userArrayList = null;
        messageArrayList = null;
        cmdStop.setEnabled(false);
        cmdStart.setEnabled(true);
    }
}
