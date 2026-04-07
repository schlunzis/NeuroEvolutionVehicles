package org.schlunzis.neuroevolution.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.neuroevolution.sdk.util.Boundary;
import org.schlunzis.neuroevolution.sdk.util.SVector;
import org.schlunzis.zis.ai.nn.GeneticNeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Getter
public class Vehicle {

    @Setter
    private UUID id;

    private double maxSpeed = 5 / 400d;
    private double maxForce = 0.2 / 400d;

    private double crashDistance = 5 / 400d;
    private double scale = 10 / 800d;
    @Getter
    private double vehicleWidth = 1 * scale;
    @Getter
    private double vehicleHeight = 2.5 * scale;

    private double sight = 1 / 4d;
    private int lifespan = 35;
    private int lifeCounter;

    private SVector pos;
    private SVector vel;
    private SVector acc;
    private SVector startVel;

    private int checkPointFitness;
    private int lapFitness;
    @Setter
    private double fitness;
    private boolean dead;

    private ArrayList<Ray> rays;

    private int lapCount;
    private int checkpointIndex;

    private GeneticNeuralNetwork brain;

    private Random random;

    /**
     *
     * @param start
     * @param startVel     represents the initial facing. Should be perpendicular to
     *                     the first checkpoint
     * @param nn
     * @param mutationRate
     */
    public Vehicle(SVector start, SVector startVel, GeneticNeuralNetwork nn, double mutationRate) {
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

        if (nn != null) {
            brain = nn.copy();
        } else
            brain = new GeneticNeuralNetwork(0, mutationRate, rays.size(), 2, rays.size() * 2);
    }

    public Vehicle(SVector start, double mutationRate) {
        this(start, null, null, mutationRate);
    }

    public void mutate() {
        brain.mutate();
    }

    public SVector getStartVelocity() {
        return new SVector(startVel);
    }

    public void setStartVelocity(SVector vel) {
        this.startVel = new SVector(vel);
    }

    public GeneticNeuralNetwork getBrain() {
        return brain.copy();
    }

    public void setBrain(GeneticNeuralNetwork nn) {
        brain = nn;
    }

    public void applyForce(SVector force) {
        acc = acc.add(force);
    }

    public void look(List<Boundary> walls) {
        double[][] inputs = new double[1][rays.size()];
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
            if (rec < crashDistance) { // vehicle crashed in the wall
                dead = true;
                return;
            }
            inputs[0][i] = map(rec, 0, sight, 1, 0);
        }
        double[][] output = brain.query(inputs).toArray();
        double angle = map(output[0][0], 0, 1, -Math.PI, Math.PI);
        double speed = map(output[1][0], 0, 1, 0, maxSpeed);
        angle += vel.rawAngle();
        SVector steering = SVector.fromAngle(angle)
                .withMag(speed)
                .sub(vel)
                .withLimit(maxForce);
        applyForce(steering);
    }

    public void update() {
        if (!dead) {
            pos = pos.add(vel);
            vel = vel.add(acc)
                    .withLimit(maxSpeed);
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

    public void calculateFitness() {
        fitness = Math.pow(2, checkPointFitness);
    }

    private double pldistance(SVector p1, SVector p2, double x, double y) {
        double num = Math.abs((p2.y() - p1.y()) * x - (p2.x() - p1.x()) * y + p2.x() * p1.y() - p2.y() * p1.x());
        double den = p1.dist(p2);
        return num / den;
    }

    /**
     * Taken from Processing
     *
     * @param value
     * @param inputMin
     * @param inputMax
     * @param outputMin
     * @param outputMax
     * @return
     */
    private double map(double value, double inputMin, double inputMax, double outputMin, double outputMax) {
        return outputMin + (outputMax - outputMin) * ((value - inputMin) / (inputMax - inputMin));
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Vehicle v && v.id.equals(id);
    }
}
