package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Controller {



    // socket attributes - >
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    // JAVAFX ELEMENTS

    public TextArea chatBox = new TextArea();
    public TextField usernameInput = new TextField();
    public TextField chatMessage = new TextField();
    public TextArea quizBox;

    public AnchorPane scene1;
    public AnchorPane scene2;
    public AnchorPane scene3;

    public void initialize() {
        scene1.setVisible(true);
        scene2.setVisible(false);
        scene3.setVisible(false);
            // read thread
            new Thread(() -> {
                // LOBBY LOOP - >
                try {
                Socket socket = new Socket("localhost", 8000);
                toServer = new DataOutputStream(socket.getOutputStream());
                fromServer = new DataInputStream(socket.getInputStream());
                while (true) {
                        // READING A NEXT MESSAGE FROM THE SERVER AND APPENDING IT TO CHATBOX IN SCENE2
                        chatBox.appendText(readMessage());
                        if (readMessage().equalsIgnoreCase("STARTTHEGAME")) {
                            scene2.setVisible(false);
                            scene3.setVisible(true);
                            break;
                        }
                    }

                while (true) {
                        quizBox.appendText(readMessage());
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
    }

    public void joinTheServer(ActionEvent actionEvent) throws IOException {
        // SENDING THE USERNAME TO THE SERVER USING THE SENDMESSAGE FUNCTION
        sendMessage(usernameInput.getText());
        scene1.setVisible(false);
        scene2.setVisible(true);
    }


    public void sendMessageButton(ActionEvent actionEvent) {
        sendMessage(chatMessage.getText());
        chatMessage.clear();
    }

    public void sendMessage(String message) {
        try {
            toServer.writeUTF(message);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendInt(int answer) throws IOException{
        toServer.writeInt(answer);
        toServer.flush();
    }

    public int readInt() throws IOException {
        return fromServer.readInt();
    }

    public String readMessage() throws IOException {
        return fromServer.readUTF();
    }

    public void startGame(ActionEvent actionEvent) throws IOException {
        // IF A USER CLICKS THE START GAME BUTTON A MESSAGE GETS SEND THAT TELLS THE SERVER TO START THE GAME
        sendMessage("STARTTHEGAME");
    }

    public void answerQOne(ActionEvent actionEvent) throws IOException {
        sendInt(1);
    }

    public void answerQTwo(ActionEvent actionEvent) throws IOException {
        sendInt(2);
    }

    public void answerQThree(ActionEvent actionEvent) throws IOException {
        sendInt(3);
    }

    public void answerQFour(ActionEvent actionEvent) throws IOException {
        sendInt(4);
    }
}
