package org.schlunzis.neuroevolution.view.components;

import lombok.Getter;
import lombok.Setter;
import org.gnome.gtk.Box;
import org.gnome.gtk.ListBoxRow;
import org.gnome.gtk.Widget;
import org.javagi.gtk.annotations.GtkChild;
import org.javagi.gtk.annotations.GtkTemplate;

import java.lang.foreign.MemorySegment;

@Setter
@Getter
@GtkTemplate(ui = "/org/schlunzis/neuroevolution/components/custom-row.ui")
public class CustomRow extends ListBoxRow {

    @GtkChild
    public Box box;

    private String title;
    private String sideText;
    private Widget content;

    public CustomRow(MemorySegment address) {
        super(address);
    }

    public void setSideText(String sideText) {
        this.sideText = sideText;
        this.notify("side-text");
    }

    public void setContent(Widget content) {
        if (this.content != null) box.remove(this.content);
        this.content = content;
        box.append(content);
        this.notify("content");
    }
}
