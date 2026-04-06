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

    @Getter
    private boolean running = true;

    public void start() {
        GLib.timeoutAdd(GLib.PRIORITY_DEFAULT, 16, () -> { // ~60 FPS
            if (running)
                world.update();
            redrawCallback.run();
            return true;
        });
    }

    /// not at all thread safe, but good enough to begin with
    public void toggle() {
        running = !running;
    }

}
