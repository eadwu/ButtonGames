package ButtonGames;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {
    private static String username;
    private static HBox buttonContainer;
    private static VBox root;

    private static int score = 0;
    private static int buttonCount = Constants.BASE_PATTERN_COUNT;

    private static List<Integer> inputPattern = new ArrayList<>();
    private static List<Integer> realPattern = new ArrayList<>();

    public static void generatePattern(int length) {
        Game.realPattern.clear();
        IntStream
                .range(0, length)
                .forEach(i -> Game.realPattern.add((int) (Math.random() * Constants.NUMBER_OF_BUTTONS)));
    }

    public static void displayPattern() {
        new AnimationTimer() {
            private int count = 0;
            private long tick;

            @Override
            public void handle(long now) {
                // now - The timestamp of the current frame given in nanoseconds.
                // This value will be the same for all AnimationTimers called during one frame.
                if (this.tick == 0) this.tick = now;
                final long dt = now - tick;

                if (dt > 1 / Constants.FRAMES_PER_SECOND * Constants.NANO_CONVERSION_RATIO / 2) {
                    Game.buttonContainer.getChildren().forEach(button ->
                            button.setStyle("-fx-background-color: " + Constants.DARK_BG + ";"));
                }

                if (dt > 1 / Constants.FRAMES_PER_SECOND * Constants.NANO_CONVERSION_RATIO) {
                    // TODO: Remove polyfill logic
                    if (this.count >= Game.realPattern.size()) {
                        this.stop();
                        Game.buttonContainer.getChildren().forEach(button -> {
                            button.setStyle("-fx-background-color: " + Constants.DARK_BG + ";");
                            button.setDisable(false);
                        });
                    }

                    if (this.count < Game.realPattern.size()) {
                        Game.buttonContainer.getChildren().forEach(button -> {
                            Integer x = Integer.parseInt(button.getProperties().get("data-button-id").toString());
                            Integer y = Game.realPattern.get(count);

                            if (x.compareTo(y) == 0) {
                                button.setStyle("-fx-background-color: " + Constants.LIGHT_BG + ";");
                            } else {
                                button.setStyle("-fx-background-color: " + Constants.DARK_BG + ";");
                            }
                        });
                    }

                    this.tick = now;
                    this.count = this.count + 1;
                }
            }
        }.start();
    }

    public static void registerClick(Button button) {
        Game.inputPattern.add(Integer.parseInt(button.getProperties().get("data-button-id").toString()));

        if (Game.inputPattern.size() >= Game.realPattern.size()) Game.endRound();
    }

    public static void startRound() {
        Game.inputPattern.clear();
        Game.generatePattern(Game.buttonCount);
        Game.displayPattern();

        Game.buttonCount = Game.buttonCount + Constants.BASE_PATTERN_COUNT;
    }

    public static void endRound() {
        Game.buttonContainer.getChildren().forEach(button -> button.setDisable(true));

        if (Game.inputPattern.equals(Game.realPattern)) {
            score = score + Game.realPattern.size();
            Game.startRound();
        } else {
            Game.terminate();
        }
    }

    public static void init(final String username, HBox buttonContainer, VBox root) {
        Game.username = username;
        Game.buttonContainer = buttonContainer;
        Game.root = root;

        Game.startRound();
    }

    public static void terminate() {
        try {
            Backend.write(new ArrayList<>(Arrays.asList(Game.username, String.valueOf(Game.score))));
        } catch (IOException err) {
            err.printStackTrace();
        }

        GridPane highScores = new GridPane();
        highScores.setHgap(10);
        highScores.setVgap(10);
        highScores.setAlignment(Pos.CENTER);

        // TODO: Could be fetched through CSVUtilities
        Label usernameHeader = new Label();
        usernameHeader.setText("Username");

        Label scoreHeader = new Label();
        scoreHeader.setText("Score");

        highScores.addRow(0, usernameHeader, scoreHeader);

        List<List<String>> scores = Backend.read()
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(s -> Integer.parseInt(s.get(1)))))
                .collect(Collectors.toList());

        IntStream.range(0, Constants.MAX_SCORE_LISTING).forEach(i -> {
            if (i < scores.size()) {
                List<String> user = scores.get(i);

                Label usernameLabel = new Label();
                usernameLabel.setText(user.get(0));

                Label scoreLabel = new Label();
                scoreLabel.setText(user.get(1));

                highScores.addRow(i + 1, usernameLabel, scoreLabel);
            }
        });
        Game.root.getChildren().add(highScores);
    }
}
