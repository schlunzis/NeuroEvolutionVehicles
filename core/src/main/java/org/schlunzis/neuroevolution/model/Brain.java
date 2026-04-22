package org.schlunzis.neuroevolution.model;

import org.schlunzis.zis.ai.nn.GeneticNeuralNetwork;
import org.schlunzis.zis.math.linear.Matrix;

import java.io.File;
import java.io.IOException;

import static org.schlunzis.neuroevolution.sdk.Constants.MAX_SPEED;
import static org.schlunzis.neuroevolution.sdk.util.MathUtils.map;
import static org.schlunzis.zis.ai.nn.NeuralNetwork.deserialize;


public class Brain {

    private final GeneticNeuralNetwork network;

    public Brain(int rayCount) {
        this(new GeneticNeuralNetwork(0, 0, rayCount + 1, 2, rayCount * 2));
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
        return new Brain(network.copy().mutate(mutationRate));
    }

    public Outputs query(double[] rays, double currentSpeed) {
        Matrix inputs = new Matrix(rays.length + 1, 1);
        for (int i = 0; i < rays.length; i++) {
            inputs.set(i, 0, rays[i]);
        }
        inputs.set(rays.length, 0, currentSpeed);

        Matrix output = network.query(inputs);

        double angle = map(output.get(0, 0), 0, 1, -Math.PI, Math.PI);
        double speed = map(output.get(1, 0), 0, 1, 0, MAX_SPEED);

        return new Outputs(
                angle,
                speed
        );
    }

    public void save(File location) throws IOException {
        network.serialize(location);
    }

    public record Outputs(
            double desiredAngle,
            double desiredSpeed
    ) {

    }

}
