package org.schlunzis.neuroevolution.view.tabs;

import lombok.extern.slf4j.Slf4j;
import org.gnome.adw.ActionRow;
import org.gnome.gio.File;
import org.gnome.gtk.Box;
import org.gnome.gtk.FileDialog;
import org.gnome.gtk.FileFilter;
import org.javagi.base.GErrorException;
import org.javagi.gtk.annotations.GtkCallback;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;
import org.schlunzis.neuroevolution.model.Vehicle;
import org.schlunzis.neuroevolution.simulation.SimulationController;

import java.io.IOException;

@Slf4j
@GtkTemplate(ui = "/org/schlunzis/neuroevolution/vehicle-tab.ui")
public class VehicleTab extends Box {

    private final Vehicle vehicleToShow;
    @GtkChild
    public ActionRow idRow;
    private SimulationController controller;

    public VehicleTab(Vehicle vehicle) {
        this.vehicleToShow = vehicle;
        idRow.setSubtitle(vehicle.getId().toString());
    }

    public void setController(SimulationController controller) {
        this.controller = controller;
    }

    @GtkCallback
    public void saveModelButtonPressed() {
        // TODO deduplicate
        FileFilter filter = new FileFilter();
        filter.addPattern("*.nn");

        FileDialog dialog = FileDialog.builder().setDefaultFilter(filter).build();
        dialog.save(null, null, (_, res, _) -> {
            try {
                File selection = dialog.saveFinish(res);
                java.io.File outputFile = new java.io.File(selection.getPath());
                vehicleToShow.getGenotype().brain().save(outputFile);
            } catch (GErrorException | IOException ex) {
                log.debug(ex.getLocalizedMessage());
                // user probably just canceled the dialog, so we can ignore this error
            }
        });
    }


}
