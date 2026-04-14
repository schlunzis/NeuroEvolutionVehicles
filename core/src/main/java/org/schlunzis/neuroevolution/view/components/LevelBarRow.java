package org.schlunzis.neuroevolution.view.components;

import lombok.Getter;
import lombok.Setter;
import org.gnome.gtk.LevelBarMode;
import org.javagi.gobject.annotations.Layout;
import org.javagi.gtk.annotations.GtkTemplate;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

@Setter
@Getter
@GtkTemplate(ui = "/org/schlunzis/neuroevolution/components/level-bar-row.ui")
public class LevelBarRow extends CustomRow {

    private double minValue;
    private double maxValue;
    private double value;
    @SuppressWarnings("unused")
    private LevelBarMode mode;

    public LevelBarRow(MemorySegment address) {
        super(address);
    }

    @Layout
    public static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                CustomRow.getMemoryLayout().withName("parent_instance")
        ).withName("org_schlunzis_neuroevolution_view_components_LevelBarRow");
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
        setSideText(text);

        this.notify("value");
    }
}
