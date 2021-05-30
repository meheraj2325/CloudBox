package Client;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class FileFoldersListViewer {

    static String directoryPath;

    public static void display(Socket client, String command, String username, Stage window, PrintWriter printWriterClientOutputStream , BufferedReader bufferedReaderClientInputStream){
        printWriterClientOutputStream.println(command);
        printWriterClientOutputStream.flush();
        String response;
        HBox hboxLayout = new HBox(10);
        hboxLayout.setPadding(new Insets(10,10,10,10));
        Button backButton = new Button("<- Back");
        backButton.setOnAction(event->{
            FileFoldersListViewer.display(client,"**Back**",username,window,printWriterClientOutputStream,bufferedReaderClientInputStream);
        });
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event->{
            FileFoldersListViewer.display(client,"**Refresh**",username,window,printWriterClientOutputStream,bufferedReaderClientInputStream);
        });
        Button uploadButton = new Button("Upload File");
        uploadButton.setOnAction(event->{
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            String selectedFilePath;
            if (selectedFile != null) {
                selectedFilePath = selectedFile.getAbsolutePath();
                String uploadPath = directoryPath + "/";
                uploadPath += selectedFile.getName();
                printWriterClientOutputStream.println("**Upload**");
                printWriterClientOutputStream.flush();
                printWriterClientOutputStream.println(uploadPath);
                printWriterClientOutputStream.flush();
                System.out.println("File selected: " + uploadPath);
                System.out.println("File selected: " + selectedFile.getName() + "  path : " + selectedFilePath);
                File file = new File(selectedFilePath);
                printWriterClientOutputStream.println(file.length());
                System.out.println("FIle length " + file.length());
                printWriterClientOutputStream.flush();
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(file);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                BufferedInputStream bufferInputStream = new BufferedInputStream(fileInputStream);
                try {
                    OutputStream outputStream = client.getOutputStream();
                    byte[] contents;
                    long fileLength = file.length();
                    long current = 0;

                    while(current<fileLength){
                        int size = 10000;
                        if(fileLength - current >= size)
                            current += size;
                        else{
                            size = (int)(fileLength - current);
                            current = fileLength;
                        }
                        contents = new byte[size];
                        bufferInputStream.read(contents, 0, size);
                        outputStream.write(contents);
                        outputStream.flush();
                        ///System.out.println("Sending file ... "+(current*100)/fileLength+"% complete!");
                    }
                    bufferInputStream.close();
                    fileInputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Alert.display("Notification","File uploaded successfully.");
                System.out.println("File sent successfully!");
            }
            else {
                System.out.println( "File selection cancelled.");
            }
        });

        Label tempLabel =new Label("                                        ");

        Label usernameLabel = new Label("User : " + username);
        usernameLabel.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD,20));
        usernameLabel.setTextFill(Color.DARKSLATEGREY);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e->window.setScene(GUI_Client.openingScene));

        hboxLayout.getChildren().addAll(backButton,refreshButton,uploadButton,tempLabel,usernameLabel,logoutButton);

        Label filesLabel = new Label("Files : ");
        filesLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD,15));
        filesLabel.setTextFill(Color.DARKSLATEGREY);
        Label foldersLabel = new Label("Folders : ");
        foldersLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD,15));
        foldersLabel.setTextFill(Color.DARKSLATEGREY);
        ListView<String> listViewOfFolders = new ListView<>();
        Button openFolderButton = new Button("Open Folder");
        Button createNewFolderButton = new Button("Create New Folder");

        openFolderButton.setOnAction(event->{
            String selectedItem = listViewOfFolders.getSelectionModel().getSelectedItem();
            if(selectedItem!=null){
                System.out.println(selectedItem);
                FileFoldersListViewer.display(client,selectedItem,username,window,printWriterClientOutputStream,bufferedReaderClientInputStream);
            }
        });

        createNewFolderButton.setOnAction(event -> {
                String folderName = Alert.getFolderName("Create New Folder!","Enter your new folder's name: ");
                if(folderName.length()>0){
                    String createFolderPath = directoryPath + "/";
                    createFolderPath += folderName;
                    printWriterClientOutputStream.println("**CreateFolder**");
                    printWriterClientOutputStream.flush();
                    printWriterClientOutputStream.println(createFolderPath);
                    printWriterClientOutputStream.flush();
                    System.out.println("New Folder Path: " + createFolderPath);

                }
        });

        HBox hboxLayout2 = new HBox(10);
        hboxLayout2.getChildren().addAll(openFolderButton,createNewFolderButton);

        Button downloadFileButton = new Button("Download File");
        ListView<String> listViewOfFiles = new ListView<>();
        downloadFileButton.setOnAction( event->{
            String selectedItem = listViewOfFiles.getSelectionModel().getSelectedItem();
            System.out.println(selectedItem);
            if(selectedItem!=null){
                printWriterClientOutputStream.println("**Download**");
                printWriterClientOutputStream.flush();
                String selectedItemInitialPath = directoryPath + "/";
                selectedItemInitialPath += selectedItem;
                printWriterClientOutputStream.println(selectedItemInitialPath);
                printWriterClientOutputStream.flush();

                long fileLength = 0;

                try {
                    fileLength = Long.parseLong(bufferedReaderClientInputStream.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte[] contents = new byte[10000];

                //Initialize the FileOutputStream to the output file's full path.
                try{
                    String downloadPath = "C:/Users/meher/Downloads/";
                    downloadPath+=selectedItem;
                    FileOutputStream fileOutputStream = new FileOutputStream(downloadPath);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    InputStream inputStream = client.getInputStream();

                    int bytesRead = 0;
                    long recieved = 0;

                    while((bytesRead = inputStream.read(contents))!=-1) {
                        bufferedOutputStream.write(contents, 0, bytesRead);
                        recieved+=bytesRead;
                        if(recieved>=fileLength) break;
                    }
                    bufferedOutputStream.flush();
                    fileOutputStream.close();
                    bufferedOutputStream.close();
                    System.out.println("File saved successfully!");
                    Alert.display("Notification","File downloaded successfully.");
                }catch(IOException er){
                    er.printStackTrace();
                }
            }

        });
        VBox vboxlayout = new VBox(10);
        vboxlayout.setPadding(new Insets(20, 20, 20, 20));
        vboxlayout.getChildren().add(hboxLayout);
        try{
            directoryPath = bufferedReaderClientInputStream.readLine();
            System.out.println(directoryPath);
            while(!(response = bufferedReaderClientInputStream.readLine()).equals("-->END<--")){
                StringTokenizer stringTokenizer = new StringTokenizer(response,"=");
                String firstToken = stringTokenizer.nextToken();
                if(firstToken.equals("Folder")){
                    String folderName = stringTokenizer.nextToken();
                    listViewOfFolders.getItems().add(folderName);
                }
                else if(firstToken.equals("File")){
                    String fileName = stringTokenizer.nextToken();
                    listViewOfFiles.getItems().add(fileName);
                }
                //System.out.println("From Server : "+ fileName);
            }
        }catch(IOException e1){
            e1.printStackTrace();
        }
        vboxlayout.getChildren().addAll(foldersLabel,listViewOfFolders,hboxLayout2,filesLabel,listViewOfFiles,downloadFileButton);
        Scene filesAndFolders = new Scene(vboxlayout,600,600);
        window.setScene(filesAndFolders);
    }

}
