package org.schlunzis.neuroevolution.view.simulation;


import lombok.Getter;
import org.gnome.gtk.Fixed;
import org.gnome.gtk.Widget;
import org.jspecify.annotations.NonNull;
import org.schlunzis.neuroevolution.model.Vehicle;
import org.schlunzis.neuroevolution.model.World;
import org.schlunzis.neuroevolution.sdk.util.SVector;

import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class VehiclesView extends Fixed {
    private final List<VehicleView> views = new ArrayList<>();
    private final List<Widget> children = new CopyOnWriteArrayList<>();
    @Getter
    private World world;

    public VehiclesView(MemorySegment address) {
        super(address);
    }

    public void setWorld(World world) {
        this.world = world;
        world.getGa().addNewGenerationHook(this::reset);
        reset();
    }

    public void update() {
        int fixedWidth = this.getParent().getWidth();
        int fixedHeight = this.getParent().getHeight();
        for (int i = views.size() - 1; i >= 0; i--) {
            VehicleView view = views.get(i);
            if (view.isFinished()) {
                remove(view);
                views.remove(view);
                continue;
            }
            Vehicle vehicle = view.getVehicle();
            SVector pos = vehicle.getPos();
            double width = vehicle.getVehicleWidth() * fixedWidth;
            double height = vehicle.getVehicleHeight() * fixedHeight;
            double posX = (pos.x() * fixedWidth) - width / 2;
            double posY = (pos.y() * fixedHeight) - height / 2;

            view.setContentWidth((int) width);
            view.setContentHeight((int) height);

            this.move(view, posX, posY);
            view.setDirection(Math.toDegrees(vehicle.getVel().rawAngle()));
        }
    }

    @Override
    public void put(@NonNull Widget widget, double x, double y) {
        children.add(widget);
        super.put(widget, x, y);
    }

    @Override
    public void remove(@NonNull Widget widget) {
        children.remove(widget);
        super.remove(widget);
    }

    public void removeAllChildren() {
        children.forEach(this::remove);
        children.clear();
    }

    public void reset() {
        removeAllChildren();
        views.clear();
        world.getGa().getPopulation().forEach(v -> {
            VehicleView view = new VehicleView(v);
            views.add(view);
            this.put(view, 0, 0);
        });
    }

}
