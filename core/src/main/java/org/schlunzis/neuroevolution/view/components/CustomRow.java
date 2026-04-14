package org.schlunzis.neuroevolution.view.components;

import lombok.Getter;
import lombok.Setter;
import org.gnome.gtk.Box;
import org.gnome.gtk.ListBoxRow;
import org.gnome.gtk.Widget;
import org.javagi.gobject.annotations.Layout;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

@Setter
@Getter
@GtkTemplate(ui = "/org/schlunzis/neuroevolution/components/custom-row.ui")
public class CustomRow extends ListBoxRow {

    @GtkChild
    public Box box;

    @SuppressWarnings("unused")
    private String title;
    private String sideText;
    private Widget content;

    public CustomRow(MemorySegment address) {
        super(address);
    }

    @Layout
    public static MemoryLayout getMemoryLayout() {
        return MemoryLayout.structLayout(
                ListBoxRow.getMemoryLayout().withName("parent_instance")
        ).withName("org_schlunzis_neuroevolution_view_components_CustomRow");
    }

    public void setSideText(String sideText) {
        this.sideText = sideText;
        this.notify("side-text");
    }

    public void setContent(Widget content) {
        box.remove(this.content);
        this.content = content;
        box.append(content);
        this.notify("content");
    }
}
