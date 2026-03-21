package org.schlunzis.neuroevolution.view;

import org.gnome.adw.TabBar;
import org.gnome.adw.TabView;
import org.gnome.gtk.Box;
import org.gnome.gtk.Orientation;
import org.schlunzis.neuroevolution.view.tabs.Tab1View;
import org.schlunzis.neuroevolution.view.tabs.Tab2View;

public class ConfigView extends Box {

    private final TabView tabView;

    public ConfigView() {
        super(Orientation.VERTICAL, 0);

        tabView = new TabView();
        TabBar tabBar = new TabBar();
        tabBar.setView(tabView);

        append(tabBar);
        append(tabView);
        addTabs();

    }

    private void addTabs() {
        tabView.append(new Tab1View()).setTitle("Tab 1");
        tabView.append(new Tab2View()).setTitle("Tab 2");
    }

}
