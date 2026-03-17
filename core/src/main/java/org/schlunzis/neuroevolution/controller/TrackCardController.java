package org.schlunzis.neuroevolution.controller;

import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import org.schlunzis.neuroevolution.model.track.TrackFactory;
import org.schlunzis.neuroevolution.view.MainStage;

public class TrackCardController {

    public TrackCardController(MainStage mainStage) {
        var toggleGroup = new ToggleGroup();
        mainStage.getDrawer().getTrackCard().getPerlinTrackToggle().setOnAction(
                _ -> mainStage.getGeneticAlgorithm().setTrack(TrackFactory.createTrack(TrackFactory.PERLIN_NOISE)));
        mainStage.getDrawer().getTrackCard().getPerlinTrackToggle().setToggleGroup(toggleGroup);

        mainStage.getDrawer().getTrackCard().getConvexHullToggle().setOnAction(
                _ -> mainStage.getGeneticAlgorithm().setTrack(TrackFactory.createTrack(TrackFactory.CONVEX_HULL)));
        mainStage.getDrawer().getTrackCard().getConvexHullToggle().setToggleGroup(toggleGroup);
        mainStage.getDrawer().getTrackCard().getConvexHullToggle().setSelected(true);

        mainStage.getDrawer().getTrackCard().getPartTrackToggle().setOnAction(
                _ -> mainStage.getGeneticAlgorithm().setTrack(TrackFactory.createTrack(TrackFactory.PART_TRACK)));
        mainStage.getDrawer().getTrackCard().getPartTrackToggle().setToggleGroup(toggleGroup);

        mainStage.getDrawer().getTrackCard().getCustomTrackToggles().forEach(toggle -> {
            toggle.setOnAction(_ -> {
                String text = ((Label) toggle.getGraphic()).getText();
                mainStage.getGeneticAlgorithm().setTrack(TrackFactory.createTrack(text));
            });
            toggle.setToggleGroup(toggleGroup);
        });
    }
}
