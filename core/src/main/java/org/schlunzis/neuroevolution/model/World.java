package org.schlunzis.neuroevolution.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class World {
    @Getter
    private final List<Car> cars = new ArrayList<>();
    private double width;
    private double height;

    public World(int carCount, double width, double height) {
        this.width = width;
        this.height = height;
        for (int i = 0; i < carCount; i++) {
            cars.add(new Car(width / 2, height / 2));
        }
    }


    public void update() {
        for (Car car : cars) {
            car.update();
            car.clamp(width, height);
        }
    }

    public void resize(double width, double height) {
        this.width = width;
        this.height = height;
    }

}
