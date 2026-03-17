package org.schlunzis.neuroevolution.controller;

import org.schlunzis.neuroevolution.model.GeneticAlgorithm;
import org.schlunzis.neuroevolution.view.MainStage;

import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimulationController {

	private MainStage mainStage;

	private GeneticAlgorithm ga;

	private boolean simulationRunning = false;

	private Simulation simulation;

	public SimulationController(MainStage mainStage, GeneticAlgorithm ga) {
		this.ga = ga;
		this.mainStage = mainStage;
		mainStage.getStartButtonToolbar().setOnAction(e -> {
			if (!isSimulationRunning())
				start();
			else
				resume();
		});
		mainStage.getPauseButtonToolbar().setOnAction(e -> pause());
		mainStage.getResetButtonToolbar().setOnAction(e -> reset());

	}

	public void pause() {
		if (isSimulationRunning()){
			simulation.setPaused(true);
			disableButtonStates(false, true);
		}
	}

	public void start() {
		log.debug("Starting new simulation");
		simulationRunning = true;
		simulation = new Simulation(ga, this);
		simulation.setDaemon(true);
		simulation.start();
		disableButtonStates(true, false);
	}

	public void resume() {
		if (isSimulationRunning() && simulation.isPaused()) {
			simulation.setPaused(false);
			synchronized (simulation.getLock()) {
				simulation.getLock().notifyAll();
			}
			disableButtonStates(true, false);
		}
	}

	public void stop() {
		if (isSimulationRunning()) {
			simulation.setStopped(true);
			simulation.setPaused(false);
			simulation.interrupt();
			synchronized (simulation.getLock()) {
				simulation.getLock().notifyAll();
			}
			disableButtonStates(false, true);
		}
	}

	public void reset() {
		stop();
		ga.reset();
		start();
	}

	public boolean isSimulationRunning() {
		return simulation != null || simulationRunning;
	}

	public void finish() {
		log.debug("finishing the simulation");
	}

	private void disableButtonStates(boolean start, boolean pause) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> {
				mainStage.getStartButtonToolbar().setDisable(start);
				mainStage.getPauseButtonToolbar().setDisable(pause);
			});
		} else {
			mainStage.getStartButtonToolbar().setDisable(start);
			mainStage.getPauseButtonToolbar().setDisable(pause);
		}
	}

}
