package org.schlunzis.neuroevolution.view.tabs;

import lombok.extern.slf4j.Slf4j;
import org.gnome.gio.File;
import org.gnome.gtk.Box;
import org.gnome.gtk.Button;
import org.gnome.gtk.FileDialog;
import org.gnome.gtk.FileFilter;
import org.javagi.base.GErrorException;
import org.javagi.gtk.annotations.GtkCallback;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;
import org.schlunzis.neuroevolution.simulation.SimulationController;

import java.io.IOException;
import java.lang.foreign.MemorySegment;

@Slf4j
@GtkTemplate(ui = "/org/schlunzis/neuroevolution/simulation-tab.ui")
public class SimulationTab extends Box {

    @GtkChild
    public Button toggleRunningButton;

    private SimulationController controller;

    public SimulationTab(MemorySegment address) {
        super(address);
    }

    public void setController(SimulationController controller) {
        this.controller = controller;
        update();
    }

    public void update() {
        updateToggleRunningButton();
    }

    private void updateToggleRunningButton() {
        boolean running = controller.isRunning();
        String iconName = running ? "pause-symbolic" : "play-symbolic";
        toggleRunningButton.setIconName(iconName);
    }

    @GtkCallback
    public void toggleRunningButtonPressed() {
        this.controller.toggle();
        updateToggleRunningButton();
    }

    @GtkCallback
    public void nextGenerationButtonPressed() {
        this.controller.getWorld().getGa().triggerNextGeneration();
    }


    @GtkCallback
    public void exportModelButtonPressed() {
        FileFilter filter = new FileFilter();
        filter.addPattern("*.nn");

        FileDialog dialog = FileDialog.builder().setDefaultFilter(filter).build();
        dialog.save(null, null, (_, res, _) -> {
            try {
                File selection = dialog.saveFinish(res);

                java.io.File outputFile = new java.io.File(selection.getPath());

                controller.getWorld().getGa().getPrevBest().getGenotype().brain().save(outputFile);


            } catch (GErrorException | IOException ex) {
                log.debug(ex.getLocalizedMessage());
                // user probably just canceled the dialog, so we can ignore this error
            }
        });
    }
}
