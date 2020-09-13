/*
 * Copyright (c) 2020 Mastercard. All rights reserved.
 */

package dolchlist.dto;

import javafx.scene.control.Button;
import lombok.Data;

@Data
public class ButtonElement {

    private Button button;
    private boolean isCorrect = false;
    private DolchListElement dolchListElement;

    public ButtonElement(Button button) {
        this.button = button;
    }

}
