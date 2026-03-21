package org.schlunzis.neuroevolution;

import org.gnome.adw.*;
import org.gnome.gtk.Box;
import org.gnome.gtk.Label;
import org.gnome.gtk.Orientation;
import org.gnome.gtk.Paned;

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

        ApplicationWindow window = new ApplicationWindow(app);
        window.setDefaultSize(800, 400);

        // HeaderBar with winddow controls
        ToolbarView toolbarView = new ToolbarView();
        window.setContent(toolbarView);
        HeaderBar headerBar = new HeaderBar();
        headerBar.setTitleWidget(
                new WindowTitle("Demo", "Soon to be simulated")
        );
        toolbarView.addTopBar(headerBar);

        // Split pane
        Paned splitPane = new Paned(Orientation.HORIZONTAL);
        toolbarView.setContent(splitPane);


        // Left Pane: Drawing Area
        // FIXME
        Label scene = new Label("Hello World!");
        splitPane.setStartChild(scene);

        // Right pane: tab group

        TabView tabView = new TabView();
        TabBar tabBar = new TabBar();
        tabBar.setView(tabView);

        Box configBox = new Box(Orientation.VERTICAL, 0);
        configBox.append(tabBar);
        configBox.append(tabView);

        splitPane.setEndChild(configBox);


        // tab 1
        Box tab1Content = new Box(Orientation.VERTICAL, 6);
        // tab1Content.setMarginTop(12);
        // tab1Content.setMarginStart(12);
        tab1Content.append(new Label("Tab 1 content"));

        TabPage tab1 = tabView.append(tab1Content);
        tab1.setTitle("Tab 1");

        // Tab 2
        Box tab2Content = new Box(Orientation.VERTICAL, 6);
        // tab2Content.setMarginTop(12);
        // tab2Content.setMarginStart(12);
        tab2Content.append(new Label("Tab 2 content"));

        TabPage tab2 = tabView.append(tab2Content);
        tab2.setTitle("Tab 2");

        window.present();
    }
}