package org.schlunzis.neuroevolution.sdk.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SVectorTest {

    @Test
    void testConstructor() {
        SVector none = new SVector();
        assertEquals(0, none.x());
        assertEquals(0, none.y());
        assertEquals(0, none.z());

        SVector two = new SVector(1, 2);
        assertEquals(1, two.x());
        assertEquals(2, two.y());
        assertEquals(0, two.z());

        SVector three = new SVector(1, 2, 3);
        assertEquals(1, three.x());
        assertEquals(2, three.y());
        assertEquals(3, three.z());
    }

    @Test
    void testAdd() {
        SVector a = new SVector(1, 2, 3);
        SVector b = new SVector(4, 5, 6);

        SVector cInstance = a.add(b);
        SVector cStatic = SVector.add(a, b);

        assertEquals(5, cInstance.x());
        assertEquals(7, cInstance.y());
        assertEquals(9, cInstance.z());
        assertEquals(5, cStatic.x());
        assertEquals(7, cStatic.y());
        assertEquals(9, cStatic.z());
    }

    @Test
    void testSub() {
        SVector a = new SVector(1, 2, 3);
        SVector b = new SVector(6, 8, 10);

        SVector cInstance = a.sub(b);
        SVector cStatic = SVector.sub(a, b);

        assertEquals(-5, cInstance.x());
        assertEquals(-6, cInstance.y());
        assertEquals(-7, cInstance.z());
        assertEquals(-5, cStatic.x());
        assertEquals(-6, cStatic.y());
        assertEquals(-7, cStatic.z());
    }

    @Test
    void testMult() {
        SVector v = new SVector(1, 2, 3);
        double factor = 2;

        SVector rInstance = v.mult(factor);
        SVector rStatic = SVector.mult(v, factor);

        assertEquals(2, rInstance.x());
        assertEquals(4, rInstance.y());
        assertEquals(6, rInstance.z());
        assertEquals(2, rStatic.x());
        assertEquals(4, rStatic.y());
        assertEquals(6, rStatic.z());
    }

    @Test
    void testDiv() {
        SVector v = new SVector(2, 4, 6);
        double factor = 2;

        SVector rInstance = v.div(factor);
        SVector rStatic = SVector.div(v, factor);

        assertEquals(1, rInstance.x());
        assertEquals(2, rInstance.y());
        assertEquals(3, rInstance.z());
        assertEquals(1, rStatic.x());
        assertEquals(2, rStatic.y());
        assertEquals(3, rStatic.z());
    }

    @Test
    void testMag() {
        SVector a = new SVector(1, 0, 0);
        SVector b = new SVector(0, 1, 0);
        SVector c = new SVector(0, 0, 1);
        SVector d = new SVector(1, 1, 1);
        SVector e = new SVector(2, -3, 5);

        assertEquals(1, a.mag());
        assertEquals(1, b.mag());
        assertEquals(1, c.mag());
        assertEquals(Math.sqrt(3), d.mag());
        assertEquals(Math.sqrt(38), e.mag());
    }

    @Test
    void testWithMagNegative() {
        assertThrows(IllegalArgumentException.class, () -> new SVector(1, 2, 3).withMag(-1));
    }

    @Test
    void testWithMagZero() {
        SVector a = new SVector(1, 2, 3).withMag(0);
        assertEquals(0, a.mag());
        assertEquals(new SVector(0, 0, 0), a);

        SVector b = new SVector(0, 0, 0).withMag(2);
        assertEquals(0, b.mag());
        assertEquals(new SVector(0, 0, 0), b);
    }

    @Test
    void testWithMagSingleDim() {
        SVector a = new SVector(1, 0, 0).withMag(2);
        SVector b = new SVector(0, 1, 0).withMag(2);
        SVector c = new SVector(0, 0, 1).withMag(2);

        assertEquals(2, a.mag());
        assertEquals(new SVector(2, 0, 0), a);
        assertEquals(2, b.mag());
        assertEquals(new SVector(0, 2, 0), b);
        assertEquals(2, c.mag());
        assertEquals(new SVector(0, 0, 2), c);
    }

    @Test
    void testWithMagMultiDim() {
        SVector a = new SVector(1, 2, 3).withMag(2);
        SVector b = new SVector(-1, -4, 5).withMag(3);

        assertEquals(2, a.mag());
        assertEquals(new SVector(2 / Math.sqrt(14), 4 / Math.sqrt(14), 6 / Math.sqrt(14)), a);
        assertEquals(3, b.mag(), 0.000001);
        assertEquals(-3 / Math.sqrt(42), b.x(), 0.000001);
        assertEquals(-12 / Math.sqrt(42), b.y(), 0.000001);
        assertEquals(15 / Math.sqrt(42), b.z(), 0.000001);
    }

    @Test
    void testNormalized() {
        SVector a = new SVector(0, 0, 0).normalized();
        SVector b = new SVector(1, 0, 0).normalized();
        SVector c = new SVector(0, 1, 0).normalized();
        SVector d = new SVector(0, 0, 1).normalized();
        SVector e = new SVector(1, 2, 3).normalized();

        assertEquals(new SVector(0, 0, 0), a);
        assertEquals(new SVector(1, 0, 0), b);
        assertEquals(new SVector(0, 1, 0), c);
        assertEquals(new SVector(0, 0, 1), d);
        double magE = Math.sqrt(14);
        assertEquals(1, e.mag());
        assertEquals(1 / magE, e.x(), 0.000001);
        assertEquals(2 / magE, e.y(), 0.000001);
        assertEquals(3 / magE, e.z(), 0.000001);
    }

    @Test
    void testWithLimitNegative() {
        assertThrows(IllegalArgumentException.class, () -> new SVector(1, 2, 3).withLimit(-1));
    }

    @Test
    void testWithLimitZero() {
        SVector a = new SVector(1, 2, 3).withLimit(0);
        assertEquals(0, a.mag());
        assertEquals(new SVector(0, 0, 0), a);

        SVector b = new SVector(0, 0, 0).withLimit(2);
        assertEquals(0, b.mag());
        assertEquals(new SVector(0, 0, 0), b);
    }

    @Test
    void testWithLimitSingleDim() {
        SVector a = new SVector(1, 0, 0).withLimit(2);
        SVector b = new SVector(0, 1, 0).withLimit(2);
        SVector c = new SVector(0, 0, 1).withLimit(2);
        SVector d = new SVector(1, 0, 0).withLimit(0.5);
        SVector e = new SVector(0, 1, 0).withLimit(0.5);
        SVector f = new SVector(0, 0, 1).withLimit(0.5);

        assertEquals(1, a.mag());
        assertEquals(new SVector(1, 0, 0), a);
        assertEquals(1, b.mag());
        assertEquals(new SVector(0, 1, 0), b);
        assertEquals(1, c.mag());
        assertEquals(new SVector(0, 0, 1), c);
        assertEquals(0.5, d.mag());
        assertEquals(new SVector(0.5, 0, 0), d);
        assertEquals(0.5, e.mag());
        assertEquals(new SVector(0, 0.5, 0), e);
        assertEquals(0.5, f.mag());
        assertEquals(new SVector(0, 0, 0.5), f);
    }

    @Test
    void testWithLimitMultiDim() {
        SVector a = new SVector(1, 2, 3).withLimit(2);
        SVector b = new SVector(-1, -4, 5).withLimit(3);

        assertEquals(2, a.mag());
        assertEquals(new SVector(2 / Math.sqrt(14), 4 / Math.sqrt(14), 6 / Math.sqrt(14)), a);
        assertEquals(3, b.mag(), 0.000001);
        assertEquals(-3 / Math.sqrt(42), b.x(), 0.000001);
        assertEquals(-12 / Math.sqrt(42), b.y(), 0.000001);
        assertEquals(15 / Math.sqrt(42), b.z(), 0.000001);
    }

    @Test
    void testFromAngle() {
        SVector a = SVector.fromAngle(0);
        SVector b = SVector.fromAngle(Math.PI / 2);
        SVector c = SVector.fromAngle(Math.PI);
        SVector d = SVector.fromAngle(3 * (Math.PI / 2));
        SVector e = SVector.fromAngle(2 * Math.PI);
        SVector f = SVector.fromAngle(-1);
        SVector g = SVector.fromAngle(-(Math.PI / 2));
        SVector h = SVector.fromAngle(-Math.PI);

        assertEquals(1, a.x(), 0.000001);
        assertEquals(0, a.y(), 0.000001);
        assertEquals(0, a.z());
        assertEquals(0, b.x(), 0.000001);
        assertEquals(1, b.y(), 0.000001);
        assertEquals(0, b.z());
        assertEquals(-1, c.x(), 0.000001);
        assertEquals(0, c.y(), 0.000001);
        assertEquals(0, c.z());
        assertEquals(0, d.x(), 0.000001);
        assertEquals(-1, d.y(), 0.000001);
        assertEquals(0, d.z());
        assertEquals(1, e.x(), 0.000001);
        assertEquals(0, e.y(), 0.000001);
        assertEquals(0, e.z());
        assertEquals(Math.cos(-1), f.x(), 0.000001);
        assertEquals(Math.sin(-1), f.y(), 0.000001);
        assertEquals(0, f.z());
        assertEquals(0, g.x(), 0.000001);
        assertEquals(-1, g.y(), 0.000001);
        assertEquals(0, g.z());
        assertEquals(-1, h.x(), 0.000001);
        assertEquals(0, h.y(), 0.000001);
        assertEquals(0, h.z());
    }

    @Test
    void testAngle() {
        SVector a = SVector.fromAngle(0);
        SVector b = SVector.fromAngle(Math.PI / 2);
        SVector c = SVector.fromAngle(Math.PI);
        SVector d = SVector.fromAngle(3 * (Math.PI / 2));
        SVector e = SVector.fromAngle(2 * Math.PI);
        SVector f = SVector.fromAngle(-1);

        assertEquals(0, a.angle(), 0.000001);
        assertEquals(Math.PI / 2, b.angle(), 0.000001);
        assertEquals(Math.PI, c.angle(), 0.000001);
        assertEquals(3 * (Math.PI / 2), d.angle(), 0.000001);
        assertEquals(Math.PI * 2, e.angle(), 0.000001);
        assertEquals(2 * Math.PI - 1, f.angle(), 0.000001);
    }

    @Test
    void testRawAngle() {
        SVector a = SVector.fromAngle(0);
        SVector b = SVector.fromAngle(Math.PI / 2);
        SVector c = SVector.fromAngle(Math.PI);
        SVector d = SVector.fromAngle(3 * (Math.PI / 2));
        SVector e = SVector.fromAngle(2 * Math.PI);
        SVector f = SVector.fromAngle(-1);

        assertEquals(0, a.rawAngle(), 0.000001);
        assertEquals(Math.PI / 2, b.rawAngle(), 0.000001);
        assertEquals(Math.PI, c.rawAngle(), 0.000001);
        assertEquals(-(Math.PI / 2), d.rawAngle(), 0.000001);
        assertEquals(0, e.rawAngle(), 0.000001);
        assertEquals(-1, f.rawAngle(), 0.000001);
    }

    @Test
    void testRotateZero() {
        SVector v = new SVector(0, 0, 0).rotate(Math.PI / 2);

        assertEquals(0, v.x());
        assertEquals(0, v.y());
        assertEquals(0, v.z());
    }

    @Test
    void testRotate() {
        SVector a = new SVector(1, 0, 0).rotate(0);
        SVector b = new SVector(1, 0, 0).rotate(Math.PI / 2);
        SVector c = new SVector(1, 2, 3).rotate(Math.PI);

        assertEquals(1, a.x(), 0.000001);
        assertEquals(0, a.y(), 0.000001);
        assertEquals(0, a.z());
        assertEquals(0, b.x(), 0.000001);
        assertEquals(1, b.y(), 0.000001);
        assertEquals(0, b.z());
        assertEquals(-1, c.x(), 0.000001);
        assertEquals(-2, c.y(), 0.000001);
        assertEquals(3, c.z());
    }

    @Test
    void testDist() {
        SVector a = new SVector(1, 0.5, 1);
        SVector b = new SVector(2, 0.5, 2);

        double distance = a.dist(b);

        assertEquals(Math.sqrt(2), distance, 0.000001);
    }

    @Test
    void testDistSq() {
        SVector a = new SVector(1, 0.5, 1);
        SVector b = new SVector(2, 0.5, 2);

        double distance = a.distSq(b);

        assertEquals(2, distance, 0.000001);
    }

    @Test
    void testCross() {
        SVector a = new SVector(1, 0, 0);
        SVector b = new SVector(0, 1, 0);

        SVector cross = a.cross(b);

        assertEquals(0, cross.x(), 0.000001);
        assertEquals(0, cross.y(), 0.000001);
        assertEquals(1, cross.z(), 0.000001);
    }

}
