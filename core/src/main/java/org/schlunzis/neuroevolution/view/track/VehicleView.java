package org.schlunzis.neuroevolution.view.track;

import lombok.Getter;
import org.freedesktop.cairo.Context;
import org.gnome.gdk.Texture;
import org.gnome.graphene.Point;
import org.gnome.graphene.Rect;
import org.gnome.graphene.Size;
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

    private static Texture carTexture;

    static {
        carTexture = Texture.fromResource("/icons/scalable/actions/car-top-view.svg");
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
        cr.setSourceRGB(1, 0, 0);
        cr.rectangle(0, 0, height * 2, width * 2);
        cr.stroke();
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
        snapshot.rotate((float) angleDeg + 90); // +90 because the car texture is oriented upwards
        snapshot.appendTexture(carTexture, new Rect(new Point(-h, 0), new Size(2 * h, 2 * w)));
        snapshot.translate(new Point(-w / 2f, -h / 2f));

        // draw content (image / child / custom rendering)
        super.snapshot(snapshot);
    }

}
