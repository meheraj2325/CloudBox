package Client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alert{
     static String folderName = "";

    public static void display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        window.setMinWidth(320);
        Label label = new Label();
        label.setText(message);

        Button okayButton = new Button("Okay");

        okayButton.setOnAction(e -> {
            window.close();
        });
        VBox vboxlayout = new VBox(10);
        vboxlayout.setPadding(new Insets(7, 7, 7, 7));

        vboxlayout.getChildren().addAll(label, okayButton);
        vboxlayout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vboxlayout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static String getFolderName(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        window.setMinWidth(320);
        Label label = new Label();
        label.setText(message);

        TextField folderNameTextField = new TextField();
        folderNameTextField.setPromptText("Folder name");

        Button okayButton = new Button("Okay");
        Button cancelButton = new Button("Cancel");

        okayButton.setOnAction(e -> {
            folderName = folderNameTextField.getText();
            window.close();
        });
        cancelButton.setOnAction(e -> {
            window.close();
        });
        HBox hboxLayout = new HBox(10);
        hboxLayout.getChildren().addAll(okayButton,cancelButton);

        VBox vboxlayout = new VBox(10);
        vboxlayout.setPadding(new Insets(7, 7, 7, 7));
        vboxlayout.getChildren().addAll(label,folderNameTextField,hboxLayout);
        vboxlayout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vboxlayout);
        window.setScene(scene);
        window.showAndWait();
        return folderName;
    }
}

