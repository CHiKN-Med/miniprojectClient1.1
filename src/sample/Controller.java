package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    //QUIZ BUTTONS
    public Button answerButtonOne;
    public Button answerButtonFour;
    public Button answerButtonThree;
    public Button answerButtonTwo;
    public AnchorPane root;
    public TextField answerOne;
    public TextField answerTwo;
    public Button sendMessage;
    public Button startGame;
    public TextArea messageArea;


    // socket attributes - >
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    Socket socket;

    {
        try {
            if (socket == null) {
                socket = new Socket("localhost", 8000);
                System.out.println("connecting");
                System.out.println(socket == null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // JAVAFX ELEMENTS

    public TextArea chatBox = new TextArea();
    public TextField usernameInput = new TextField();
    public TextField chatMessage = new TextField();

    public void sendMessage(String message) throws IOException {
        toServer.writeUTF(message);
        toServer.flush();
    }

    public String readMessage() throws IOException {
        String m = null;
        m = fromServer.readUTF();
        return m;
    }

    public void joinTheServer(ActionEvent actionEvent) throws IOException {
        //CHANGING SCENE TO THE SCENE2.FXML WITH THE FOLLOWING CODE
        Parent scene2Parent = FXMLLoader.load(getClass().getResource("scene2.fxml"));
        Scene scene2 = new Scene(scene2Parent);
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene2);
        stage.show();

        // SENDING THE USERNAME TO THE SERVER USING THE SENDMESSAGE FUNCTION
        String message = usernameInput.getText();
        chatBox.appendText(message);
        sendMessage(message);
    }

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        sendMessage(chatMessage.getText());
        chatMessage.clear();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
        // Create a socket to connect to the server

        // Create an input stream to receive data from the server
        fromServer = new DataInputStream(socket.getInputStream());
        // Create an output stream to send data to the server
        toServer = new DataOutputStream(socket.getOutputStream());

        // read thread
        new Thread(() -> {
            try {
                if (!chatBox.getText().isEmpty()){
                chatBox.appendText(readMessage());}
            } catch (IOException e) {
                e.printStackTrace();
            }
            // LOBBY LOOP - >
            while(true){
                try {
                    // READING A NEXT MESSAGE FROM THE SERVER AND APPENDING IT TO CHATBOX IN SCENE2
                    System.out.println("1");
                    chatBox.appendText(readMessage());
                    System.out.println("2");
                    //if(readMessage().equalsIgnoreCase("START GAME")){
                    //break;
                    //}
                } catch (IOException e) {
                    e.printStackTrace(); }
            }
        }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

     //QUIZ BUTTON METHODS
    }
    public void answerQOne(ActionEvent actionEvent) {

    }

    public void answerQTwo(ActionEvent actionEvent) {
    }

    public void answerQThree(ActionEvent actionEvent) {
    }

    public void answerQFour(ActionEvent actionEvent) {
    }


    public void startGame(ActionEvent actionEvent) throws IOException {
        //CHANGING SCENE TO THE SCENEQUIZ.FXML WITH THE FOLLOWING CODE
        Parent scene2Parent = FXMLLoader.load(getClass().getResource("quizScene.fxml"));
        Scene scene3 = new Scene(scene2Parent);
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene3);
        stage.show();
    }
}
