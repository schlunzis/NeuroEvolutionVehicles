package org.schlunzis.neuroevolution;

import org.gnome.adw.Application;
import org.schlunzis.neuroevolution.view.MainWindow;

public class App {

    static void main(String[] args) {
        new App(args);
    }

    public App(String[] args) {
        Application app = new Application("org.schlunzis.neuroevolution");
        app.onActivate(() -> activate(app));
        app.run(args);
    }

    private void activate(Application app) {
        MainWindow window = new MainWindow(app);
        window.present();
    }
}