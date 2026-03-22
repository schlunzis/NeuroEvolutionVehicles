package org.schlunzis.neuroevolution;

import org.gnome.adw.ApplicationWindow;
import org.gnome.gio.MenuModel;
import org.gnome.gtk.DrawingArea;
import org.gnome.gtk.GtkBuilder;
import org.gnome.gtk.MenuButton;
import org.javagi.gobject.annotations.InstanceInit;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;

@GtkTemplate(ui = "/org/schlunzis/neuroevolution/window.ui")
public class AppWindow extends ApplicationWindow {

    @GtkChild
    public MenuButton gears;

    @GtkChild
    public DrawingArea scene;

    public AppWindow(App app) {
        setApplication(app);
    }

    @InstanceInit
    public void init() {
        GtkBuilder builder = GtkBuilder.fromResource("/org/schlunzis/neuroevolution/gears-menu.ui");
        MenuModel menu = (MenuModel) builder.getObject("menu");
        gears.setMenuModel(menu);
        scene.setDrawFunc((_, cr, width, height) -> {
            cr.setSourceRGB(1, 1, 1);
            cr.paint();
            cr.setSourceRGB(0, 0, 1);
            cr.arc(width / 2.0, height / 2.0, 50, 0, 2 * Math.PI);
            cr.stroke();
        });
        scene.onResize((_, _) -> scene.queueDraw());
    }


}