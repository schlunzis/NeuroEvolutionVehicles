package org.schlunzis.neuroevolution.sdk.util;

/// A 3D Vector providing common operations.
///
/// This vector can also be used as a 2D vector by ignoring the z dimension.
///
/// @param x the first dimensions
/// @param y the second dimensions
/// @param z the third dimensions
public record SVector(double x, double y, double z) {

    /// Constructs a new vector where all dimensions are set to zero.
    public SVector() {
        this(0, 0);
    }

    /// Constructs a new vector with the given x and y dimensions, and z set to zero.
    ///
    /// @param x the first dimension
    /// @param y the second dimension
    public SVector(double x, double y) {
        this(x, y, 0);
    }

    /// Constructs a new vector with the same dimensions as the given vector.
    /// All references to the given vector are lost.
    ///
    /// @param other the vector to copy
    public SVector(SVector other) {
        this(
                other.x(),
                other.y(),
                other.z()
        );
    }

    /// Convenience method for operation chaining.
    ///
    /// @param other the vector to add to this vector
    /// @return a new vector that is the sum of this vector and the given vector
    /// @see SVector#add(SVector, SVector)
    public SVector add(SVector other) {
        return add(this, other);
    }

    /// Convenience method for operation chaining.
    ///
    /// @param other the vector to subtract from this vector
    /// @return a new vector that is the difference of this vector and the given vector
    /// @see SVector#sub(SVector, SVector)
    public SVector sub(SVector other) {
        return sub(this, other);
    }

    /// Convenience method for operation chaining.
    ///
    /// @param factor the factor to multiply this vector with
    /// @return a new vector that is the product of this vector and the given factor
    /// @see SVector#mult(SVector, double)
    public SVector mult(double factor) {
        return mult(this, factor);
    }

    /// Convenience method for operation chaining.
    ///
    /// @param factor the factor to divide this vector by
    /// @return a new vector that is the quotient of this vector and the given factor
    /// @see SVector#div(SVector, double)
    public SVector div(double factor) {
        return div(this, factor);
    }

    /// Adds two vectors together componentwise and returns the result as a new vector.
    ///
    /// `a + b`
    ///
    /// @param a the first vector to add
    /// @param b the second vector to add
    /// @return a new vector that is the sum of `a` and `b`
    public static SVector add(SVector a, SVector b) {
        return new SVector(a.x() + b.x(), a.y() + b.y(), a.z() + b.z());
    }

    /// Subtracts `b` from `a` componentwise and returns the result as a new vector.
    ///
    /// `a - b`
    ///
    /// @param a the vector to subtract from
    /// @param b the vector to subtract
    /// @return a new vector that is the difference of `a` and `b`
    public static SVector sub(SVector a, SVector b) {
        return new SVector(a.x() - b.x(), a.y() - b.y(), a.z() - b.z());
    }

    /// Multiplies the given vector with the given factor componentwise and returns the result as a new vector.
    ///
    /// `v * factor`
    ///
    /// @param v      the vector to multiply
    /// @param factor the factor to multiply the vector with
    /// @return a new vector that is the product of `v` and `factor`
    public static SVector mult(SVector v, double factor) {
        return new SVector(v.x() * factor, v.y() * factor, v.z() * factor);
    }

    /// Divides the given vector by the given factor componentwise and returns the result as a new vector.
    ///
    /// `v / factor`
    ///
    /// @param v      the vector to divide
    /// @param factor the factor to divide the vector by
    /// @return a new vector that is the quotient of `v` and `factor`
    public static SVector div(SVector v, double factor) {
        return new SVector(v.x() / factor, v.y() / factor, v.z() / factor);
    }

