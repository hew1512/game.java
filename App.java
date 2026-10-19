import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

  private static final int MAX_CHANCES = 10;

  private Label matchLabel;
  private Label chanceLabel;
  private Card firstCard = null;
  private boolean busy = false;
  private int matches = 0;
  private int chances = 0;

  @Override
  public void start(Stage primaryStage) {
    List<String> values = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      values.add(String.valueOf(i));
      values.add(String.valueOf(i));
    }
    Collections.shuffle(values);

    GridPane grid = new GridPane();
    grid.setHgap(15);
    grid.setVgap(15);
    grid.setAlignment(Pos.CENTER);

    int index = 0;
    for (int row = 0; row < 4; row++) {
      for (int col = 0; col < 5; col++) {
        Card card = new Card(values.get(index++));
        card.setOnMouseClicked(e -> handleCardClick(card));
        grid.add(card, col, row);
      }
    }

    matchLabel = new Label("Match : 0");
    matchLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
    chanceLabel = new Label("Chance : 0 / " + MAX_CHANCES);
    chanceLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

    HBox status = new HBox(40, matchLabel, chanceLabel);
    status.setAlignment(Pos.CENTER);

    VBox root = new VBox(25, grid, status);
    root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(20));

    primaryStage.setTitle("Memory Game");
    primaryStage.setScene(new Scene(root, 600, 490));
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  private void handleCardClick(Card card) {
    if (busy || !card.isFlippable() || card.isFaceUp()) {
      return;
    }

    card.flip();

    if (firstCard == null) {
      firstCard = card;
      return;
    }

    Card previous = firstCard;
    firstCard = null;
    chances++;

    if (previous.getValue().equals(card.getValue())) {
      previous.setFlippable(false);
      card.setFlippable(false);
      matches++;
      updateLabels();
      checkGameOver();
    } else {
      updateLabels();
      busy = true;
      PauseTransition pause = new PauseTransition(Duration.seconds(1));
      pause.setOnFinished(e -> {
        previous.flip();
        card.flip();
        busy = false;
        checkGameOver();
      });
      pause.play();
    }
  }

  private void updateLabels() {
    matchLabel.setText("Match : " + matches);
    chanceLabel.setText("Chance : " + chances + " / " + MAX_CHANCES);
  }

  private void checkGameOver() {
    if (matches == 10) {
      matchLabel.setText("Match : 10 - You win!");
      busy = true;
    } else if (chances >= MAX_CHANCES) {
      chanceLabel.setText("Chance : " + chances + " / " + MAX_CHANCES + " - Game over");
      busy = true;
    }
  }

  private static class Card extends StackPane {
    private final Rectangle background;
    private final Text valueLabel;
    private final String value;
    private boolean faceUp = false;
    private boolean flippable = true;

    Card(String value) {
      this.value = value;
      background = new Rectangle(62, 73, Color.DODGERBLUE);
      background.setArcWidth(5);
      background.setArcHeight(5);
      background.setStroke(Color.BLACK);
      valueLabel = new Text(value);
      valueLabel.setFont(Font.font(24));
      valueLabel.setVisible(false);
      getChildren().addAll(background, valueLabel);
    }

    void flip() {
      faceUp = !faceUp;
      valueLabel.setVisible(faceUp);
      background.setFill(faceUp ? Color.LIGHTGRAY : Color.DODGERBLUE);
    }

    String getValue() { return value; }
    boolean isFaceUp() { return faceUp; }
    boolean isFlippable() { return flippable; }
    void setFlippable(boolean flippable) { this.flippable = flippable; }
  }

  public static void main(String[] args) {
    launch(args);
  }
}