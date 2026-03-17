package org.schlunzis.neuroevolution.model.track;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import org.schlunzis.neuroevolution.sdk.track.Track;
import org.schlunzis.neuroevolution.sdk.util.Boundary;
import org.schlunzis.neuroevolution.sdk.util.PVector;

import java.util.ArrayList;
import java.util.List;

public class ConvexHullTrack implements Track {

    private static final int CHECKPOINT_DIVIDER = 90;
    private static final double HALF_TRACK_WIDTH = 3 / 40d;

    @Getter
    private PVector start;

    @Getter
    private List<Boundary> walls;
    @Getter
    private List<Boundary> checkpoints;

    public String getTrackName() {
        return "ConvexHullTrack";
    }

    /**
     * <a href=
     * "https://www.gamedeveloper.com/programming/generating-procedural-racetracks">gamedeveloper.com</a>
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void buildTrack() {
        int numRandomPoints = 50;

        ArrayList<PVector> pts = new ArrayList<>();
        checkpoints = new ArrayList<>();
        double buffer = HALF_TRACK_WIDTH * 1.5;
        for (int i = 0; i < numRandomPoints; i++) {
            double x = buffer + Math.random() * (1 - buffer * 2);
            double y = buffer + Math.random() * (1 - buffer * 2);
            pts.add(new PVector(x, y));
        }
        ArrayList<PVector> hull = getConvexHull(pts);

        int pushIterations = 3;
        for (int i = 0; i < pushIterations; i++) {
            pushApart(hull);
        }

        // adding circuit details
        ArrayList<PVector> rSet = new ArrayList<>();
        PVector disp = new PVector();
        double difficulty = 1f / 20f; // the closer to the, the harder the track
        double maxDisp = 20 / 400d;
        for (int i = 0; i < hull.size(); i++) {
            double dispLen = Math.pow(Math.random(), difficulty) * maxDisp;
            disp.set(0, 1);
            disp.rotate(Math.random() * 2 * Math.PI);
            disp.setMag(dispLen);
            PVector p1 = hull.get(i);
            PVector p2 = hull.get((i + 1) % hull.size());
            PVector mid = PVector.add(p1, p2).mult(0.5);
            mid.add(disp);
            rSet.add(p1);
            rSet.add(mid);
        }
        hull = rSet;

        for (int i = 0; i < pushIterations; i++) {
            pushApart(hull);
        }

        hull = smoothTrack(hull);

        ArrayList<PVector> ptsInner = new ArrayList<>();
        ArrayList<PVector> ptsOuter = new ArrayList<>();
        PVector center = new PVector(0.5, 0.5);
        for (int i = 0; i < hull.size(); i++) {
            PVector p = hull.get(i);
            PVector v = PVector.sub(p, center);
            v.limit(HALF_TRACK_WIDTH);
            PVector v2 = v.copy();
            v2.rotate(Math.PI);
            PVector outer = PVector.add(v2, p);
            PVector inner = PVector.add(v, p);
            ptsInner.add(inner);
            ptsOuter.add(outer);
            if (i % CHECKPOINT_DIVIDER == 0)
                checkpoints.add(new Boundary(inner.x, inner.y, outer.x, outer.y));
        }
        start = checkpoints.getFirst().midPoint();

        walls = new ArrayList<>();
        walls.addAll(Boundary.createBoundaries(ptsInner, true));
        walls.addAll(Boundary.createBoundaries(ptsOuter, true));
    }

    private ArrayList<PVector> getConvexHull(ArrayList<PVector> points) {
        points.sort((p1, p2) -> p1.x < p2.x ? -1 : 1);
        ArrayList<PVector> hull = new ArrayList<>();
        PVector leftMost = points.getFirst();
        PVector currentVertex = leftMost;
        hull.add(currentVertex);
        PVector nextVertex = points.get(1);
        int index = 2;
        boolean finished = false;
        while (!finished) {
            PVector checking = points.get(index);
            PVector a = PVector.sub(nextVertex, currentVertex);
            PVector b = PVector.sub(checking, currentVertex);
            PVector cross = a.cross(b);
            if (cross.z < 0) {
                nextVertex = checking;
            }
            index++;

            if (index == points.size()) {
                if (nextVertex == leftMost)
                    finished = true;
                else {
                    hull.add(nextVertex);
                    currentVertex = nextVertex;
                    index = 0;
                    nextVertex = leftMost;
                }
            }
        }
        return hull;
    }

    private ArrayList<PVector> smoothTrack(ArrayList<PVector> dataSet) {
        Vector2[] data = new Vector2[dataSet.size()];
        for (PVector p : dataSet) {
            data[dataSet.indexOf(p)] = new Vector2((float) p.x, (float) p.y);
        }
        float step = 1 / 4000f;
        ArrayList<PVector> smoothed = new ArrayList<>();
        for (float t = 0; t <= 1.0f; ) {
            Vector2 p = new Vector2();
            // TODO: use other library to smooth the track
            p = CatmullRomSpline.calculate(p, t, data, true, new Vector2());
            Vector2 deriv = new Vector2();
            deriv = CatmullRomSpline.derivative(deriv, t, data, true, new Vector2());
            float len = deriv.len();
            deriv.scl(1f / len);
            deriv.scl(1 / 400f);
            deriv.set(-deriv.y, deriv.x);
            Vector2 v1 = new Vector2();
            v1.set(p).add(deriv);
            smoothed.add(new PVector(v1.x, v1.y));
            Vector2 v2 = new Vector2();
            v2.set(p).sub(deriv);
            smoothed.add(new PVector(v2.x, v2.y));
            t += step / len;
        }
        return smoothed;
    }

    private void pushApart(ArrayList<PVector> points) {
        double dst = 15 / 400d;
        double dst2 = dst * dst;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                if (dst2(points.get(i), points.get(j)) < dst2) {
                    double hx = points.get(j).x - points.get(i).x;
                    double hy = points.get(j).y - points.get(i).y;
                    double hl = Math.sqrt(hx * hx + hy * hy);
                    hx /= hl;
                    hy /= hl;
                    double dif = dst - hl;
                    hx *= dif;
                    hy *= dif;
                    points.get(j).x += hx;
                    points.get(j).y += hy;
                    points.get(i).x -= hx;
                    points.get(i).y -= hy;
                }
            }
        }
    }

    private double dst2(PVector p1, PVector p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }

    @Override
    public PVector getStartVelocity() {
        Boundary boundary = checkpoints.getFirst();
        PVector v = PVector.sub(boundary.getA(), boundary.getB());
        v.rotate(Math.PI * 0.5);
        v.normalize();
        return v;
    }

}
