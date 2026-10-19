import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Card extends StackPane {

  private final Rectangle background;
  private final Text valueLabel;
  private final String value;
  private boolean isFaceUp = false;
  private boolean canFlip = true;

  public Card(String value) {
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

  public void flip() {
    isFaceUp = !isFaceUp;

    valueLabel.setVisible(isFaceUp);

    if (isFaceUp) {
      background.setFill(Color.LIGHTGRAY);
    } else {
      background.setFill(Color.DODGERBLUE);
    }
  }

  public String getValue() {
    return this.value;
  }

  public boolean isFilpable() {
    return this.canFlip;
  }

  public boolean isFaceUp() {
    return this.isFaceUp;
  }

  public void setFlipable(boolean canFlip) {
    this.canFlip = canFlip;
  }
}