package org.schlunzis.neuroevolution.view;

import lombok.extern.slf4j.Slf4j;
import org.freedesktop.cairo.Cairo;
import org.gnome.adw.AboutDialog;
import org.gnome.adw.Application;
import org.gnome.gdk.Display;
import org.gnome.gio.ApplicationFlags;
import org.gnome.gio.SimpleAction;
import org.gnome.glib.Variant;
import org.gnome.gtk.IconTheme;

@Slf4j
public class App extends Application {

    @Override
    public void activate() {
        try {
            Cairo.ensureInitialized();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        IconTheme theme = IconTheme.getForDisplay(Display.getDefault());
        theme.addResourcePath("/org/schlunzis/neuroevolution/icons");
        AppWindow win = new AppWindow(this);
        win.present();
    }

    public void preferencesActivated(Variant parameter) {
        System.out.println("Preferences activated");
    }

    public void quitActivated(Variant parameter) {
        super.quit();
    }


    public void aboutActivated(Variant parameter) {
        String[] developers = {"Tilman Holube", "Jonas Pohl"};
        var about = AboutDialog.builder()
                .setApplicationName("NeuroEvolution")
                .setApplicationIcon("org.schlunzis.neuroevolution")
                .setDeveloperName("Schlunzis")
                .setDevelopers(developers)
                .setVersion("0.0.1")
                .setCopyright("© 2026 Schlunzis")
                .build();
        about.present(this.getActiveWindow());
    }

    @Override
    protected void startup() {
        super.startup();
        var preferences = new SimpleAction("preferences", null);
        preferences.onActivate(this::preferencesActivated);
        addAction(preferences);

        var about = new SimpleAction("about", null);
        about.onActivate(this::aboutActivated);
        addAction(about);

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