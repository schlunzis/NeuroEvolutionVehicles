package org.schlunzis.neuroevolution.view.track;

import lombok.Getter;
import org.freedesktop.cairo.Context;
import org.gnome.gtk.DrawingArea;
import org.javagi.gobject.annotations.InstanceInit;
import org.schlunzis.neuroevolution.model.Vehicle;

public class VehicleView extends DrawingArea {

    @Getter
    private final Vehicle vehicle;

    public VehicleView(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @InstanceInit
    public void init() {
        this.setDrawFunc(this::draw);
        this.co
    }

    private void draw(DrawingArea drawingArea, Context cr, int width, int height) {
        cr.setSourceRGB(0, 0, 1);
        cr.paint();
    }

}
