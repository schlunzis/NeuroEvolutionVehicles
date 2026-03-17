package org.schlunzis.neuroevolution.controller;

import org.schlunzis.neuroevolution.util.I18nUtils;
import org.schlunzis.neuroevolution.view.MainStage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Tooltip;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageController {

    public LanguageController(MainStage stage) {

        stage.getDrawer().getLanguageCard().getEnglishButton()
                .setOnAction(e -> I18nUtils.setBundle(getBundle(Locale.UK)));
        stage.getDrawer().getLanguageCard().getGermanButton()
                .setOnAction(e -> I18nUtils.setBundle(getBundle(Locale.GERMANY)));

        // mainStage
        stage.titleProperty().bind(createBinding("main.title"));

        // drawer
        stage.getDrawer().getDrawerPaneTrackButton().textProperty().bind(createBinding("drawer.track"));
        stage.getDrawer().getDrawerPaneGeneticAlgorithmButton().textProperty()
                .bind(createBinding("drawer.geneticAlgorithm"));
        stage.getDrawer().getDrawerPaneLanguageButton().textProperty().bind(createBinding("drawer.language"));
        stage.getDrawer().getDrawerDoneButton().textProperty().bind(createBinding("drawer.done"));

        // cards
        stage.getDrawer().getTrackCard().getHeader().textProperty().bind(createBinding("drawer.card.track.header"));
        stage.getDrawer().getTrackCard().getPerlinTrackLabel().textProperty().bind(createBinding("drawer.card.track.perlin"));
        stage.getDrawer().getTrackCard().getConvexHullLabel().textProperty().bind(createBinding("drawer.card.track.convexHull"));
        stage.getDrawer().getTrackCard().getPartTrackLabel().textProperty().bind(createBinding("drawer.card.track.partTrack"));

        stage.getDrawer().getGeneticAlgorithmCard().getHeader().textProperty()
                .bind(createBinding("drawer.card.geneticAlgorithm.header"));
        stage.getDrawer().getLanguageCard().getHeader().textProperty()
                .bind(createBinding("drawer.card.language.header"));
        stage.getDrawer().getLanguageCard().getEnglishButton().textProperty()
                .bind(createBinding("drawer.card.language.english"));
        stage.getDrawer().getLanguageCard().getGermanButton().textProperty()
                .bind(createBinding("drawer.card.language.german"));

        // toolbar
        stage.getResetButtonToolbar().setTooltip(createTooltip("toolbar.action.reset.tooltip"));
        stage.getStartButtonToolbar().setTooltip(createTooltip("toolbar.action.start.tooltip"));
        stage.getPauseButtonToolbar().setTooltip(createTooltip("toolbar.action.pause.tooltip"));
    }

    /**
     * Helper to crate a new String Binding for the provided key.
     *
     * @param key the key to be mapped on the resources
     * @return a binding for the provided key
     */
    private StringBinding createBinding(String key) {
        return Bindings.createStringBinding(() -> I18nUtils.i18n(key), I18nUtils.bundleProperty());
    }

    /**
     * Helper to create a new Tooltip with a text-binding to the provided key.
     *
     * @param key the key to map the text to
     * @return a Tooltip with text-binding
     */
    private Tooltip createTooltip(String key) {
        Tooltip tt = new Tooltip();
        tt.textProperty().bind(createBinding(key));
        return tt;
    }

    private static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("lang.messages", locale);
    }
}