    /// Returns the magnitude (the length) of the vector.
    ///
    /// @return the magnitude of the vector
    public double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    ///  Returns a new vector pointing in the same direction as this one, but with the given magnitude.
    ///
    /// If the current magnitude is zero, the resulting vector will also have the magnitude of zero,
    /// since the direction is undefined.
    ///
    /// @param mag the magnitude of the new vector
    /// @return a new vector with the same direction as this one, but with the given magnitude
    /// @throws IllegalArgumentException if the given magnitude is negative
    public SVector withMag(double mag) {
        if (mag < 0) throw new IllegalArgumentException("Magnitude cannot be negative");

        double currentMag = mag();
        if (currentMag != 0) {
            return this.mult(mag / currentMag);
        } else {
            return this;
        }
    }

    /// Returns a new vector pointing in the same direction as this one, but with a magnitude of 1.
    ///
    /// If the current magnitude is zero, the resulting vector will also have a magnitude of zero,
    /// since the direction is undefined.
    ///
    /// @return a new vector with the same direction as this one, but with a magnitude of 1
    public SVector normalized() {
        double m = mag();
        if (m != 0) {
            return new SVector(x / m, y / m, z / m);
        } else {
            return this;
        }
    }

    /// Returns a new vector with the same direction as this one, but with a magnitude of at most the given limit.
    /// If the current magnitude is less than or equal to the limit, the resulting vector will be the same as this one.
    ///
    /// @param limit the maximum magnitude of the new vector
    /// @return a new vector with the same direction as this one, but with a magnitude of at most the given limit
    /// @throws IllegalArgumentException if the given limit is negative
    public SVector withLimit(double limit) {
        if (limit < 0) throw new IllegalArgumentException("Limit cannot be negative");

        double mag = mag();
        if (mag > limit) {
            return this.withMag(limit);
        }
        return this;
    }

    /// Returns a new vector pointing in the direction of the given angle, with a magnitude of 1.
    /// The angle is measured in radians, where the following vectors are created:
    ///
    /// | Angle (radians) | Vector (x, y) |
    /// |-----------------|--------|
    /// | -PI | (-1, 0) |
    /// | -(PI / 2) | (0, -1) |
    /// | 0 | (1, 0) |
    /// | PI / 2 | (0, 1) |
    /// | PI | (-1, 0) |
    /// | 3 * (PI / 2) | (0, -1) |
    ///
    /// z is always zero.
    ///
    /// @param angle the angle of the new vector in radians
    /// @return a new vector pointing in the direction of the given angle, with a magnitude of 1
    public static SVector fromAngle(double angle) {
        return new SVector(Math.cos(angle), Math.sin(angle));
    }

    /// Returns the angle of this vector in radians.
    /// This angle is guarantied to be positive. To get raw angles, which may be negative, use [SVector#rawAngle()].
    ///
    /// Angles are defined as in the [SVector#fromAngle(double)] method.
    /// For a vector constructed with `SVector.fromAngle(0)` this method may return `2 * PI - epsilon`.
    ///
    /// z is ignored.
    ///
    /// @return the angle of this vector in radians, guaranteed to be positive
    public double angle() {
        double angle = Math.atan2(y, x);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
        return angle;
    }

    /// Returns the angle of this vector in radians.
    ///
    /// Angles are defined as in the [SVector#fromAngle(double)] method.
    ///
    /// z is ignored.
    ///
    /// @return the angle of this vector in radians
    public double rawAngle() {
        return Math.atan2(y, x);
    }

    public SVector rotate(double angle) {
        double x = x() * Math.cos(angle) - y() * Math.sin(angle);
        double y = x() * Math.sin(angle) + y() * Math.cos(angle);
        return new SVector(x, y, z());
    }

    public static double dist(SVector v1, SVector v2) {
        return Math.sqrt(Math.pow(v1.x() - v2.x(), 2) + Math.pow(v1.y() - v2.y(), 2));
    }

    public static double distSq(SVector v1, SVector v2) {
        return Math.pow(v1.x() - v2.x(), 2) + Math.pow(v1.y() - v2.y(), 2);
    }

    public SVector cross(SVector v) {
        return new SVector(
                y() * v.z() - v.y() * z(),
                z() * v.x() - v.z() * x(),
                x() * v.y() - v.x() * y()
        );
    }
}
