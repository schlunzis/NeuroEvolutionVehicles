package org.schlunzis.neuroevolution.view.simulation;

import lombok.Getter;
import org.gnome.gtk.Box;
import org.gnome.gtk.Overlay;
import org.javagi.gobject.annotations.InstanceInit;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;
import org.schlunzis.neuroevolution.model.World;

import java.lang.foreign.MemorySegment;

@GtkTemplate(ui = "/org/schlunzis/neuroevolution/simulation-view.ui")
public class SimulationView extends Box {

    @GtkChild
    public TrackView trackView;

    @Getter
    @GtkChild
    public VehiclesView vehiclesView;

    @GtkChild
    public Overlay overlay;

    public SimulationView(MemorySegment address) {
        super(address);
    }

    @InstanceInit
    public void init() {
        overlay.setClipOverlay(vehiclesView, true);
    }

    public void setWorld(World world) {
        trackView.setWorld(world);
        vehiclesView.setWorld(world);
    }

    public void update() {
        trackView.queueDraw();
        vehiclesView.update();
    }

}
