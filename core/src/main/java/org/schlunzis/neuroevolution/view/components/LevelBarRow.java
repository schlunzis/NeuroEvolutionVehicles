package org.schlunzis.neuroevolution.view.components;

import lombok.Getter;
import lombok.Setter;
import org.gnome.gtk.LevelBarMode;
import org.gnome.gtk.ListBoxRow;
import org.javagi.gtk.annotations.GtkTemplate;

import java.lang.foreign.MemorySegment;

@Setter
@Getter
@GtkTemplate(ui = "/org/schlunzis/neuroevolution/components/level-bar-row.ui")
public class LevelBarRow extends ListBoxRow {

    @SuppressWarnings("unused")
    private String title;
    private double minValue;
    private double maxValue;
    private double value;
    private String valueText;
    @SuppressWarnings("unused")
    private LevelBarMode mode;

    public LevelBarRow(MemorySegment address) {
        super(address);
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

        String format;
        LevelBarMode mode = this.getMode();
        if (mode != null && mode.equals(LevelBarMode.DISCRETE)) format = "%.0f";
        else format = "%.6f";
        String text = String.format(format, value);
        setValueText(text);

        this.notify("value");
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
        this.notify("value-text");
    }
}
