package org.schlunzis.neuroevolution.view;

import org.gnome.adw.ApplicationWindow;
import org.gnome.adw.TabPage;
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@GtkTemplate(ui = "/org/schlunzis/neuroevolution/window.ui")
public class AppWindow extends ApplicationWindow {

    private final Map<UUID, VehicleTabInfo> vehicleTabs = new HashMap<>();

    @GtkChild
    public MenuButton gears;

    @GtkChild
    public SimulationView simulationView;

    @GtkChild
    public TabView tab_view;
    @GtkChild
    public SimulationTab simulationTab;
    @GtkChild
    public VehicleTab staticVehicleTab;

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
                vehicleTabs.values().forEach(t -> t.tab().update());
                if (staticVehicleTab.getVehicleToShow() == null || staticVehicleTab.getVehicleToShow().isDead()) {
                    replaceVehicleInStaticTab();
                } else {
                    staticVehicleTab.update();
                }
            });
            simulationTab.setController(controller);

            GtkBuilder builder = GtkBuilder.fromResource("/org/schlunzis/neuroevolution/gears-menu.ui");
            MenuModel menu = (MenuModel) builder.getObject("menu");
            gears.setMenuModel(menu);
            controller.start();

            simulationView.getVehiclesView().connect("selected",
                    (VehiclesView.Selected) vehicle -> showVehicleTab(vehicle.getVehicle()));
            tab_view.onClosePage(page -> {
                UUID id = vehicleTabs.entrySet().stream()
                        .filter(e -> e.getValue().page() == page)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElseThrow();
                vehicleTabs.remove(id);
                return false;

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replaceVehicleInStaticTab() {
        Vehicle current = staticVehicleTab.getVehicleToShow();
        if (current != null) {
            current.setHighlightMode(Vehicle.HighlightMode.NONE);
        }

        controller.getWorld().getGa().getPopulation().stream()
                .max(Comparator.comparingInt(Vehicle::getCheckPointFitness))
                .ifPresent(v -> {
                    staticVehicleTab.setVehicleToShow(v);
                    v.setHighlightMode(Vehicle.HighlightMode.STATIC_TAB);
                });
    }

    public void showVehicleTab(Vehicle vehicle) {
        VehicleTabInfo info = vehicleTabs.computeIfAbsent(vehicle.getId(),
                _ -> {
                    VehicleTab tab = new VehicleTab(vehicle);
                    TabPage page = tab_view.addPage(tab, null);
                    page.setTitle(vehicle.getId().toString().substring(0, 5));
                    return new VehicleTabInfo(tab, page);
                }
        );
        tab_view.setSelectedPage(info.page());
    }

    private record VehicleTabInfo(VehicleTab tab, TabPage page) {

    }

}
