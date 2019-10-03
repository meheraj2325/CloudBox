package Client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class GUI_Client extends Application{

    Stage window;
    static Scene loginScene,registerScene,openingScene;

    static Socket client = null;
    static BufferedReader bufferedReaderClientInputStream = null;
    static PrintWriter printWriterClientOutputStream = null;
    static String directoryPath;

    public static void main(String []args) throws IOException{

        try {
            client=new Socket("localhost", 4444);
            bufferedReaderClientInputStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
            printWriterClientOutputStream = new PrintWriter(client.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }
        //System.out.println("Client Address : "+ );
        System.out.println("Enter Data to Server :");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("CloudBox");

        ///Final task if user click on the exit button.
        window.setOnCloseRequest(e->{
            e.consume();
            try {
                FinalWork();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Label cloudBoxlabel = new Label();
        cloudBoxlabel.setText("Welcome To CloudBox.");
        cloudBoxlabel.setTextFill(Color.CADETBLUE);
        cloudBoxlabel.setFont(Font.font("Times New Roman", FontWeight.BOLD,20));

        Label templabel = new Label();

        Button loginButtonOpeningScene = new Button("Login");
        loginButtonOpeningScene.setOnAction(e->window.setScene(loginScene));
        Label orLabel = new Label();
        orLabel.setText("or,");
        Button registerButtonOpeningScene = new Button("Register");
        registerButtonOpeningScene.setOnAction(e->window.setScene(registerScene));

        VBox vboxLayout = new VBox(10);
        vboxLayout.getChildren().addAll(cloudBoxlabel,templabel,loginButtonOpeningScene,orLabel,registerButtonOpeningScene);
        vboxLayout.setAlignment(Pos.CENTER);

        BorderPane borderPaneLayout = new BorderPane();
        borderPaneLayout.setCenter(vboxLayout);

        openingScene = new Scene(borderPaneLayout,300,300);

        GridPane gridPaneLayoutLoginScene = new GridPane();
        gridPaneLayoutLoginScene.setPadding(new Insets(10, 10, 10, 10));
        gridPaneLayoutLoginScene.setVgap(8);
        gridPaneLayoutLoginScene.setHgap(10);

        // Username label
        Label userNameLabelLoginScene = new Label("Username:");
        GridPane.setConstraints(userNameLabelLoginScene, 0, 0);

        //Username TextField
        TextField userNameTextFieldLoginScene = new TextField();
        userNameTextFieldLoginScene.setPromptText("Username");
        GridPane.setConstraints(userNameTextFieldLoginScene, 1, 0);

        //Password Label
        Label passwordLabelLoginScene = new Label("Password:");
        GridPane.setConstraints(passwordLabelLoginScene, 0, 1);

        //Password Input
        PasswordField passwordFieldLoginScene = new PasswordField();
        passwordFieldLoginScene.setPromptText("password");
        GridPane.setConstraints(passwordFieldLoginScene, 1, 1);

        //Login
        Button loginButtonLoginScene = new Button("Log In");
        loginButtonLoginScene.setOnAction(e->{
            boolean flag = false;
            String username = "";
            try {
                FileReader fileReader = new FileReader("Username.txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String test = userNameTextFieldLoginScene.getText();
                username = test;
                test+="+";
                test+=passwordFieldLoginScene.getText();
                String temp;
                while((temp=bufferedReader.readLine())!=null){
                    if(temp.equals(test)) flag= true;
                }
                bufferedReader.close();
                fileReader.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch(IOException e2){
                e2.printStackTrace();
            }
            if(!flag){
                Alert.display("Alert","Username and password did not match");
            }
            else{
                FileFoldersListViewer.display(client,"**Login**",username,window,printWriterClientOutputStream,bufferedReaderClientInputStream);
            }
        });

        Button backButtonLoginScene = new Button("Back");
        backButtonLoginScene.setOnAction(e->{
            window.setScene(openingScene);
        });

        HBox hboxlayout1 = new HBox(10);
        hboxlayout1.getChildren().addAll(loginButtonLoginScene,backButtonLoginScene);
        GridPane.setConstraints(hboxlayout1, 1, 2);

        gridPaneLayoutLoginScene.getChildren().addAll(userNameLabelLoginScene, userNameTextFieldLoginScene, passwordLabelLoginScene, passwordFieldLoginScene, hboxlayout1);

        loginScene = new Scene(gridPaneLayoutLoginScene, 300, 200);

        GridPane gridPaneLayoutRegisterScene = new GridPane();
        gridPaneLayoutRegisterScene.setPadding(new Insets(10, 10, 10, 10));
        gridPaneLayoutRegisterScene.setVgap(8);
        gridPaneLayoutRegisterScene.setHgap(10);

        //Name Label
        Label userNameLabelRegisterScene = new Label("Username:");
        GridPane.setConstraints(userNameLabelRegisterScene, 0, 0);

        //Email Input
        TextField userNameTextFieldRegisterScene = new TextField();
        userNameTextFieldRegisterScene.setPromptText("Enter your username");
        GridPane.setConstraints(userNameTextFieldRegisterScene, 1, 0);

        //Email Label
        Label emailLabelRegisterScene = new Label("Email: ");
        GridPane.setConstraints(emailLabelRegisterScene, 0, 1);

        //Name Input
        TextField emailTextFieldRegisterScene = new TextField();
        emailTextFieldRegisterScene.setPromptText("Enter your Email address");
        GridPane.setConstraints(emailTextFieldRegisterScene, 1, 1);

        //Password Label
        Label passwordLabelRegisterScene = new Label("Password:");
        GridPane.setConstraints(passwordLabelRegisterScene, 0, 2);

        //Password Input
        PasswordField passwordFieldRegisterScene = new PasswordField();
        passwordFieldRegisterScene.setPromptText("password");
        GridPane.setConstraints(passwordFieldRegisterScene, 1, 2);

        //Confirm Password Label
        Label passwordConfirmLabelRegisterScene = new Label("Confirm Password:");
        GridPane.setConstraints(passwordConfirmLabelRegisterScene, 0, 3);

        //Password Input
        PasswordField passwordFieldConfirmationRegisterScene = new PasswordField();
        passwordFieldConfirmationRegisterScene.setPromptText("password");
        GridPane.setConstraints(passwordFieldConfirmationRegisterScene, 1, 3);

        //Login
        Button registerButtonRegisterScene = new Button("Register");
        registerButtonRegisterScene.setOnAction(e->{
            try {
                FileWriter fileWriter1 = new FileWriter("Username.txt",true);
                BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter1);
                FileWriter fileWriter2 = new FileWriter("Users Info.txt",true);
                BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
                String userNameAndEmail = "";
                String nameAndPass = "";
                boolean testName = false, testEmail = false, testPass = false ,flag = false;
                if(userNameTextFieldRegisterScene.getText().length()<4){
                    Alert.display("Alert","You must enter your username of length at least 4.");
                }
                else{
                    String temp = userNameTextFieldRegisterScene.getText();
                    temp+= "+";
                    FileReader fileReader = new FileReader("Username.txt");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String tempp;
                    while((tempp=bufferedReader.readLine())!=null){
                        if(tempp.startsWith(temp)) flag = true;
                    }
                    bufferedReader.close();
                    fileReader.close();
                    if(flag){
                        Alert.display("Alert","Username is already in use.");
                    }
                    else{
                        testName = true;
                        userNameAndEmail = "Username : ";
                        userNameAndEmail +=userNameTextFieldRegisterScene.getText();
                        nameAndPass+=userNameTextFieldRegisterScene.getText();
                    }
                }
                if(emailTextFieldRegisterScene.getText().length()==0 || (!emailTextFieldRegisterScene.getText().endsWith("@gmail.com") && !emailTextFieldRegisterScene.getText().endsWith("@hotmail.com") && !emailTextFieldRegisterScene.getText().endsWith("@yahoo.com"))){
                    Alert.display("Alert","You must enter your valid E-mail address.");
                }
                else{
                    testEmail = true;
                    userNameAndEmail += "\n";
                    userNameAndEmail += "Email : ";
                    userNameAndEmail += emailTextFieldRegisterScene.getText();
                }
                if(passwordFieldRegisterScene.getText().length()<5){
                    Alert.display("Alert","You must enter your password of length at least 5.");
                }
                if(passwordFieldConfirmationRegisterScene.getText().length()<5){
                    Alert.display("Alert","You must confirm your password.");
                }
                else if(!passwordFieldConfirmationRegisterScene.getText().equals(passwordFieldRegisterScene.getText())){
                    Alert.display("Alert","Passwords are not matching.");
                }
                else{
                    testPass = true;
                    nameAndPass+="+";
                    nameAndPass+=passwordFieldConfirmationRegisterScene.getText();
                }
                if(testName && testEmail && testPass){
                    bufferedWriter2.write(userNameAndEmail);
                    bufferedWriter2.newLine();
                    bufferedWriter2.flush();
                    bufferedWriter1.write(nameAndPass);
                    bufferedWriter1.newLine();
                    bufferedWriter1.flush();
                    window.setScene(loginScene);
                }
                bufferedWriter1.close();
                bufferedWriter2.close();
                fileWriter1.close();
                fileWriter2.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Button backButtonRegisterScene = new Button("Back");
        backButtonRegisterScene.setOnAction(e->{
            window.setScene(openingScene);
        });

        HBox hboxlayout2 = new HBox(10);
        hboxlayout2.getChildren().addAll(registerButtonRegisterScene,backButtonRegisterScene);
        GridPane.setConstraints(hboxlayout2, 1, 4);
        //Add everything to grid
        gridPaneLayoutRegisterScene.getChildren().addAll(userNameLabelRegisterScene, userNameTextFieldRegisterScene, emailLabelRegisterScene,emailTextFieldRegisterScene, passwordLabelRegisterScene, passwordFieldRegisterScene, passwordConfirmLabelRegisterScene,passwordFieldConfirmationRegisterScene, hboxlayout2);

        registerScene = new Scene(gridPaneLayoutRegisterScene, 500, 320);

        window.setScene(openingScene);
        window.show();
    }

    public void FinalWork() throws IOException {
        boolean flag = Confirm.display("Exit?","Are you sure you want to exit?");
        if(flag){
            bufferedReaderClientInputStream.close();
            printWriterClientOutputStream.close();
            client.close();
            System.out.println("Connection Closed");
            window.close();
        }
    }
}

