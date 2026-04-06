package org.schlunzis.neuroevolution.view.tabs;

import org.gnome.gtk.Box;
import org.gnome.gtk.Button;
import org.javagi.gtk.annotations.GtkCallback;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;
import org.schlunzis.neuroevolution.simulation.SimulationController;

import java.lang.foreign.MemorySegment;

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

}
