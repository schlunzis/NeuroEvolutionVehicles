package org.schlunzis.neuroevolution.view.simulation;

import lombok.Getter;
import lombok.Setter;
import org.freedesktop.cairo.Context;
import org.gnome.gtk.DrawingArea;
import org.javagi.gobject.annotations.InstanceInit;
import org.schlunzis.neuroevolution.model.World;
import org.schlunzis.neuroevolution.sdk.track.Track;
import org.schlunzis.neuroevolution.sdk.util.Boundary;
import org.schlunzis.neuroevolution.sdk.util.SVector;

import java.lang.foreign.MemorySegment;

public class TrackView extends DrawingArea {

    @Getter
    @Setter
    private World world;

    public TrackView(MemorySegment address) {
        super(address);
    }

    @InstanceInit
    public void init() {
        this.setDrawFunc(this::draw);
        this.onResize((_, _) -> {
            this.queueDraw();
        });
    }

    private void draw(DrawingArea drawingArea, Context cr, int width, int height) {
        // background
        cr.setSourceRGB(0, 0.5, 0);
        cr.paint();

        cr.setSourceRGB(0, 0, 1);

        Track track = world.getTrack();
        cr.setSourceRGB(0, 0, 0);
        for (Boundary wall : track.getWalls()) {
            drawBoundary(cr, wall, width, height);
        }

        cr.setSourceRGB(1, 0, 0);
        for (Boundary wall : track.getCheckpoints()) {
            drawBoundary(cr, wall, width, height);
        }
    }

    private void drawBoundary(Context cr, Boundary boundary, int width, int height) {
        SVector a = boundary.getA();
        SVector b = boundary.getB();
        cr.moveTo(a.x() * width, a.y() * height);
        cr.lineTo(b.x() * width, b.y() * height);
        cr.stroke();
    }
}
