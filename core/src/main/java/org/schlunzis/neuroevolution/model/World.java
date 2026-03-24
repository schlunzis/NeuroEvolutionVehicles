package org.schlunzis.neuroevolution.model;

import lombok.Getter;
import org.schlunzis.neuroevolution.model.track.TrackFactory;
import org.schlunzis.neuroevolution.sdk.track.Track;

import java.util.ArrayList;
import java.util.List;

public class World {
    @Getter
    private final List<Car> cars = new ArrayList<>();

    @Getter
    private final Track track = TrackFactory.createTrack(TrackFactory.PART_TRACK);

    public World(int carCount) {
        track.buildTrack();
        for (int i = 0; i < carCount; i++) {
            cars.add(new Car(.5, .5));
        }
    }

    public void update() {
        for (Car car : cars) {
            car.update();
            car.clamp();
        }
    }

}
