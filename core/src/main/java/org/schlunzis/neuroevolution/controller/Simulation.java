package org.schlunzis.neuroevolution.controller;

import org.schlunzis.neuroevolution.model.GeneticAlgorithm;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Simulation extends Thread {

	private GeneticAlgorithm ga;
	private SimulationController simulationController;
	@Getter
	@Setter
	private volatile boolean stopped;
	@Getter
	@Setter
	private volatile boolean paused;
	@Getter
	private final Object lock = new Object();

	private volatile int sleepTime = 1000 / 30; // default 30 fps

	public Simulation(GeneticAlgorithm ga, SimulationController simController) {
		this.ga = ga;
		this.simulationController = simController;
		stopped = false;
		paused = false;
	}

	@Override
	public void run() {
		log.debug("simulation started");
		while (!stopped) {
			ga.update();
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			while (paused) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt();
					}
				}
			}
		}
		simulationController.finish();
	}

}
