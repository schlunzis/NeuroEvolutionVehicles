package org.schlunzis.neuroevolution.view.tabs;

import org.gnome.gtk.Box;
import org.gnome.gtk.Label;
import org.gnome.gtk.Orientation;

public class Tab1View extends Box {

    public Tab1View() {
        super(Orientation.VERTICAL, 6);
        append(new Label("Tab 1 content"));
    }
}
