package chatserver;

import java.net.Socket;

public class ClientObject {
    Socket ClientSocket;
    String ClientUserName, ClientRoomName;

    public ClientObject(Socket clientSocket, String clientUserName, String clientRoomName) {
        ClientSocket = clientSocket;
        ClientUserName = clientUserName;
        ClientRoomName = clientRoomName;
    }

    public Socket getClientSocket() {
        return ClientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        ClientSocket = clientSocket;
    }

    public String getClientUserName() {
        return ClientUserName;
    }

    public void setClientUserName(String clientUserName) {
        ClientUserName = clientUserName;
    }

    public String getClientRoomName() {
        return ClientRoomName;
    }

    public void setClientRoomName(String clientRoomName) {
        ClientRoomName = clientRoomName;
    }
}
