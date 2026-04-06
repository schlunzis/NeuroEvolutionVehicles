package org.schlunzis.neuroevolution;

import org.gnome.gio.Resource;
import org.javagi.base.GErrorException;
import org.javagi.gobject.types.Types;
import org.javagi.gtk.types.TemplateTypes;
import org.schlunzis.neuroevolution.view.App;
import org.schlunzis.neuroevolution.view.simulation.SimulationView;
import org.schlunzis.neuroevolution.view.simulation.TrackView;
import org.schlunzis.neuroevolution.view.simulation.VehicleView;
import org.schlunzis.neuroevolution.view.simulation.VehiclesView;
import org.schlunzis.neuroevolution.view.tabs.SimulationTab;

public class Launcher {

    static {
        TemplateTypes.register(SimulationView.class);
        TemplateTypes.register(SimulationTab.class);

        Types.register(TrackView.class);
        Types.register(VehiclesView.class);
        Types.register(VehicleView.class);
    }

    static void main(String[] args) throws GErrorException {
        var resource = Resource.load("core/src/main/resources/gresources/neuroevolution.gresource");
        resource.resourcesRegister();

        new App().run(args);
    }
}