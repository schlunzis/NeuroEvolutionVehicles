package org.schlunzis.neuroevolution.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.neuroevolution.sdk.util.Boundary;
import org.schlunzis.neuroevolution.sdk.util.SVector;
import org.schlunzis.neuroevolution.util.Geometry;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static org.schlunzis.neuroevolution.sdk.Constants.MAX_SPEED;
import static org.schlunzis.neuroevolution.sdk.util.MathUtils.map;

@Slf4j
@Getter
public class Vehicle {

    private static final int ALPHA = 60;
    private static final int DELTA = 15;
    private static final int RAY_COUNT = ((ALPHA * 2) / DELTA) + 1;
    private static final double CRASH_DISTANCE = 5 / 400d;
    private static final double SCALE = 10 / 800d;
    @Getter
    private static final double VEHICLE_WIDTH = 1 * SCALE;
    @Getter
    private static final double VEHICLE_HEIGHT = 104d / 72d * SCALE;
    private static final double SIGHT = 1 / 4d;
    private static final double MAX_FORCE = 0.001;
    private static final int STARTING_LIFESPAN = 20;

    private final Genotype genotype;
    private final Random random;
    private final Ray[] rays = new Ray[RAY_COUNT];
    private final SVector startVel;
    @Setter
    private UUID id;
    private int lifespan = STARTING_LIFESPAN;
    private int lifeCounter;
    private SVector pos;
    private SVector vel;
    private SVector acc;
    private int checkPointFitness;
    private int lapFitness;
    private double fitness;
    @Setter
    private double proportionalFitness;
    private boolean dead;
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
        pos = start;

        double d2 = 1 / 400d;
        if (startVel == null) {
            double x = random.nextDouble(-d2, d2);
            double y = random.nextDouble(-d2, d2);
            vel = new SVector(x, y);
        } else
            vel = startVel.normalized().mult(1 / 400d);

        this.startVel = vel;
        acc = SVector.zero();
        updateRays();

        if (genotype != null) {
            this.genotype = genotype.copy();
        } else
            this.genotype = new Genotype(0.1, new Brain(rays.length)); // TODO get starting mutation rate from somewhere else
    }

    public Vehicle(SVector start) {
        this(start, null, null);
    }

    public Vehicle mutate() {
        return new Vehicle(pos, startVel, genotype.mutate());
    }

    public void applyForce(SVector force) {
        acc = acc.add(force)
                .withLimit(MAX_FORCE);
    }

    public void look(List<Boundary> walls) {
        double[] inputs = new double[rays.length];
        for (int i = 0; i < rays.length; i++) {
            Ray ray = rays[i];
            double rec = SIGHT;
            for (Boundary wall : walls) {
                SVector pt = ray.cast(wall);
                if (pt != null) {
                    double d = pos.dist(pt);
                    if (d < rec)
                        rec = d;
                }
            }
            inputs[i] = map(rec, 0, SIGHT, 1, 0);
        }

        double velAngle = vel.rawAngle();
        for (Boundary wall : walls) {
            if (Geometry.intersectsRotatedRectLine(pos.x(), pos.y(), VEHICLE_WIDTH, VEHICLE_HEIGHT, velAngle,
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
            acc = SVector.zero();
            lifeCounter++;
            if (lifeCounter > lifespan) {
                dead = true;
            }
            updateRays();
        }
    }

    public void check(List<Boundary> checkpoints) {
        Boundary goal = checkpoints.get(checkpointIndex);
        double d = Geometry.pldistance(goal.getA(), goal.getB(), pos.x(), pos.y());
        if (d < CRASH_DISTANCE) {
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
        double d = Geometry.pldistance(goal.getA(), goal.getB(), pos.x(), pos.y());
        newFitness += map(d, 0, 1, 0.0001, 0);

        this.fitness = newFitness;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vehicle vehicle)) return false;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Vehicle copyWithPos(SVector pos) {
        Vehicle copy = new Vehicle(pos, startVel, genotype.copy());
        copy.setId(id);
        return copy;
    }

    private void updateRays() {
        int index = 0;
        for (int a = -ALPHA; a <= ALPHA; a += DELTA) {
            rays[index] = new Ray(this.pos, Math.toRadians(a) + this.vel.rawAngle());
            index++;
        }
    }
}
