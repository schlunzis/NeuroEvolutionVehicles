package org.schlunzis.neuroevolution.view.drawer;

import com.jfoenix.controls.JFXToggleNode;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.schlunzis.neuroevolution.model.track.TrackFactory;
import org.schlunzis.neuroevolution.sdk.track.Track;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TrackCard extends Pane {

    private final Label header;
    private final JFXToggleNode perlinTrackToggle;
    private final Label perlinTrackLabel;
    private final JFXToggleNode convexHullToggle;
    private final Label convexHullLabel;
    private final JFXToggleNode partTrackToggle;
    private final Label partTrackLabel;

    private final List<JFXToggleNode> customTrackToggles;

    public TrackCard() {
        header = new Label();

        perlinTrackToggle = new JFXToggleNode();
        perlinTrackLabel = new Label();
        perlinTrackToggle.setGraphic(perlinTrackLabel);
        convexHullToggle = new JFXToggleNode();
        convexHullLabel = new Label();
        convexHullToggle.setGraphic(convexHullLabel);
        partTrackToggle = new JFXToggleNode();
        partTrackLabel = new Label();
        partTrackToggle.setGraphic(partTrackLabel);

        var vBox = new VBox(header);
        vBox.getChildren().addAll(perlinTrackToggle, convexHullToggle, partTrackToggle);

        customTrackToggles = new ArrayList<>();
        for (Track t : TrackFactory.getCustomTracks()) {
            JFXToggleNode toggle = new JFXToggleNode();
            Label label = new Label(t.getTrackName());
            toggle.setGraphic(label);
            customTrackToggles.add(toggle);
        }
        vBox.getChildren().addAll(customTrackToggles);


        getChildren().add(vBox);
    }
}
