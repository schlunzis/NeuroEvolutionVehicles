package org.schlunzis.neuroevolution.view;

import org.gnome.adw.ApplicationWindow;
import org.gnome.gio.MenuModel;
import org.gnome.gtk.DrawingArea;
import org.gnome.gtk.GtkBuilder;
import org.gnome.gtk.MenuButton;
import org.javagi.gobject.annotations.InstanceInit;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;
import org.schlunzis.neuroevolution.model.Car;
import org.schlunzis.neuroevolution.model.World;
import org.schlunzis.neuroevolution.simulation.SimulationController;

@GtkTemplate(ui = "/org/schlunzis/neuroevolution/window.ui")
public class AppWindow extends ApplicationWindow {

    @GtkChild
    public MenuButton gears;

    @GtkChild
    public DrawingArea scene;

    private SimulationController controller;

    public AppWindow(App app) {
        setApplication(app);
    }

    @InstanceInit
    public void init() {
        World world = new World(20, 400, 400);
        controller = new SimulationController(world, scene::queueDraw);


        GtkBuilder builder = GtkBuilder.fromResource("/org/schlunzis/neuroevolution/gears-menu.ui");
        MenuModel menu = (MenuModel) builder.getObject("menu");
        gears.setMenuModel(menu);
        scene.setDrawFunc((_, cr, _, _) -> {

            // background
            cr.setSourceRGB(1, 1, 1);
            cr.paint();


            cr.setSourceRGB(0, 0, 1);
            for (Car car : world.getCars()) {
                cr.arc(car.getX(), car.getY(), 5, 0, 2 * Math.PI);
                cr.stroke();
            }
        });
        scene.onResize((width, height) -> {
            world.resize(width, height);
            scene.queueDraw();
        });
        controller.start();
    }


}