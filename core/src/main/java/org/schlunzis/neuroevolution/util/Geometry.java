package org.schlunzis.neuroevolution.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.schlunzis.neuroevolution.sdk.util.SVector;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Geometry {

    public static boolean intersectsRotatedRectLine(
            double cx, double cy,
            double width, double height,
            double theta,
            double ax, double ay,
            double bx, double by) {

        double hx = width * 0.5;
        double hy = height * 0.5;

        // 1) Move line into rectangle-centered coordinates
        double ax0 = ax - cx, ay0 = ay - cy;
        double bx0 = bx - cx, by0 = by - cy;

        // 2) Rotate by -theta
        double c = Math.cos(theta);
        double s = Math.sin(theta);

        double alx = c * ax0 + s * ay0;
        double aly = -s * ax0 + c * ay0;
        double blx = c * bx0 + s * by0;
        double bly = -s * bx0 + c * by0;

        // 3) Segment vs AABB [-hx,hx] x [-hy,hy]
        double dx = blx - alx;
        double dy = bly - aly;

        double tMin = 0.0;
        double tMax = 1.0;

        // X slab
        if (Math.abs(dx) < 1e-12) {
            if (alx < -hx || alx > hx) return false;
        } else {
            double tx1 = (-hx - alx) / dx;
            double tx2 = (hx - alx) / dx;
            if (tx1 > tx2) {
                double tmp = tx1;
                tx1 = tx2;
                tx2 = tmp;
            }
            tMin = Math.max(tMin, tx1);
            tMax = Math.min(tMax, tx2);
            if (tMin > tMax) return false;
        }

        // Y slab
        if (Math.abs(dy) < 1e-12) {
            if (aly < -hy || aly > hy) return false;
        } else {
            double ty1 = (-hy - aly) / dy;
            double ty2 = (hy - aly) / dy;
            if (ty1 > ty2) {
                double tmp = ty1;
                ty1 = ty2;
                ty2 = tmp;
            }
            tMin = Math.max(tMin, ty1);
            tMax = Math.min(tMax, ty2);
            if (tMin > tMax) return false;
        }

        return true;
    }

    public static double pldistance(SVector p1, SVector p2, double x, double y) {
        double num = Math.abs((p2.y() - p1.y()) * x - (p2.x() - p1.x()) * y + p2.x() * p1.y() - p2.y() * p1.x());
        double den = p1.dist(p2);
        return num / den;
    }

}
