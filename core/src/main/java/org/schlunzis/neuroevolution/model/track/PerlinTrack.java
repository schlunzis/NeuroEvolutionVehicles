package org.schlunzis.neuroevolution.model.track;

import lombok.Getter;
import org.schlunzis.neuroevolution.sdk.track.Track;
import org.schlunzis.neuroevolution.sdk.util.Boundary;
import org.schlunzis.neuroevolution.sdk.util.PVector;
import org.schlunzis.neuroevolution.util.OpenSimplexNoise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PerlinTrack implements Track {

    private static final int PARTS = 900;
    private static final int CHECKPOINT_DIVIDER = 30;
    private static final int NOISE_MAX = 8;
    private static final double HALF_TRACK_WIDTH = 3d / 40d;

    @Getter
    private PVector start;

    @Getter
    private List<Boundary> walls;
    @Getter
    private List<Boundary> checkpoints;

    public String getTrackName() {
        return "PerlinTrack";
    }

    @Override
    public void buildTrack() {
        ArrayList<PVector> ptsInner = new ArrayList<>();
        ArrayList<PVector> ptsOuter = new ArrayList<>();
        checkpoints = new ArrayList<>();
        OpenSimplexNoise noise = new OpenSimplexNoise(ThreadLocalRandom.current().nextLong());
        for (int i = 0; i < PARTS; i++) {
            double a = map(i, 0, PARTS, 0, 2 * Math.PI);
            double xoff = map(Math.cos(a), -1, 1, 0, NOISE_MAX);
            double yoff = map(Math.sin(a), -1, 1, 0, NOISE_MAX);
            double r = map(noise.eval(xoff, yoff), -1, 1, 0.25, 0.4);
            double innerX = Math.cos(a) * (r - HALF_TRACK_WIDTH) + 0.5;
            double innerY = Math.sin(a) * (r - HALF_TRACK_WIDTH) + 0.5;
            ptsInner.add(new PVector(innerX, innerY));

            double outerX = Math.cos(a) * (r + HALF_TRACK_WIDTH) + 0.5;
            double outerY = Math.sin(a) * (r + HALF_TRACK_WIDTH) + 0.5;
            ptsOuter.add(new PVector(outerX, outerY));
            if (i % CHECKPOINT_DIVIDER == 0)
                checkpoints.add(new Boundary(innerX, innerY, outerX, outerY));

        }
        walls = new ArrayList<>();
        walls.addAll(Boundary.createBoundaries(ptsInner, true));
        walls.addAll(Boundary.createBoundaries(ptsOuter, true));

        start = checkpoints.getFirst().midPoint();
    }

    @Override
    public PVector getStartVelocity() {
        Boundary boundary = checkpoints.getFirst();
        PVector v = PVector.sub(boundary.getA(), boundary.getB());
        v.rotate(-Math.PI * 0.5);
        v.normalize();
        return v;
    }

    /**
     * taken from Processing
     *
     * @param value
     * @param inputMin
     * @param inputMax
     * @param outputMin
     * @param outputMax
     * @return
     */
    private double map(double value, double inputMin, double inputMax, double outputMin, double outputMax) {
        return outputMin + (outputMax - outputMin) * ((value - inputMin) / (inputMax - inputMin));
    }

}
