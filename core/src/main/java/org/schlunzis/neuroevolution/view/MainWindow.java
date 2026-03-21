package org.schlunzis.neuroevolution.view;

import org.gnome.adw.*;
import org.gnome.gtk.Orientation;
import org.gnome.gtk.Paned;
import org.gnome.gtk.Widget;

public class MainWindow extends ApplicationWindow {

    public MainWindow(Application app) {
        super(app);
        setDefaultSize(800, 400);
        setContent(buildUi());
    }

    private Widget buildUi() {
        ToolbarView root = new ToolbarView();
        root.addTopBar(createHeaderBar());
        root.setContent((createSplitPane()));
        return root;
    }

    private HeaderBar createHeaderBar() {
        HeaderBar headerBar = new HeaderBar();
        headerBar.setTitleWidget(
                new WindowTitle("Demo", "Soon to be simulated")
        );
        return headerBar;
    }

    private Widget createSplitPane() {
        Paned splitPane = new Paned(Orientation.HORIZONTAL);
        splitPane.setStartChild(new SceneView());
        splitPane.setEndChild(new ConfigView());
        return splitPane;
    }

}
