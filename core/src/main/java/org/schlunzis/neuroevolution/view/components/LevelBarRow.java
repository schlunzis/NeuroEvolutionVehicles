package org.schlunzis.neuroevolution.view.components;

import lombok.Getter;
import org.gnome.gtk.ListBoxRow;
import org.javagi.gtk.annotations.GtkTemplate;

import java.lang.foreign.MemorySegment;

@Getter
@GtkTemplate(ui = "/org/schlunzis/neuroevolution/components/level-bar-row.ui")
public class LevelBarRow extends ListBoxRow {

    private String title;
    private double minValue;
    private double maxValue;
    private double value;

    public LevelBarRow(MemorySegment address) {
        super(address);
    }

    public void setTitle(String title) {
        this.title = title;
        this.notify("title");
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
        this.notify("min-value");
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
        this.notify("max-value");
    }

    public void setValue(double value) {
        this.value = value;
        this.notify("value");
    }
}
