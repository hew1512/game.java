import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class Controller {

  private static final int MAX_CHANCES = 10;

  @FXML
  private GridPane cardGrid;
  @FXML
  private Label matchLabel;
  @FXML
  private Label chanceLabel;

  private Card firstCard = null;
  private boolean busy = false; // blocks clicks while two wrong cards are showing
  private int matches = 0;
  private int chances = 0;

  @FXML
  public void initialize() {

    List<String> values = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      values.add(String.valueOf(i));
      values.add(String.valueOf(i));
    }
    Collections.shuffle(values);

    int cols = 5;
    int rows = 4;
    int index = 0;

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Card card = new Card(values.get(index));
        index++;
        card.setOnMouseClicked(e -> handleCardClick(card));
        cardGrid.add(card, col, row);
      }
    }
  }

  private void handleCardClick(Card card) {
    if (busy || !card.isFilpable() || card.isFaceUp()) {
      return;
    }

    card.flip();

    if (firstCard == null) {
      firstCard = card;
      return;
    }

    Card secondCard = card;
    Card previous = firstCard;
    firstCard = null;
    chances++;

    if (previous.getValue().equals(secondCard.getValue())) {
      previous.setFlipable(false);
      secondCard.setFlipable(false);
      matches++;
      updateLabels();
      checkGameOver();
    } else {
      updateLabels();
      busy = true;
      PauseTransition pause = new PauseTransition(Duration.seconds(1));
      pause.setOnFinished(e -> {
        previous.flip();
        secondCard.flip();
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
}