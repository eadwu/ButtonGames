package ButtonGames;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameCode extends Application {
    @FXML
    public void buttonPressed(ActionEvent actionEvent) {
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 300, 275);

        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);

        for (int i = 0; i < Constants.NUMBER_OF_BUTTONS; i++) {
            Button button = new Button(String.valueOf(i));

            button.getStyleClass().add("fake_lights");
            root.add(button, i, 0);
        }

        Button initGame = new Button("Start Game");
        initGame.getStyleClass().add("init");
        root.addRow(1, initGame);

        scene.getStylesheets().add(this.getClass().getResource("display.css").toExternalForm());
        primaryStage.setTitle("Simon Says");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
