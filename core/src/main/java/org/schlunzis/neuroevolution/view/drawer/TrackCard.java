package org.schlunzis.neuroevolution.view.drawer;

import java.util.ArrayList;
import java.util.List;

import org.schlunzis.neuroevolution.model.track.TrackFactory;
import org.schlunzis.neuroevolution.sdk.track.Track;
import com.jfoenix.controls.JFXToggleNode;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class TrackCard extends Pane {

    private Label header;
    private JFXToggleNode perlinTrackToggle;
    private Label perlinTrackLabel;
    private JFXToggleNode convexHullToggle;
    private Label convexHullLabel;
    private JFXToggleNode partTrackToggle;
    private Label partTrackLabel;

    private List<JFXToggleNode> customTrackToggles;

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
        for(Track t : TrackFactory.getCustomTracks()) {
            JFXToggleNode toggle = new JFXToggleNode();
            Label label = new Label(t.getTrackName());
            toggle.setGraphic(label);
            customTrackToggles.add(toggle);
        }
        vBox.getChildren().addAll(customTrackToggles);


        getChildren().add(vBox);
    }
}
