
package dolchlist.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dolchlist.dto.ButtonElement;
import dolchlist.dto.DolchListElement;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class StageBuilder {

    Map<Button, ButtonElement> buttonMap = new HashMap();
    private Random randomGenerator = new Random();
    private ButtonElement correctButton;
	private boolean isExitEnabled = false;

    public void addButton(Button button) {
        buttonMap.put(button, new ButtonElement(button));
    }

    public void assign1CorrectWord(List<DolchListElement> element3words) {
        int indexForCorrect = randomGenerator.nextInt(buttonMap.size());
        int i = 0;
        for (Button b : buttonMap.keySet()) {
            DolchListElement word = element3words.get(i);
            ButtonElement buttonElement = buttonMap.get(b);
            buttonElement.getButton().setText(word.getWord());
            buttonElement.setDolchListElement(word);

            if (i == indexForCorrect) {
                buttonElement.setCorrect(true);
                correctButton = buttonElement;
            }
            i++;
        }
    }

    public ButtonElement getByButton(Button button) {
        return buttonMap.get(button);
    }

    public ButtonElement getCorrectButton() {
        return correctButton;
    }

    public void addLabel(Pane pane) {
        Label label1 = new Label("Find:  " + correctButton.getDolchListElement().getWord());
        label1.setLayoutX(200);
        label1.setLayoutY(50);
        label1.setStyle("-fx-font-size: 5em;  -fx-font-family: Verdana; ");
        pane.getChildren().add(label1);

    }
    

	public boolean isExitEnabled() {
		return isExitEnabled;
	}

	public void setExitEnabled(boolean isExitEnabled) {
		this.isExitEnabled = isExitEnabled;
	}
    
}
