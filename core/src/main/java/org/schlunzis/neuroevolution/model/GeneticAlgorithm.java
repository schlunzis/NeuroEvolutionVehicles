package org.schlunzis.neuroevolution.model;

import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.neuroevolution.model.track.TrackFactory;
import org.schlunzis.neuroevolution.sdk.track.Track;
import org.schlunzis.neuroevolution.sdk.util.PVector;
import org.schlunzis.neuroevolution.util.Observable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class GeneticAlgorithm extends Observable {

    private static final double MUTATION_RATE = 0.05;
    private static final int POPULATION_SIZE = 100;

    // TODO make modifiable
    private int cycles = 1;

    private int generationCount = 0;

    private List<Vehicle> population;
    private List<Vehicle> savedVehicles;

    private final Object trackLock = new Object();

    @Getter(onMethod_ = {@Synchronized("trackLock")})
    private Track track;

    private Vehicle prevBest;

    @Synchronized("trackLock")
    public void setTrack(Track track) {
        this.track = track;
        reset();
    }

    public GeneticAlgorithm() {
        synchronized (trackLock) {
            this.track = TrackFactory.createTrack(TrackFactory.CONVEX_HULL);
        }
        reset();
    }

    public void reset() {
        generationCount = 0;
        prevBest = null;
        population = new ArrayList<>(POPULATION_SIZE);
        savedVehicles = new ArrayList<>();

        synchronized (trackLock) {
            track.buildTrack();

            for (int i = 0; i < POPULATION_SIZE; i++) {
                population.add(new Vehicle(track.getStart(), getStartVelocity(), null, MUTATION_RATE));
            }
        }
    }

    @Synchronized("trackLock")
    private PVector getStartVelocity() {
        return track.getStartVelocity();
    }

    public synchronized List<Vehicle> getPopulation() {
        return population;
    }

    /**
     * Make method multi-threaded
     */
    public void update() {
        synchronized (this) {
            for (int c = 0; c < cycles; c++) {

                for (Vehicle v : population) {
                    v.look(track.getWalls());
                    v.check(track.getCheckpoints());
                    v.update();

                }

                for (int i = population.size() - 1; i >= 0; i--) {
                    Vehicle v = population.get(i);
                    if (v.isDead()) {
                        savedVehicles.add(population.remove(i));
                    }
                }

                if (population.isEmpty()) {
                    track.buildTrack();
                    nextGeneration();
                    generationCount++;
                    log.debug("Generation #{}", generationCount);
                }
            }
        }
        setChanged();
        notifyAllObservers();
    }

    private void nextGeneration() {
        calculateFitness();
        population = new ArrayList<>();
        prevBest = findBestVehicle();
        population.add(prevBest);
        for (int i = 1; i < POPULATION_SIZE; i++) {
            population.add(pickOne());
        }
        savedVehicles = new ArrayList<>();
    }

    private Vehicle findBestVehicle() {
        double fitness = 0;
        Vehicle best = null;
        for (Vehicle v : savedVehicles) {
            if (v.getFitness() > fitness) {
                fitness = v.getFitness();
                best = v;
            }
        }
        Vehicle v = new Vehicle(track.getStart(), getStartVelocity(), best.getBrain(), MUTATION_RATE);
        v.setId(best.getId());
        return v;
    }

    private Vehicle pickOne() {
        int index = 0;
        double r = Math.random();
        while (r > 0) {
            r = r - savedVehicles.get(index).getFitness();
            index++;
        }
        index--;
        Vehicle v = savedVehicles.get(index);
        Vehicle child = new Vehicle(track.getStart(), getStartVelocity(), v.getBrain(), MUTATION_RATE);

        child.mutate();
        return child;
    }

    @Synchronized
    private void calculateFitness() throws ArithmeticException {
        for (Vehicle v : savedVehicles)
            v.calculateFitness();
        double sum = 0;
        for (Vehicle v : savedVehicles)
            sum += v.getFitness();
        if (sum == 0)
            throw new ArithmeticException("Could not calculate fitness");
        for (Vehicle v : savedVehicles) {
            v.setFitness(v.getFitness() / sum);
        }
    }

}
