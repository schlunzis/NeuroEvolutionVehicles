package org.schlunzis.neuroevolution.view.components;

import lombok.Getter;
import lombok.Setter;
import org.gnome.gtk.LevelBar;
import org.gnome.gtk.ListBoxRow;
import org.javagi.gobject.annotations.InstanceInit;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;

import java.lang.foreign.MemorySegment;

@Setter
@Getter
@GtkTemplate(ui = "/org/schlunzis/neuroevolution/components/direction-bar-row.ui")
public class DirectionBarRow extends ListBoxRow {

    @GtkChild
    public LevelBar levelBarLeft;
    @GtkChild
    public LevelBar levelBarRight;

    @SuppressWarnings("unused")
    private String title;
    private double minValue;
    private double maxValue;
    private double value;
    private String valueText;

    public DirectionBarRow(MemorySegment address) {
        super(address);
    }

    @InstanceInit
    public void init() {
        levelBarLeft.setMinValue(0);
        levelBarLeft.setMaxValue(0);
        levelBarRight.setMinValue(0);
        levelBarRight.setMaxValue(0);
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
        levelBarLeft.setMaxValue(-minValue / 2);
        this.notify("min-value");
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
        levelBarRight.setMaxValue(maxValue / 2);
        this.notify("max-value");
    }

    public void setValue(double value) {
        this.value = value;

        String text = String.format("%.6f", value);
        setValueText(text);

        if (value < 0) {
            levelBarLeft.setValue(-value);
            levelBarRight.setValue(0);
        } else if (value > 0) {
            levelBarLeft.setValue(0);
            levelBarRight.setValue(value);
        } else {
            levelBarLeft.setValue(0);
            levelBarRight.setValue(0);
        }

        this.notify("value");
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
        this.notify("value-text");
    }
}
