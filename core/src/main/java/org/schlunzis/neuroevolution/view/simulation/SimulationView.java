package org.schlunzis.neuroevolution.view.simulation;

import org.gnome.gtk.Box;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;
import org.schlunzis.neuroevolution.model.World;

import java.lang.foreign.MemorySegment;

@GtkTemplate(ui = "/org/schlunzis/neuroevolution/simulation-view.ui")
public class SimulationView extends Box {

    @GtkChild
    public TrackView trackView;

    @GtkChild
    public VehiclesView vehiclesView;

    public SimulationView(MemorySegment address) {
        super(address);
    }

    public void setWorld(World world) {
        trackView.setWorld(world);
        vehiclesView.setWorld(world);
    }

    public void update() {
        if (trackView != null)
            trackView.queueDraw();
        if (vehiclesView != null)
            vehiclesView.update();
    }

}
