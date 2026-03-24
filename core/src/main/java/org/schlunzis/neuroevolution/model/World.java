package org.schlunzis.neuroevolution.model;

import lombok.Getter;
import org.schlunzis.neuroevolution.model.track.TrackFactory;
import org.schlunzis.neuroevolution.sdk.track.Track;

@Getter
public class World {

    private final Track track = TrackFactory.createTrack(TrackFactory.CONVEX_HULL);
    private final GeneticAlgorithm ga = new GeneticAlgorithm();

    public World(int carCount) {
        track.buildTrack();
        ga.setTrack(track);
    }

    public void update() {
        ga.update();
    }

}
