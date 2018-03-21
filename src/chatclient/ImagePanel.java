package chatclient;

import java.awt.*;

public class ImagePanel extends Panel {
    // Global Variable
    ChatClient parent;
    Image displayImage;
    public ImagePanel(ChatClient chatClient, Image imgLogo) {
        setLayout(null);
        parent = chatClient;
        displayImage = imgLogo;
        int xPos = imgLogo.getWidth(this);
        int yPos = imgLogo.getHeight(this);
        setBounds(0, 0, xPos + 10, yPos + 10);
    }

    public void paint(Graphics graphics){
        graphics.drawImage(displayImage, 10, 10, this);
    }
}
