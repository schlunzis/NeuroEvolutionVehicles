package org.schlunzis.neuroevolution.model;

import java.util.concurrent.ThreadLocalRandom;

public record Genotype(
        double mutationRate,
        Brain brain
) {

    public Genotype mutate() {
        double z = ThreadLocalRandom.current().nextGaussian();
        double newMutationRate = mutationRate * Math.exp(0.001 * z);
        return new Genotype(newMutationRate, brain.mutate(mutationRate));
    }

    public Genotype copy() {
        return new Genotype(mutationRate, brain.copy());
    }

}
