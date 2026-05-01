package org.schlunzis.neuroevolution.model.physics;

import org.schlunzis.neuroevolution.sdk.util.SVector;

import static java.lang.Math.*;
import static org.schlunzis.neuroevolution.util.Geometry.cot;

/// This physics model simulates Ackermann steering as described in the paper:
/// "A 2D Car Physics Model based on Ackermann Steering" by Helmut Hlavacs.
public class AckermannSteeringVehiclePhysicsModel {

    /// Width of the vehicle in metres
    private final double W;
    /// Length of the vehicle in metres
    private final double L;
    /// Mass of the vehicle in kilograms
    private final double M;
    /// Radius of the tyre in metres
    private final double R_w;
    /// Inertia of the tyre in kg m²
    private final double I_w;
    /// Inertia of the vehicle
    private final double I_c;
    /// Other inertia of the vehicle
    private final double I_B;

    private Input oldInput = new Input();

    public AckermannSteeringVehiclePhysicsModel(double W, double L) {
        this(W, L, L * 300);
    }

    public AckermannSteeringVehiclePhysicsModel(double W, double L, double M) {
        this.W = W;
        this.L = L;
        this.M = M;
        this.R_w = L / 6;
        this.I_w = 2 * L;
        this.I_c = (M * (pow(W, 2) + pow(L, 2))) / 12;
        this.I_B = I_c + M * pow(L, 2) / 4;
    }

    public Output compute(double v, double beta, double torque, double dt) {
        return compute(v, new Input(beta, torque < 0 ? torque : 0, torque), dt);
    }

    /// Implements the ComputeModel function as seen on page 8.
    private Output compute(double v, Input input, double dt) {
        System.out.println(input);
        double beta = oldInput.beta();
        double dBeta = input.beta() - oldInput.beta();
        double T_ef = oldInput.T_ef();
        double T_er = oldInput.T_er();

        //23
        double omega = (v / L) * tan(beta);
        SVector F_cpf = SVector.zero();
        SVector F_cpr = SVector.zero();
        if (beta != 0) {
            F_cpf = computeF_cpf(v, beta);
            F_cpr = computeF_cpr(v, beta);
        }
        // 33
        double f_r = (I_B * tan(beta)) / pow(L, 2);
        double F_tot = computeF_tot(v, beta, dBeta, F_cpf, f_r, T_ef, T_er, dt);
        double a = F_tot / M;
        // 24
        double alpha = (v / (L * pow(cos(beta), 2))) * (dBeta / dt);
        // ?
        double dv = v * dt;
        SVector F_rotf = new SVector(
                // 25
                (alpha * I_B) / L,
                // 26
                -((I_B * tan(beta)) / pow(L, 2)) * (tan(beta) * (dv / dt) + (v / pow(cos(beta), 2)) * (dBeta / dt))
        );
        // 28
        SVector F_rotr = new SVector(
                (I_B - I_c) / I_B * F_rotf.x(),
                0
        );
        // ?
        double F_accf = a;
        // 30
        SVector F_tracf = computeF_tracf(F_cpf, F_rotf, beta, F_accf);
        // 34
        double F_tracry = F_tot - F_tracf.y();
        // 29
        double T_totr = T_er - F_tracry * R_w;
        // 31
        double T_totf = T_ef - F_accf * R_w;

        SVector F_tracr = new SVector(0, F_tracry).add(F_cpr).add(F_rotr);

        oldInput = input;

        Output output = new Output(
                a * pow(dt, 2) / 2,
                omega * dt + alpha * pow(dt, 2) / 2
        );
        System.out.println(output);
        return output;
    }

    /// 21
    private SVector computeF_cpf(double v, double beta) {
        double numerator = -M * pow(v, 2) * pow(tan(beta), 2);
        double factor = numerator / L;
        return new SVector(
                cot(beta) / 2,
                1 / 2d
        ).mult(factor);
    }

    /// 22
    private SVector computeF_cpr(double v, double beta) {
        double numerator = -M * pow(v, 2) * pow(tan(beta), 2);
        double factor = numerator / L;
        return new SVector(
                cot(beta) / 2,
                0
        ).mult(factor);
    }

    /// 30
    private SVector computeF_tracf(SVector F_cpf, SVector F_rotf, double beta, double F_accf) {
        double sinBeta = sin(beta);
        double cosBeta = cos(beta);

        SVector product = new SVector(
                F_accf * cosBeta + F_accf * -sinBeta,
                F_accf * sinBeta + F_accf * cosBeta
        );

        return F_cpf.add(F_rotf).add(product);
    }

    /// 37
    private double computeF_tot(double v, double beta, double dBeta, SVector F_cpf, double f_r, double T_ef, double T_er, double dt) {
        final double R_w2 = pow(R_w, 2);
        double denominator = 2 * I_w + (M + f_r * tan(beta)) * R_w2;

        double firstNumerator = M * R_w2 * (F_cpf.y() - (f_r * v) / pow(cos(beta), 2) * (dBeta / dt));
        double first = firstNumerator / denominator;

        double secondNumerator = M * R_w * (cos(beta) * T_ef + T_er);
        double second = secondNumerator / denominator;

        return first + second;
    }

    public record Input(
            double beta,
            double T_ef,
            double T_er
    ) {

        public Input() {
            this(0, 0, 0);
        }

        public Input withBeta(double beta) {
            return new Input(beta, T_ef, T_er);
        }

    }

    public record Output(
            double a,
            double theta
    ) {
    }

}
