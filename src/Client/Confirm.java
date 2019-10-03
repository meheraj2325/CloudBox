package Client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Confirm{
    static boolean flag;

    public static boolean display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        window.setMinWidth(320);
        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            flag = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            flag = false;
            window.close();
        });

        VBox vboxlayout = new VBox(10);
        vboxlayout.setPadding(new Insets(7, 7, 7, 7));

        vboxlayout.getChildren().addAll(label, yesButton, noButton);
        vboxlayout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vboxlayout);
        window.setScene(scene);
        window.showAndWait();

        return flag;
    }
}
