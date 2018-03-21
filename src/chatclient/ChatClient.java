package chatclient;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

public class ChatClient extends Frame implements Serializable, Runnable, KeyListener, ActionListener{
    // Global variables
    Toolkit toolkit;
    MenuItem loginItem, disconnectItem, seperatorItem, quitItem, aboutItem;
    //
    public ChatClient(){
        // Thiet dat size cho frame
        toolkit = Toolkit.getDefaultToolkit();
        // Width > 778 thi de max theo ti le
        if(toolkit.getScreenSize().getWidth() > 778)
            setSize(778, 575);
        else
            setSize((int)toolkit.getScreenSize().getWidth(),(int)toolkit.getScreenSize().getHeight() - 20);
        // Khong thay doi kich thuoc
        setResizable(false);
        // Border
        setLayout(new BorderLayout());

        // TODO: Change to common settings
        setTitle("C500 Client Chat");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // TODO: Disconnect Chat
                System.exit(0);
            }
        });
        // LOading menubar
        MenuBar menuBar = new MenuBar();
        Menu loginMenu = new Menu("Login");
        loginItem = new MenuItem("Login");
        loginItem.addActionListener(this);
        disconnectItem = new MenuItem("Disconnect");
        disconnectItem.addActionListener(this);
        seperatorItem = new MenuItem("-");
        quitItem = new MenuItem("Quit");
        quitItem.addActionListener(this);
        loginMenu.add(loginItem);
        loginMenu.add(disconnectItem);
        loginMenu.add(seperatorItem);
        loginMenu.add(quitItem);

        Menu aboutMenu = new Menu("Help ");
        aboutItem = new MenuItem("About " + "C500 chat");
        aboutItem.addActionListener(this);
        aboutMenu.add(aboutItem);

        menuBar.add(loginMenu);
        menuBar.add(aboutMenu);
        setMenuBar(menuBar);
    }

    public static void main(String[] args) {
        ChatClient mainFrame = new ChatClient();
        mainFrame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {

    }
}
