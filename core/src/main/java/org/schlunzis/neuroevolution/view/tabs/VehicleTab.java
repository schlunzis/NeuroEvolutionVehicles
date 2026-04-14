package org.schlunzis.neuroevolution.view.tabs;

import lombok.extern.slf4j.Slf4j;
import org.gnome.adw.ActionRow;
import org.gnome.gio.File;
import org.gnome.gtk.Box;
import org.gnome.gtk.FileDialog;
import org.gnome.gtk.FileFilter;
import org.gnome.gtk.LevelBar;
import org.javagi.base.GErrorException;
import org.javagi.gtk.annotations.GtkCallback;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;
import org.schlunzis.neuroevolution.model.Vehicle;
import org.schlunzis.neuroevolution.sdk.Constants;
import org.schlunzis.neuroevolution.simulation.SimulationController;

import java.io.IOException;

@Slf4j
@GtkTemplate(ui = "/org/schlunzis/neuroevolution/vehicle-tab.ui")
public class VehicleTab extends Box {

    private final SimulationController controller;
    private final Vehicle vehicleToShow;

    @GtkChild
    public ActionRow idRow;

    @GtkChild
    public LevelBar velocityBar;

    public VehicleTab(SimulationController controller, Vehicle vehicle) {
        this.controller = controller;
        this.vehicleToShow = vehicle;
        idRow.setSubtitle(vehicle.getId().toString());
        velocityBar.setMinValue(0);
        velocityBar.setMaxValue(Constants.MAX_SPEED);
    }

    public void update() {
        velocityBar.setValue(vehicleToShow.getVel().mag());
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
