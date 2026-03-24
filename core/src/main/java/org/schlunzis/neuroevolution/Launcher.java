package org.schlunzis.neuroevolution;

import org.gnome.gio.Resource;
import org.javagi.base.GErrorException;
import org.javagi.gobject.types.Types;
import org.schlunzis.neuroevolution.view.App;
import org.schlunzis.neuroevolution.view.track.TrackView;

public class Launcher {

    static {
        Types.register(TrackView.class);
    }

    static void main(String[] args) throws GErrorException {
        var resource = Resource.load("core/src/main/resources/gresources/neuroevolution.gresource");
        resource.resourcesRegister();

        new App().run(args);
    }
}