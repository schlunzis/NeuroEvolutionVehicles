package org.schlunzis.neuroevolution.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.neuroevolution.sdk.util.Boundary;
import org.schlunzis.neuroevolution.sdk.util.SVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.schlunzis.neuroevolution.sdk.Constants.MAX_SPEED;
import static org.schlunzis.neuroevolution.sdk.util.MathUtils.map;

@Slf4j
@Getter
public class Vehicle {

    private final Genotype genotype;
    private final Random random;
    @Setter
    private UUID id;
    private double maxForce = 0.1 / 400d;
    private double crashDistance = 5 / 400d;
    private double scale = 10 / 800d;
    @Getter
    private double vehicleWidth = 1 * scale;
    @Getter
    private double vehicleHeight = 104d / 72d * scale;
    private double sight = 1 / 4d;
    private int lifespan = 35;
    private int lifeCounter;
    private SVector pos;
    private SVector vel;
    private SVector acc;
    private SVector startVel;
    private int checkPointFitness;
    private int lapFitness;
    private double fitness;
    @Setter
    private double proportionalFitness;
    private boolean dead;
    private ArrayList<Ray> rays;
    private int lapCount;
    private int checkpointIndex;

    /**
     *
     * @param start
     * @param startVel represents the initial facing. Should be perpendicular to
     *                 the first checkpoint
     */
    public Vehicle(SVector start, SVector startVel, Genotype genotype) {
        id = UUID.randomUUID();
        random = new Random();
        checkPointFitness = 0;
        lapFitness = 0;
        lapCount = 0;
        lifeCounter = 0;
        dead = false;
        checkpointIndex = 0;
        pos = new SVector(start);

        double d2 = 1 / 400d;
        if (startVel == null) {
            double x = random.nextDouble(-d2, d2);
            double y = random.nextDouble(-d2, d2);
            vel = new SVector(x, y);
        } else
            vel = startVel.normalized().mult(1 / 400d);

        this.startVel = new SVector(vel);
        acc = new SVector();
        rays = new ArrayList<>();
        for (int a = -45; a <= 45; a += 15) {
            rays.add(new Ray(pos, Math.toRadians(a)));
        }

        if (genotype != null) {
            this.genotype = genotype.copy();
        } else
            this.genotype = new Genotype(0.1, new Brain(rays.size())); // TODO get starting mutation rate from somewhere else
    }

    public Vehicle(SVector start) {
        this(start, null, null);
    }

    static boolean intersectsRotatedRectLine(
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

    public Vehicle mutate() {
        return new Vehicle(pos, startVel, genotype.mutate());
    }

    public SVector getStartVelocity() {
        return new SVector(startVel);
    }

    public void setStartVelocity(SVector vel) {
        this.startVel = new SVector(vel);
    }

    public void applyForce(SVector force) {
        acc = acc.add(force);
    }

    public void look(List<Boundary> walls) {
        double[] inputs = new double[rays.size()];
        for (int i = 0; i < rays.size(); i++) {
            Ray ray = rays.get(i);
            double rec = sight;
            for (Boundary wall : walls) {
                SVector pt = ray.cast(wall);
                if (pt != null) {
                    double d = pos.dist(pt);
                    if (d < rec)
                        rec = d;
                }
            }
            inputs[i] = map(rec, 0, sight, 1, 0);
        }

        for (Boundary wall : walls) {
            if (intersectsRotatedRectLine(pos.x(), pos.y(), vehicleWidth, vehicleHeight, vel.rawAngle(),
                    wall.getA().x(), wall.getA().y(), wall.getB().x(), wall.getB().y())) {
                dead = true;
                return;
            }
        }

        Brain.Outputs output = genotype.brain().query(inputs, vel.mag());
        double angle = output.desiredAngle();
        double speed = output.desiredSpeed();
        angle += vel.rawAngle();
        SVector steering = SVector.fromAngle(angle)
                .withMag(speed)
                .sub(vel);
        applyForce(steering);
    }

    public void update() {
        if (!dead) {
            pos = pos.add(vel);
            vel = vel.add(acc)
                    .withLimit(MAX_SPEED);
            acc = acc.mult(0);
            lifeCounter++;
            if (lifeCounter > lifespan) {
                dead = true;
            }
            rays = new ArrayList<>();
            for (int a = -45; a <= 45; a += 15) {
                rays.add(new Ray(this.pos, Math.toRadians(a) + this.vel.rawAngle()));
            }
        }
    }

    public void check(List<Boundary> checkpoints) {
        Boundary goal = checkpoints.get(checkpointIndex);
        double d = pldistance(goal.getA(), goal.getB(), pos.x(), pos.y());
        if (d < crashDistance) {
            checkpointIndex = ++checkpointIndex % checkpoints.size();
            if (checkpointIndex == 0) {
                lapCount++;
                lapFitness++;
                if (lapCount % 2 == 0) {
                    lifespan--;
                }
            }
            checkPointFitness++;
            lifeCounter = 0;
        }
    }

    public void calculateFitness(List<Boundary> checkpoints) {
        double newFitness = lapFitness;
        newFitness += (checkPointFitness % checkpoints.size()) / (double) checkpoints.size();

        Boundary goal = checkpoints.get(checkpointIndex);
        double d = pldistance(goal.getA(), goal.getB(), pos.x(), pos.y());
        newFitness += map(d, 0, 1, 0.0001, 0);

        if (newFitness < fitness) {
            System.out.println("Are you dumb?");
        }
        this.fitness = newFitness;
    }

    private double pldistance(SVector p1, SVector p2, double x, double y) {
        double num = Math.abs((p2.y() - p1.y()) * x - (p2.x() - p1.x()) * y + p2.x() * p1.y() - p2.y() * p1.x());
        double den = p1.dist(p2);
        return num / den;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Vehicle v && v.id.equals(id);
    }

    public Vehicle copyWithPos(SVector pos) {
        Vehicle copy = new Vehicle(pos, startVel, genotype.copy());
        copy.setId(id);
        return copy;
    }
}
