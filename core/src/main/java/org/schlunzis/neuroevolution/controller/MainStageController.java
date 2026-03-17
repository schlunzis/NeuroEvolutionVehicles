package org.schlunzis.neuroevolution.controller;

import org.schlunzis.neuroevolution.view.MainStage;

import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainStageController {

	public MainStageController(MainStage mainStage) {
		mainStage.getResetButtonToolbar().setOnAction(e -> reset());
		mainStage.getSaveModel().setOnAction(e -> log.debug("Save current model"));
		mainStage.getLoadModel().setOnAction(e -> log.debug("load new model"));
	
		mainStage.getHamburger().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			log.debug("Toggle drawer");
			mainStage.getDrawersStack().toggle(mainStage.getDrawer());
		});

	}

	public void reset() {
		log.debug("Reset GA");
	}
}
