package org.schlunzis.neuroevolution;

import org.gnome.adw.Application;
import org.gnome.adw.Window;
import org.gnome.gio.ApplicationFlags;
import org.gnome.gtk.GtkBuilder;
import org.javagi.base.GErrorException;

public class App {

    private static void activate(Application app) {
        GtkBuilder builder = new GtkBuilder();
        try {
            builder.addFromFile("core/src/main/resources/view.ui");
        } catch (GErrorException _) {
        }

        Window window = (Window) builder.getObject("window");
        window.setApplication(app);

        window.setVisible(true);
    }

    static void main(String[] args) {
        Application app = new Application("org.schlunzis.neuroevolution", ApplicationFlags.DEFAULT_FLAGS);
        app.onActivate(() -> activate(app));
        app.run(args);
    }
}