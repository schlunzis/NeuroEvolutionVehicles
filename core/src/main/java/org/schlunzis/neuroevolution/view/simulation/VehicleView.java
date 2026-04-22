package org.schlunzis.neuroevolution.view.simulation;

import lombok.Getter;
import lombok.Setter;
import org.freedesktop.cairo.Context;
import org.gnome.gdk.Texture;
import org.gnome.graphene.Point;
import org.gnome.graphene.Rect;
import org.gnome.graphene.Size;
import org.gnome.gtk.DrawingArea;
import org.gnome.gtk.Snapshot;
import org.javagi.gobject.annotations.InstanceInit;
import org.schlunzis.neuroevolution.model.Vehicle;

public class VehicleView extends DrawingArea {

    private static final Texture carTexture;

    static {
        carTexture = Texture.fromResource("/org/schlunzis/neuroevolution/images/car-top-view.svg");
    }

    @Getter
    private final Vehicle vehicle;
    private double angleDeg;
    @Setter
    private boolean highlight = false;

    public VehicleView(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @InstanceInit
    public void init() {
        this.setDrawFunc(this::draw);
    }

    private void draw(DrawingArea drawingArea, Context cr, int width, int height) {
        if (highlight) {
            cr.setSourceRGB(1, 1, 0);
        } else {
            cr.setSourceRGB(1, 0, 0);
        }
//        cr.rectangle(0, 0, width, height);
        cr.stroke();
    }

    public boolean isFinished() {
        return false; // vehicle.isDead();
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
        snapshot.rotate((float) angleDeg + 90);
        snapshot.appendTexture(carTexture, new Rect(new Point(-w / 2f, -h / 2f), new Size(w, h)));
        snapshot.translate(new Point(-w / 2f, -h / 2f));

        // draw content (image / child / custom rendering)
        super.snapshot(snapshot);
    }

}
