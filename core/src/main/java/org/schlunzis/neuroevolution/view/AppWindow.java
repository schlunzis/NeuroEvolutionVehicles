package org.schlunzis.neuroevolution.view;

import org.gnome.adw.ApplicationWindow;
import org.gnome.adw.TabView;
import org.gnome.gio.MenuModel;
import org.gnome.gtk.GtkBuilder;
import org.gnome.gtk.MenuButton;
import org.javagi.gobject.annotations.InstanceInit;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;
import org.schlunzis.neuroevolution.model.Vehicle;
import org.schlunzis.neuroevolution.model.World;
import org.schlunzis.neuroevolution.simulation.SimulationController;
import org.schlunzis.neuroevolution.view.simulation.SimulationView;
import org.schlunzis.neuroevolution.view.simulation.VehiclesView;
import org.schlunzis.neuroevolution.view.tabs.SimulationTab;
import org.schlunzis.neuroevolution.view.tabs.VehicleTab;

@GtkTemplate(ui = "/org/schlunzis/neuroevolution/window.ui")
public class AppWindow extends ApplicationWindow {

    @GtkChild
    public MenuButton gears;

    @GtkChild
    public SimulationView simulationView;

    @GtkChild
    public TabView tab_view;
    @GtkChild
    public SimulationTab simulationTab;

    private SimulationController controller;

    public AppWindow(App app) {
        setApplication(app);
    }

    @InstanceInit
    public void init() {
        try {
            World world = new World();
            simulationView.setWorld(world);
            controller = new SimulationController(world, () -> {
                simulationView.update();
                simulationTab.update();
            });
            simulationTab.setController(controller);

            GtkBuilder builder = GtkBuilder.fromResource("/org/schlunzis/neuroevolution/gears-menu.ui");
            MenuModel menu = (MenuModel) builder.getObject("menu");
            gears.setMenuModel(menu);
            controller.start();

            simulationView.getVehiclesView().connect("selected",
                    (VehiclesView.Selected) vehicle -> showVehicleTab(vehicle.getVehicle()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showVehicleTab(Vehicle vehicle) {
        // TODO check for existing page for the vehicle
        VehicleTab vehicleTab = new VehicleTab(vehicle);
        vehicleTab.setController(controller);
        tab_view.addPage(vehicleTab, null);
    }

}
