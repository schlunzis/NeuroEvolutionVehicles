package org.schlunzis.neuroevolution.view;

import org.gnome.adw.ApplicationWindow;
import org.gnome.gio.MenuModel;
import org.gnome.gtk.Button;
import org.gnome.gtk.GtkBuilder;
import org.gnome.gtk.MenuButton;
import org.javagi.gobject.annotations.InstanceInit;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;
import org.schlunzis.neuroevolution.model.World;
import org.schlunzis.neuroevolution.simulation.SimulationController;
import org.schlunzis.neuroevolution.view.track.TrackView;
import org.schlunzis.neuroevolution.view.track.VehiclesView;

@GtkTemplate(ui = "/org/schlunzis/neuroevolution/window.ui")
public class AppWindow extends ApplicationWindow {

    @GtkChild
    public MenuButton gears;

    @GtkChild
    public TrackView trackView;

    @GtkChild
    public VehiclesView vehiclesView;

    @GtkChild
    public Button toggleButton;

    private SimulationController controller;

    public AppWindow(App app) {
        setApplication(app);
    }

    @InstanceInit
    public void init() {
        World world = new World();
        controller = new SimulationController(world, () -> {
            trackView.queueDraw();
            vehiclesView.update();
        });

        trackView.setWorld(world);
        vehiclesView.setWorld(world);

        GtkBuilder builder = GtkBuilder.fromResource("/org/schlunzis/neuroevolution/gears-menu.ui");
        MenuModel menu = (MenuModel) builder.getObject("menu");
        gears.setMenuModel(menu);
        controller.start();

        toggleButton.onClicked(() ->
                controller.toggle()
        );
    }


}