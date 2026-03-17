package org.schlunzis.neuroevolution.view.drawer;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class LanguageCard extends Pane {

    private final Label header;

    private final Button englishButton;
    private final Button germanButton;

    public LanguageCard() {
        header = new Label();

        englishButton = new JFXButton();
        germanButton = new JFXButton();

        var vBox = new VBox(header, englishButton, germanButton);
        getChildren().add(vBox);
    }
}
