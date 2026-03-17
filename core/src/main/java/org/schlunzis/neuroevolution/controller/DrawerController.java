package org.schlunzis.neuroevolution.controller;

import org.schlunzis.neuroevolution.view.drawer.Drawer;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DrawerController {
    
    public DrawerController(Drawer drawer) {
        Pane cardsPane = drawer.getCardsPane();
        addSwitchPaneAction(drawer.getDrawerPaneTrackButton(), cardsPane, drawer.getTrackCard());
		addSwitchPaneAction(drawer.getDrawerPaneGeneticAlgorithmButton(), cardsPane, drawer.getGeneticAlgorithmCard());
		addSwitchPaneAction(drawer.getDrawerPaneLanguageButton(), cardsPane, drawer.getLanguageCard());


        drawer.getDrawerDoneButton().addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			log.debug("Closing drawer");
			drawer.close();
		});
    }

    private void addSwitchPaneAction(Button button, Pane parent, Pane pane) {
		button.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			parent.getChildren().clear();
			parent.getChildren().add(pane);
		});
	}

}
