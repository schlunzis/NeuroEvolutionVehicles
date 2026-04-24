package org.schlunzis.neuroevolution.model;

import java.util.concurrent.ThreadLocalRandom;

public record Genotype(
        double mutationRate,
        Brain brain
) {

    private static final double LEARNING_RATE = 0.001;

    public Genotype mutate() {
        double z = ThreadLocalRandom.current().nextGaussian();
        double newMutationRate = mutationRate * Math.exp(LEARNING_RATE * z);
        return new Genotype(newMutationRate, brain.mutate(mutationRate));
    }

    public Genotype copy() {
        return new Genotype(mutationRate, brain.copy());
    }

}
