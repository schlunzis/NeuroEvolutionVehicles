package org.schlunzis.neuroevolution.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXHamburger;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.neuroevolution.controller.*;
import org.schlunzis.neuroevolution.model.GeneticAlgorithm;
import org.schlunzis.neuroevolution.view.drawer.Drawer;

@Getter
@Slf4j
public class MainStage extends Stage {

    private final MainStageController mainStageController;
    private final SimulationController simulationController;

    private final LanguageController languageController;
    private final TrackCardController trackCardController;
    private final GeneticAlgorithmCardController geneticAlgorithmCardController;

    private MenuBar menuBar;
    private Menu settingsMenu;
    private MenuItem saveModel;
    private MenuItem loadModel;

    private ToolBar toolBar;

    private JFXHamburger hamburger;

    private Button resetButtonToolbar;
    private Button startButtonToolbar;
    private Button pauseButtonToolbar;

    // drawer
    private final JFXDrawersStack drawersStack;
    private Drawer drawer;

    private final InformationPanel informationPanel;
    private final MainPanel mainPanel;

    private final Scene mainStageScene;

    private final GeneticAlgorithm geneticAlgorithm;

    @Getter
    private static final Image startImage;
    @Getter
    private static final Image pauseImage;
    @Getter
    private static final Image stopImage;
    @Getter
    private static final Image resetImage;

    static {
        log.debug("Loading stage images");

        startImage = new Image("/img/Play24.gif");
        pauseImage = new Image("/img/Pause24.gif");
        stopImage = new Image("/img/Stop24.gif");
        resetImage = new Image("/img/Reset24.png");
    }

    public MainStage() {

        geneticAlgorithm = new GeneticAlgorithm();

        createMenubar();
        createToolbar();
        createDrawer();

        mainPanel = new MainPanel(geneticAlgorithm);
        StackPane holder = new StackPane(mainPanel);
        informationPanel = new InformationPanel(geneticAlgorithm);
        var vbox = new VBox(menuBar, toolBar, informationPanel, holder);

        drawersStack = new JFXDrawersStack();
        drawersStack.setContent(vbox);

        mainStageScene = new Scene(drawersStack);
        setScene(mainStageScene);

        mainPanel.paint();

        mainStageController = new MainStageController(this);
        simulationController = new SimulationController(this, geneticAlgorithm);

        languageController = new LanguageController(this);
        trackCardController = new TrackCardController(this);
        geneticAlgorithmCardController = new GeneticAlgorithmCardController(this);

        setResizable(false);
        show();
    }

    private void createMenubar() {
        log.debug("Creating Menubar");

        saveModel = new MenuItem("save Model");
        loadModel = new MenuItem("load Model");
        settingsMenu = new Menu("settings", null, saveModel, loadModel);

        menuBar = new MenuBar(settingsMenu);
    }

    private void createDrawer() {
        log.debug("Creating Drawer");

        drawer = new Drawer();
    }

    private void createToolbar() {
        log.debug("Creating Toolbar");

        hamburger = new JFXHamburger();

        resetButtonToolbar = new JFXButton(null, new ImageView(resetImage));
        startButtonToolbar = new JFXButton(null, new ImageView(startImage));
        pauseButtonToolbar = new JFXButton(null, new ImageView(pauseImage));
        toolBar = new ToolBar(hamburger, startButtonToolbar, pauseButtonToolbar, resetButtonToolbar);
    }

}
