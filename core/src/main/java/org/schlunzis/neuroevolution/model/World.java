package org.schlunzis.neuroevolution.model;

import lombok.Getter;
import org.schlunzis.neuroevolution.model.track.TrackFactory;
import org.schlunzis.neuroevolution.sdk.track.Track;

@Getter
public class World {

    private final Track track = TrackFactory.createTrack(TrackFactory.PART_TRACK);
    private final GeneticAlgorithm ga;

    public World() {
        track.buildTrack();
        ga = new GeneticAlgorithm(track);
    }

    public void update() {
        ga.update();
    }

}
