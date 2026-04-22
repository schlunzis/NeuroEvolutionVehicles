package org.schlunzis.neuroevolution.model;

import lombok.Getter;
import org.schlunzis.zis.ai.nn.GeneticNeuralNetwork;
import org.schlunzis.zis.ai.nn.Mutators;
import org.schlunzis.zis.math.linear.Matrix;

import java.io.File;
import java.io.IOException;

import static org.schlunzis.neuroevolution.sdk.Constants.MAX_SPEED;
import static org.schlunzis.neuroevolution.sdk.util.MathUtils.map;
import static org.schlunzis.zis.ai.nn.NeuralNetwork.deserialize;


public class Brain {

    private static final int MEMORY_NODES = 5;

    private final GeneticNeuralNetwork network;
    @Getter
    private Outputs lastOutput = new Outputs();

    public Brain(int rayCount) {
        this(new GeneticNeuralNetwork(0, 0, rayCount + 1 + 2 + 1 + MEMORY_NODES, 2 + MEMORY_NODES, rayCount * 2));
    }

    public Brain(GeneticNeuralNetwork network) {
        this.network = network;
    }

    public static Brain load(File location) throws IOException, ClassNotFoundException {
        return new Brain((GeneticNeuralNetwork) deserialize(location));
    }

    public Brain copy() {
        return new Brain(network.copy());
    }

    public Brain mutate(double mutationRate) {
        return new Brain(network.copy().mutate(mutationRate, Mutators.gaussian(), Mutators.gaussian()));
    }

    public Outputs query(double[] rays, double currentSpeed) {
        Matrix inputs = new Matrix(rays.length + 1 + 2 + 1 + 5, 1);
        for (int i = 0; i < rays.length; i++) {
            inputs.set(i, 0, rays[i]);
        }
        inputs.set(rays.length, 0, currentSpeed);

        inputs.set(rays.length + 1, 0, lastOutput.desiredAngle());
        inputs.set(rays.length + 2, 0, lastOutput.desiredSpeed());

        inputs.set(rays.length + 3, 0, MAX_SPEED);

        for (int i = 0; i < lastOutput.memory().length; i++) {
            inputs.set(rays.length + 4 + i, 0, lastOutput.memory()[i]);
        }

        Matrix output = network.query(inputs);

        double angle = map(output.get(0, 0), 0, 1, -Math.PI, Math.PI);
        double speed = map(output.get(1, 0), 0, 1, 0, MAX_SPEED);

        double[] memory = lastOutput.memory;
        for (int i = 0; i < memory.length; i++) {
            memory[i] = output.get(2 + i, 0);
        }

        lastOutput = new Outputs(
                angle,
                speed,
                lastOutput.memory
        );
        return lastOutput;
    }

    public void save(File location) throws IOException {
        network.serialize(location);
    }

    public record Outputs(
            double desiredAngle,
            double desiredSpeed,
            double[] memory
    ) {

        public Outputs() {
            this(0, 0, new double[MEMORY_NODES]);
        }

    }

}
