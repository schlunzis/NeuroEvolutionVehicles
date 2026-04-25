package org.schlunzis.neuroevolution.model;

import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.neuroevolution.sdk.track.Track;
import org.schlunzis.neuroevolution.sdk.util.SVector;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class GeneticAlgorithm {

    private final Object trackLock = new Object();
    private double mutationRate = 0.05;
    private int populationSize = 500;
    private int cycles = 1;
    private int generationCount = 0;
    private ArrayList<Vehicle> population;
    private ArrayList<Vehicle> savedVehicles;
    @Getter(onMethod_ = {@Synchronized("trackLock")})
    private Track track;

    private Vehicle prevBest;


    private List<Runnable> newGenerationHooks = new ArrayList<>();

    public GeneticAlgorithm(Track track) {
        this.setTrack(track);
    }

    public void addNewGenerationHook(Runnable r) {
        newGenerationHooks.add(r);
    }

    @Synchronized("trackLock")
    public void setTrack(Track track) {
        this.track = track;
        reset();
    }

    public void reset() {
        generationCount = 0;
        prevBest = null;
        population = new ArrayList<>();
        savedVehicles = new ArrayList<>();

        synchronized (trackLock) {
            track.buildTrack();

            for (int i = 0; i < populationSize; i++) {
                population.add(new Vehicle(track.getStart(), getStartVelocity(), null));
            }
        }
    }

    @Synchronized("trackLock")
    private SVector getStartVelocity() {
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
                    triggerNextGeneration();
                }

            }
        }
        if (population.stream().map(Vehicle::getLapCount).max(Integer::compareTo).orElse(0) >= 5) {
            triggerNextGeneration();
        }
    }

    public void triggerNextGeneration() {
        savedVehicles.addAll(population);
        calculateFitness();
        track.buildTrack();
        nextGeneration();
        generationCount++;
        for (Runnable r : newGenerationHooks)
            r.run();

        log.debug("Generation #{}", generationCount);
    }

    private void nextGeneration() {
        population = new ArrayList<>();
        prevBest = findBestVehicle();
        System.out.println("Best fitness: " + prevBest.getFitness());
        System.out.println("Best proportional fitness: " + prevBest.getProportionalFitness());
        System.out.println("Best mutationRate: " + prevBest.getGenotype().mutationRate());
        population.add(prevBest.copyWithNewStart(track.getStart(), getStartVelocity()));
        for (int i = 1; i < populationSize; i++) {
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
        return best;
    }

    private Vehicle pickOne() {
        int index = 0;
        double r = Math.random();
        while (r > 0) {
            r = r - savedVehicles.get(index).getProportionalFitness();
            index++;
        }
        index--;
        Vehicle v = savedVehicles.get(index);
        Vehicle child = new Vehicle(track.getStart(), getStartVelocity(), v.getGenotype().copy());

        child = child.mutate();
        return child;
    }

    @Synchronized
    private void calculateFitness() throws ArithmeticException {
        for (Vehicle v : savedVehicles)
            v.calculateFitness(track.getCheckpoints());
        double sum = 0;
        for (Vehicle v : savedVehicles)
            sum += v.getFitness();
        if (sum <= 0)
            throw new ArithmeticException("Could not calculate fitness");
        for (Vehicle v : savedVehicles) {
            v.setProportionalFitness(v.getFitness() / sum);
        }
    }

    public void restartWithBrain(Brain brain) {
        reset();
        population.clear();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Vehicle(track.getStart(), getStartVelocity(), new Genotype(mutationRate, brain.copy())));
        }
        for (Runnable r : newGenerationHooks)
            r.run();
    }
}
