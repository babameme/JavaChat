package chatclient;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

public class ChatClient extends Frame implements Serializable, Runnable, KeyListener, ActionListener{
    // Global variables
    Toolkit toolkit;
    MenuItem loginItem, disconnectItem, seperatorItem, quitItem, aboutItem;
    String userName, userRoom, chatLogo, bannerName, roomList;
    StringBuffer stringBuffer;
    int iconCount, G_ILoop, totalUserCount = 0;
    MediaTracker tracker;
    Image imgLogo, imgBanner;
    Image[] iconArray;
    Font textFont;
    Label informationLabel;
    TextField txtMessage;
    CustomButton cmdSend, cmdExit;
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

        // Thiet dat cac thong so
        userName 	= "";
        userRoom	="";
        iconCount 	= 21;
        chatLogo	= "images/logo.gif";
//        bannerName 	= "images/defaultbanner.gif";
        bannerName 	= "images/hocvienanninhnhandan.jpg";
        roomList 	= "";

        // Load anh logo va banner
        tracker = new MediaTracker(this);
        int imageCount = 0;
        imgLogo = toolkit.getImage(chatLogo);
        tracker.addImage(imgLogo,imageCount);
        imageCount++;
        imgBanner 	= toolkit.getImage(bannerName);
        tracker.addImage(imgBanner,imageCount);
        imageCount++;

        // Load icon
        iconArray = new Image[iconCount];
        for(G_ILoop = 0; G_ILoop < iconCount; G_ILoop++)
        {
            iconArray[G_ILoop] = toolkit.getImage("icons/photo"+G_ILoop+".gif");
            tracker.addImage(iconArray[G_ILoop],imageCount);
            imageCount++;
        }

        // TODO: Init PrivateChat Window
        try{
            tracker.waitForAll();
        } catch (InterruptedException e){}

        // Set icon image
        setIconImage(toolkit.getImage("images/logo.gif"));

        InitComponents();
    }

    private void InitComponents() {
        // Background
        setBackground(new Color(224,236,254));
        // Font
        Font font = new Font("Serif", Font.BOLD, 11);
        textFont = new Font("Diaglog", 0, 11);
        setFont(font);
        // Top Panel
        Panel topPanel = new Panel(new BorderLayout());
        topPanel.setBackground(Color.RED);
        Panel logoPanel = new ImagePanel(this, imgLogo);
        topPanel.add("East",logoPanel);
        Panel bannerPanel = new ImagePanel(this, imgBanner);
        topPanel.add("West",bannerPanel);
        add("North",topPanel);

        // Panel Thong tin user
        Panel centerPanel = new Panel(new BorderLayout());
        Panel informationPanel = new Panel(new BorderLayout());
        informationPanel.setBackground(Color.blue);
        informationLabel = new Label();
        informationLabel.setAlignment(1);
        UpdateinformationLabel();
        informationLabel.setForeground(Color.pink);
        informationPanel.add("Center",informationLabel);
        centerPanel.add("North",informationPanel);

        // TODO: Message Canvas - Add to message Panel, centerPanel

        // Input Panel - Textbook
        Panel inputPanel = new Panel(new BorderLayout());
        Panel textBoxPanel = new Panel(new BorderLayout());
        Label lblGeneral = new Label("General Message!");
        txtMessage = new TextField();
        txtMessage.addKeyListener(this);
        txtMessage.setFont(textFont);
        cmdSend = new CustomButton(this,"Send Message!");
        cmdSend.addActionListener(this);
        textBoxPanel.add("West",lblGeneral);
        textBoxPanel.add("Center",txtMessage);
        textBoxPanel.add("East",cmdSend);
        inputPanel.add("Center",textBoxPanel);

        Panel inputButtonPanel =new Panel(new BorderLayout());
        cmdExit = new CustomButton(this,"Exit Chat");
        cmdExit.addActionListener(this);
        inputButtonPanel.add("Center",cmdExit);
        inputPanel.add("East",inputButtonPanel);

        // TODO: empty Panel ?? neccesarry
        centerPanel.add("South", inputPanel);

        add("Center", centerPanel);
    }

    private void UpdateinformationLabel() {
        stringBuffer = new StringBuffer();
        stringBuffer.append("User Name: ");
        stringBuffer.append(userName);
        stringBuffer.append("       ");
        stringBuffer.append("Room Name: ");
        stringBuffer.append(userRoom);
        stringBuffer.append("       ");
        stringBuffer.append("Numbers Of Users: ");
        stringBuffer.append(totalUserCount);
        stringBuffer.append("       ");
        informationLabel.setText(stringBuffer.toString());
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
