package org.schlunzis.neuroevolution.model;

import org.schlunzis.zis.ai.nn.GeneticNeuralNetwork;

import static org.schlunzis.neuroevolution.sdk.Constants.MAX_SPEED;
import static org.schlunzis.neuroevolution.sdk.util.MathUtils.map;


public class Brain {

    private final GeneticNeuralNetwork network;

    public Brain(int rayCount) {
        this(new GeneticNeuralNetwork(0, 0, rayCount + 1, 2, rayCount * 2));
    }

    public Brain(GeneticNeuralNetwork network) {
        this.network = network;
    }

    public Brain copy() {
        return new Brain(network.copy());
    }

    public Brain mutate(double mutationRate) {
        return new Brain(network.copy().mutate(mutationRate));
    }

    public Outputs query(double[] rays, double currentSpeed) {
        double[][] inputs = new double[1][rays.length + 1];
        System.arraycopy(rays, 0, inputs[0], 0, rays.length);
        inputs[0][rays.length] = currentSpeed;

        double[][] output = network.query(inputs).toArray();

        double angle = map(output[0][0], 0, 1, -Math.PI, Math.PI);
        double speed = map(output[1][0], 0, 1, 0, MAX_SPEED);

        return new Outputs(
                angle,
                speed
        );
    }

    public record Outputs(
            double desiredAngle,
            double desiredSpeed
    ) {

    }

}
