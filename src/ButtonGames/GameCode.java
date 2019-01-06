package ButtonGames;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameCode extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(10);

        for (int i = 0; i < Constants.NUMBER_OF_BUTTONS; i++) {
            Button button = new Button(String.valueOf(i));

            button.setDisable(true);
            button.getStyleClass().add("fake_lights");
            button.getProperties().put("data-button-id", i);
            buttonContainer.getChildren().add(button);

            button.setOnAction(e -> {
                Game.registerClick(button);
            });
        }

        HBox controlContainer = new HBox();
        controlContainer.setAlignment(Pos.CENTER);
        controlContainer.setSpacing(10);

        TextField userTextField = new TextField();
        userTextField.setPromptText("Enter your username: " + Constants.USERNAME);

        Button initGame = new Button("Start Game");
        initGame.getStyleClass().add("init");
        initGame.setOnAction(e -> {
            initGame.setDisable(true);
            userTextField.setEditable(false);

            if (userTextField.getText().equals("")) {
                userTextField.setText("null");
            }

            Game.init(userTextField.getText(), buttonContainer, root);
        });

        controlContainer.getChildren().addAll(initGame, userTextField);
        root.getChildren().addAll(buttonContainer, controlContainer);

        Scene scene = new Scene(root, 300, 275);
        scene.getStylesheets().add(this.getClass().getResource("display.css").toExternalForm());
        primaryStage.setTitle("Simon Says");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
