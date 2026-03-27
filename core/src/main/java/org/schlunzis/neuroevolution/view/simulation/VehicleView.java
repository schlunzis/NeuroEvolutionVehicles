package org.schlunzis.neuroevolution.view.simulation;

import lombok.Getter;
import org.freedesktop.cairo.Context;
import org.gnome.graphene.Point;
import org.gnome.gtk.DrawingArea;
import org.gnome.gtk.GestureClick;
import org.gnome.gtk.Snapshot;
import org.javagi.gobject.annotations.InstanceInit;
import org.schlunzis.neuroevolution.model.Vehicle;

public class VehicleView extends DrawingArea {

    @Getter
    private final Vehicle vehicle;
    private double angleDeg;

    public VehicleView(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @InstanceInit
    public void init() {
        this.setDrawFunc(this::draw);
        GestureClick gesture = new GestureClick();
        gesture.onPressed((_, _, _) -> {
            System.out.println(vehicle.getId());
        });
        this.addController(gesture);
    }

    private void draw(DrawingArea drawingArea, Context cr, int width, int height) {
        cr.setSourceRGB(0, 0, 1);
        cr.paint();
    }


    public void setDirection(double angleDeg) {
        this.angleDeg = angleDeg;
        this.queueDraw();   // important!
    }

    @Override
    protected void snapshot(Snapshot snapshot) {

        int w = this.getWidth();
        int h = this.getHeight();

        snapshot.translate(new Point(w / 2f, h / 2f));
        snapshot.rotate((float) angleDeg);
        snapshot.translate(new Point(-w / 2f, -h / 2f));

        // draw content (image / child / custom rendering)
        super.snapshot(snapshot);
    }

}
