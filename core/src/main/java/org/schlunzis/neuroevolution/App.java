package org.schlunzis.neuroevolution;

import org.freedesktop.cairo.Cairo;
import org.gnome.adw.Application;
import org.gnome.gio.ApplicationFlags;
import org.gnome.gio.SimpleAction;
import org.gnome.glib.Variant;

public class App extends Application {

    @Override
    public void activate() {
        try {
            Cairo.ensureInitialized();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        AppWindow win = new AppWindow(this);
        win.present();
    }

    public void preferencesActivated(Variant parameter) {
        System.out.println("Preferences activated");
    }

    public void quitActivated(Variant parameter) {
        super.quit();
    }

    @Override
    protected void startup() {
        super.startup();
        var preferences = new SimpleAction("preferences", null);
        preferences.onActivate(this::preferencesActivated);
        addAction(preferences);

        var quit = new SimpleAction("quit", null);
        quit.onActivate(this::quitActivated);
        addAction(quit);

        String[] quitAccels = new String[]{"<Ctrl>q"};
        setAccelsForAction("app.quit", quitAccels);
    }

    public App() {
        setApplicationId("org.schlunzis.neuroevolution");
        setFlags(ApplicationFlags.DEFAULT_FLAGS);
    }
}