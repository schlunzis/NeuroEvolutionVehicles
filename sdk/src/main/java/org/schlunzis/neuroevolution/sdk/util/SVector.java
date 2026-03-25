package org.schlunzis.neuroevolution.sdk.util;

public record SVector(double x, double y, double z) {

    public SVector() {
        this(0, 0);
    }

    public SVector(double x, double y) {
        this(x, y, 0);
    }

    public SVector(SVector other) {
        this(
                other.x(),
                other.y(),
                other.z()
        );
    }

    public SVector add(SVector other) {
        return add(this, other);
    }

    public SVector sub(SVector other) {
        return sub(this, other);
    }

    public SVector mult(double factor) {
        return mult(this, factor);
    }

    public SVector div(double factor) {
        return div(this, factor);
    }

    public static SVector add(SVector a, SVector b) {
        return new SVector(a.x() + b.x(), a.y() + b.y(), a.z() + b.z());
    }

    public static SVector sub(SVector a, SVector b) {
        return new SVector(a.x() - b.x(), a.y() - b.y(), a.z() - b.z());
    }

    public static SVector mult(SVector v, double factor) {
        return new SVector(v.x() * factor, v.y() * factor, v.z() * factor);
    }

    public static SVector div(SVector v, double factor) {
        return new SVector(v.x() / factor, v.y() / factor, v.z() / factor);
    }

    public SVector rotate(double angle) {
        double x = x() * Math.cos(angle) - y() * Math.sin(angle);
        double y = x() * Math.sin(angle) + y() * Math.cos(angle);
        return new SVector(x, y, z());
    }

    public SVector withLimit(double limit) {
        double mag = mag();
        if (mag > limit) {
            return mult(this, limit / mag);
        }
        return this;
    }

    public double mag() {
        return Math.sqrt(x * x + y * y);
    }

    public SVector withMag(double mag) {
        return mult(this, mag / mag());
    }

    public SVector normalize() {
        double m = mag();
        return new SVector(x / m, y / m);
    }

    public static SVector fromAngle(double angle) {
        return new SVector(Math.cos(angle), Math.sin(angle));
    }

    public double getAngle() {
        double angle = Math.atan2(y, x);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
        return angle;
    }

    public double getRawAngle() {
        return Math.atan2(y, x);
    }

    public static double dist(SVector v1, SVector v2) {
        return Math.sqrt(Math.pow(v1.x() - v2.x(), 2) + Math.pow(v1.y() - v2.y(), 2));
    }

    public SVector cross(SVector v) {
        return new SVector(
                y() * v.z() - v.y() * z(),
                z() * v.x() - v.z() * x(),
                x() * v.y() - v.x() * y()
        );
    }
}
