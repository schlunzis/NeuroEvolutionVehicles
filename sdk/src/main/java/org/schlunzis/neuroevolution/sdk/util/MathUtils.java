package org.schlunzis.neuroevolution.sdk.util;

public class MathUtils {

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
    public static double map(double value, double inputMin, double inputMax, double outputMin, double outputMax) {
        return outputMin + (outputMax - outputMin) * ((value - inputMin) / (inputMax - inputMin));
    }

}
