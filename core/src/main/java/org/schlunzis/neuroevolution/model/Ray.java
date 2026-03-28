package org.schlunzis.neuroevolution.model;

import org.schlunzis.neuroevolution.sdk.util.Boundary;
import org.schlunzis.neuroevolution.sdk.util.SVector;

public class Ray {
    private final SVector pos;
    private SVector dir;

    Ray(SVector pos, double angle) {
        this.pos = pos;
        this.dir = SVector.fromAngle(angle);
    }

    public void setAngle(double angle) {
        dir = SVector.fromAngle(angle);
    }

    public void lookAt(double x, double y) {
        x = x - pos.x();
        y = y - pos.y();
        dir = new SVector(x, y)
                .normalized();
    }

    public SVector cast(Boundary wall) {
        double x1 = wall.getA().x();
        double y1 = wall.getA().y();
        double x2 = wall.getB().x();
        double y2 = wall.getB().y();

        double x3 = this.pos.x();
        double y3 = this.pos.y();
        double x4 = this.pos.x() + this.dir.x();
        double y4 = this.pos.y() + this.dir.y();

        double den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (den == 0) {
            return null;
        }

        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / den;
        double u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / den;
        if (t > 0 && t < 1 && u > 0) {
            return new SVector(x1 + t * (x2 - x1), y1 + t * (y2 - y1));
        } else {
            return null;
        }
    }

    public SVector getDirection() {
        return new SVector(dir);
    }
}
