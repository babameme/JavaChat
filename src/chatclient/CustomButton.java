package chatclient;
import java.awt.*;

public class CustomButton extends Button{
    ChatClient chatClient;
    public CustomButton(ChatClient chatClient, String s) {
        this.chatClient = chatClient;
        setLabel(s);
        setBackground(new Color(224,236,254));
        setForeground(Color.RED);
    }
}
