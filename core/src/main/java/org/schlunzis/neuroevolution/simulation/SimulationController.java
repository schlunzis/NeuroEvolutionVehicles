package org.schlunzis.neuroevolution.simulation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.gnome.glib.GLib;
import org.schlunzis.neuroevolution.model.World;

@RequiredArgsConstructor
public class SimulationController {
    @Getter
    private final World world;
    private final Runnable redrawCallback;

    public void start() {
        GLib.timeoutAdd(GLib.PRIORITY_DEFAULT, 16, () -> { // ~60 FPS
            world.update();
            redrawCallback.run();
            return true;
        });
    }

}
