package org.schlunzis.neuroevolution.model.g;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.gnome.gobject.GObject;
import org.schlunzis.neuroevolution.model.Vehicle;

@Getter
@AllArgsConstructor
public class GVehicle extends GObject {

    private final Vehicle vehicle;

}
