package org.schlunzis.neuroevolution.view.drawer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.schlunzis.neuroevolution.controller.DrawerController;

@Getter
public class Drawer extends JFXDrawer {

    private final StackPane pane;

    private final Button drawerPaneTrackButton;
    private final Button drawerPaneGeneticAlgorithmButton;
    private final Button drawerPaneLanguageButton;
    private final Button drawerDoneButton;

    private final TrackCard trackCard;
    private final GeneticAlgorithmCard geneticAlgorithmCard;
    private final LanguageCard languageCard;

    private final DrawerController drawerController;

    private final Pane cardsPane;

    public Drawer() {
        pane = new StackPane();

        drawerPaneTrackButton = new JFXButton();
        drawerPaneGeneticAlgorithmButton = new JFXButton();
        drawerPaneLanguageButton = new JFXButton();
        drawerDoneButton = new JFXButton();

        VBox buttonPane = new VBox();
        buttonPane.getChildren().add(drawerPaneGeneticAlgorithmButton);
        buttonPane.getChildren().add(drawerPaneTrackButton);
        buttonPane.getChildren().add(drawerPaneLanguageButton);
        buttonPane.getChildren().add(drawerDoneButton);

        cardsPane = new StackPane();

        geneticAlgorithmCard = new GeneticAlgorithmCard();
        trackCard = new TrackCard();
        languageCard = new LanguageCard();

        cardsPane.getChildren().add(geneticAlgorithmCard);

        HBox drawerContent = new HBox(buttonPane, cardsPane);
        pane.getChildren().addAll(drawerContent);

        setSidePane(pane);
        setDefaultDrawerSize(200);
        setResizeContent(true);
        setOverLayVisible(false);
        setResizableOnDrag(true);

        drawerController = new DrawerController(this);
    }
}
