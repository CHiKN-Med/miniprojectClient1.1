package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class Controller {


    public AnchorPane scene1;
    public AnchorPane scene2;
    public AnchorPane scene3;
    // socket attributes - >
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;


    // JAVAFX ELEMENTS

    public TextArea chatBox = new TextArea();
    public TextField usernameInput = new TextField();
    public TextField chatMessage = new TextField();

    public void initialize() {
        scene1.setVisible(true);
        scene2.setVisible(false);
        scene3.setVisible(false);
        try {
        Socket socket = new Socket("localhost", 8000);
        toServer = new DataOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // read thread
            new Thread(() -> {
                // LOBBY LOOP - >
                while (true) {
                    try {
                        // READING A NEXT MESSAGE FROM THE SERVER AND APPENDING IT TO CHATBOX IN SCENE2
                        chatBox.appendText(readMessage());
                        if (readMessage().equalsIgnoreCase("START GAME")) {
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

    }

    public void joinTheServer(ActionEvent actionEvent) throws IOException {
        // SENDING THE USERNAME TO THE SERVER USING THE SENDMESSAGE FUNCTION
        sendMessage(usernameInput.getText());
        scene1.setVisible(false);
        scene2.setVisible(true);
    }


    public void sendMessageButton(ActionEvent actionEvent) throws IOException {
        sendMessage(chatMessage.getText());
        chatMessage.clear();
    }

    public void sendMessage(String message) throws IOException {
        toServer.writeUTF(message);
        toServer.flush();
    }

    public String readMessage() throws IOException {
        return fromServer.readUTF();
    }


    public void startGame(ActionEvent actionEvent) {
        scene2.setVisible(false);
        scene3.setVisible(true);
    }

    public void answerQOne(ActionEvent actionEvent) {
    }

    public void answerQTwo(ActionEvent actionEvent) {
    }

    public void answerQThree(ActionEvent actionEvent) {
    }

    public void answerQFour(ActionEvent actionEvent) {
    }
}
