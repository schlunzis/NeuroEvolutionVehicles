package org.schlunzis.neuroevolution.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainController {

    @FXML
    private TabPane tabPane;

    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((_, oldTab, newTab) -> {
            log.debug("Changed from {} to {}", oldTab.getText(), newTab.getText());
        });
    }
}
