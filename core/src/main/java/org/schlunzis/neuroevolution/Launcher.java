package org.schlunzis.neuroevolution;

import org.gnome.gio.Resource;
import org.javagi.base.GErrorException;

public class Launcher {

    static void main(String[] args) throws GErrorException {
        var resource = Resource.load("core/src/main/resources/neuroevolution.gresource");
        resource.resourcesRegister();

        new App().run(args);
    }
}