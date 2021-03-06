package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // ANCHOR PANES FOR SCENES:
    public AnchorPane scene0;
    public AnchorPane scene1;
    public AnchorPane scene2;
    public AnchorPane scene3;
    public AnchorPane scene4;
    public AnchorPane scene5;

    // ATTRIBUTES FOR FXML ELEMENTS IN SCENES:
    public TextArea quizAnswerOptions;
    public TextArea correctAnswer;
    public Button answerButtonOne;
    public Button answerButtonTwo;
    public Button answerButtonThree;
    public Button answerButtonFour;
    public Button sendMessageButton;
    public Button startGame;
    public TextArea winnerNameBox;
    public TextArea scoreBoardBox;
    public TextField EnterIPtxt;
    public Button IPEnter;
    public TextArea chatBox = new TextArea();
    public TextField usernameInput = new TextField();
    public TextField chatMessage = new TextField();
    public TextArea quizBox;

    // socket attributes:
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    private Socket socket = null;

    // ATTRIBUTES:
    private String ip = null;
    private boolean iPressed = false;
    private boolean joinServer = false;


    // INITIALIZE METHOD IS RUN WHEN CLIENT OPENS:
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // SCENE 0 IS SET TO BE VISIBLE WHILE ALL OTHER SCENES ARE NOT
        scene0.setVisible(true);
        scene1.setVisible(false);
        scene2.setVisible(false);
        scene3.setVisible(false);
        scene4.setVisible(false);
        scene5.setVisible(false);

        // STARTING NEW THREAD IN ORDER TO READ MESSAGES FROM SERVER
        new Thread(() -> {

            // WHILE LOOP WHICH BREAKS WHEN THE USER INPUTS IP OG PRESSES LOCALHOST-BUTTON
            while (!joinServer && ip != "localhost")
            {
                System.out.println("waiting for IP");
            }

            // CLIENT CONNECTS TO SERVER VIA THE CHOSEN IP
            try {
                try {
                    socket = new Socket(ip, 8000);
                    toServer = new DataOutputStream(socket.getOutputStream());
                    fromServer = new DataInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // READING NEXT MESSAGE FROM THE SERVER AND APPENDING IT TO CHATBOX IN SCENE2
                while (true) {
                    String message = fromServer.readUTF();
                    chatBox.appendText(message);

                    // IF THE MESSAGE FROM THE SERVER EQUALS "STARTTHEGAME" THE LOOP BREAKS AND SCENE CHANGES.
                    if (message.equalsIgnoreCase("STARTTHEGAME")) {
                        if (!iPressed)

                        { // IF THE CLIENTS NOT IS THE ONE WHO PRESSED START GAME, THE CLIENT SENDS AN EMPTY MESSAGE TO FIX BUG AND SYNCRONIZE
                            sendMessage("");
                        }

                        scene2.setVisible(false);
                        scene3.setVisible(true);
                        break;
                    }
                }

                // LOOP WHICH APPENDS QUESTION, OPTIONS AND RIGHT ANSWER TO THE SCENE
                while (true) {
                    // READ FIRST MESSAGE FROM THE SERVER (THE QUESTION)
                    String message = fromServer.readUTF();
                    // WHEN THE FIRST MESSAGE IS READ CLEAR ALL TEXTAREAS
                    quizBox.clear();
                    quizAnswerOptions.clear();
                    correctAnswer.clear();
                    // IF THE MESSAGE IS "STOPTHEGAME" THE LOOP BREAKS
                    if (message.equalsIgnoreCase("STOPTHEGAME")) {
                        scene3.setVisible(false);
                        scene4.setVisible(true);
                        break;
                    }

                    // APPEND QUESTION TO QUIZ BOX
                    quizBox.appendText(message);
                    // READ NEXT MESSAGE (THE OPTIONS)
                    message = fromServer.readUTF();
                    // APPENDING THE OPTION TO quizAnswerOptions
                    quizAnswerOptions.appendText(message);
                    // READ NEXT MESSAGE (THE RIGHT ANSWER)
                    message = fromServer.readUTF();
                    // APPENDING MESSAGE TO correctAnswerBox
                    correctAnswer.appendText(message);
                }

                // WAITING FOR THE NEXT MESSAGE FROM SERVER - THE NEXT MESSAGE SHOULD BE "SHOWTHESCORE" WHEN EVERY CLIENT IS DONE
                // AND THEN THE CLIENT CHANGES SCENE
                String message = fromServer.readUTF();
                if (message.equalsIgnoreCase("SHOWTHESCORE")){
                    scene4.setVisible(false);
                    scene5.setVisible(true);}

                // READING NEXT MESSAGE FROM SERVER (THE WINNER)
                String messageFromServer = fromServer.readUTF();
                // APPENDING MESSAGE
                winnerNameBox.appendText(messageFromServer);
                // READING NEXT MESSAGE FROM SERVER (THE SCOREBOARD)
                String messageFromServer2 = fromServer.readUTF();
                // APPENDING MESSAGE
                scoreBoardBox.appendText(messageFromServer2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }



    // ACQUIRING IP FROM USER BEFORE CREATING A USER
    public void SendIP(ActionEvent actionEvent) throws IOException {
        ip = EnterIPtxt.getText();
        System.out.println(ip);
        scene0.setVisible(false);
        scene1.setVisible(true);
        joinServer = true;
    }

    // SENDING THE USERNAME TO THE SERVER USING THE SENDMESSAGE() FUNCTION
    public void joinTheServer(ActionEvent actionEvent) throws IOException {
        sendMessage(usernameInput.getText());
        scene1.setVisible(false);
        scene2.setVisible(true);
    }

    //ON ACTION METHOD FOR "SEND" BUTTON IN THE LOBBY CHAT
    public void sendMessageButton(ActionEvent actionEvent) {
        sendMessage(chatMessage.getText());
        chatMessage.clear();
    }

    //METHOD WHICH SENDS A STRING INPUT TO THE SERVER
    public void sendMessage(String message) {
        try {
            toServer.writeUTF(message);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //METHOD WHICH SENDS AN INT INPUT TO THE SERVER
    public void sendInt(int answer) throws IOException {
        toServer.writeInt(answer);
        toServer.flush();
    }

    // IF A USER CLICKS THE "START GAME" BUTTON A MESSAGE IS SEND WHICH TELLS THE SERVER TO START THE GAME
    public void startGame(ActionEvent actionEvent) throws IOException {
        sendMessage("STARTTHEGAME");
        iPressed = true;
    }

    //METHODS FOR THE QUIZ ANSWER OPTIONS, WHICH SENDS THE CHOSEN ANSWERS INT NR TO THE SERVER
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

    //ENABLES the KEYBOARD KEYS 1-4 TO BE USED TO ANSWER THE QUESTION
    public void keyboardClick(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.DIGIT1) {
            sendInt(1);
        } else if (event.getCode() == KeyCode.DIGIT2) {
            sendInt(2);
        } else if (event.getCode() == KeyCode.DIGIT3) {
            sendInt(3);
        } else if (event.getCode() == KeyCode.DIGIT4) {
            sendInt(4);
        }
    }

  //ON ACTION METHOD FOR THE "SEND IP" BUTTON, WHICH CHANGES THE SCENE AND JOINS THE SERVER WITH THE MATCHING IP
    public void SendIPBtn(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            ip = EnterIPtxt.getText();
            System.out.println(ip);
            scene0.setVisible(false);
            scene1.setVisible(true);
            joinServer = true;
        }
    }

    // ON ACTION METHOD FOR "LOCALHOST" BUTTON, WHICH SETS THE IP TO "LOCALHOST"
    public void SendLocalHostIp(ActionEvent actionEvent) {

        ip = "localhost";

        System.out.println("You connected through: " + ip);
        scene0.setVisible(false);
        scene1.setVisible(true);
        joinServer = true;
    }

//ON ACTION METHOD OF "JOIN" BUTTON, WHICH CHANGES THE SCENE AND SAVES THE USERNAME
    public void joinTheServerBtn(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            sendMessage(usernameInput.getText());
            scene1.setVisible(false);
            scene2.setVisible(true);
        }

    }

    //ENABLES THE ENTER KEYBOARD KEY TO BE USED TO SEND WRITTEN USER INPUTS
    public void sendMessageButtonEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            sendMessage(chatMessage.getText());
            chatMessage.clear();
        }
    }

    //ON ACTION METHOD FOR THE "EXIT" BUTTON
    public void ExitApp(ActionEvent actionEvent) {
        System.exit(0);
    }
}




