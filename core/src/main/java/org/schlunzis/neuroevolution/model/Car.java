package org.schlunzis.neuroevolution.model;

import lombok.Getter;

import java.util.Random;

public class Car {

    @Getter
    private double x;
    @Getter
    private double y;
    private double direction; // radians
    private double speed;

    private static final Random RNG = new Random();

    public Car(double x, double y) {
        this.x = x;
        this.y = y;
        this.direction = RNG.nextDouble() * 2 * Math.PI; // random initial direction
        this.speed = 2.0;
    }

    public void update() {
        // Random walk: small direction change
        direction += (RNG.nextDouble() - 0.5) * 0.3;

        x += Math.cos(direction) * speed;
        y += Math.sin(direction) * speed;
    }

    public void clamp(double width, double height) {
        x = Math.max(0, Math.min(width, x));
        y = Math.max(0, Math.min(height, y));
    }


}
