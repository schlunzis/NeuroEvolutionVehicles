package org.schlunzis.neuroevolution.model.track;

import lombok.Getter;
import org.schlunzis.neuroevolution.sdk.track.Track;
import org.schlunzis.neuroevolution.sdk.util.Boundary;
import org.schlunzis.neuroevolution.sdk.util.SVector;
import org.schlunzis.neuroevolution.util.OpenSimplexNoise;

import java.util.ArrayList;
import java.util.List;

public class PerlinTrack implements Track {

    private int parts = 900;
    private int checkpointDivider = 30;
    private int noiseMax = 8;
    private double halfTrackWidth = 3d / 40d;

    @Getter
    private SVector start;

    @Getter
    private List<Boundary> walls;
    @Getter
    private List<Boundary> checkpoints;

    public String getTrackName() {
        return "PerlinTrack";
    }

    @Override
    public void buildTrack() {
        ArrayList<SVector> ptsInner = new ArrayList<>();
        ArrayList<SVector> ptsOuter = new ArrayList<>();
        checkpoints = new ArrayList<>();
        OpenSimplexNoise noise = new OpenSimplexNoise((long) (Math.random() * 1000000));
        for (int i = 0; i < parts; i++) {
            double a = map(i, 0, parts, 0, 2 * Math.PI);
            double xoff = map(Math.cos(a), -1, 1, 0, noiseMax);
            double yoff = map(Math.sin(a), -1, 1, 0, noiseMax);
            double r = map(noise.eval(xoff, yoff), -1, 1, 0.25, 0.4);
            double innerX = Math.cos(a) * (r - halfTrackWidth) + 0.5;
            double innerY = Math.sin(a) * (r - halfTrackWidth) + 0.5;
            ptsInner.add(new SVector(innerX, innerY));

            double outerX = Math.cos(a) * (r + halfTrackWidth) + 0.5;
            double outerY = Math.sin(a) * (r + halfTrackWidth) + 0.5;
            ptsOuter.add(new SVector(outerX, outerY));
            if (i % checkpointDivider == 0)
                checkpoints.add(new Boundary(innerX, innerY, outerX, outerY));

        }
        walls = new ArrayList<>();
        walls.addAll(Boundary.createBoundaries(ptsInner, true));
        walls.addAll(Boundary.createBoundaries(ptsOuter, true));

        start = checkpoints.getFirst().midPoint();
    }

    @Override
    public SVector getStartVelocity() {
        Boundary boundary = checkpoints.getFirst();
        return SVector.sub(boundary.getA(), boundary.getB())
                .rotate(-Math.PI * 0.5)
                .normalized();
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
