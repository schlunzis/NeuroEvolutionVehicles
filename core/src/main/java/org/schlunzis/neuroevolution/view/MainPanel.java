package org.schlunzis.neuroevolution.view;

import java.util.List;

import org.schlunzis.neuroevolution.model.GeneticAlgorithm;
import org.schlunzis.neuroevolution.model.Vehicle;
import org.schlunzis.neuroevolution.sdk.track.Track;
import org.schlunzis.neuroevolution.sdk.util.Boundary;
import org.schlunzis.neuroevolution.util.Observable;
import org.schlunzis.neuroevolution.util.Observer;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MainPanel extends Canvas implements Observer {
	private GeneticAlgorithm ga;

	public MainPanel(GeneticAlgorithm ga) {
		this.ga = ga;
		ga.addObserver(this);
		setWidth(400);
		setHeight(400);
		paint();
	}

	public void paint() {
		GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(Color.GREEN);
		gc.fillRect(0, 0, getWidth(), getHeight());

		// draw the track
		gc.setStroke(Color.BLACK);
		Track track = ga.getTrack();
		for (Boundary b : track.getWalls()) {
			gc.strokeLine(mapWidth(b.getA().x), mapHeight(b.getA().y), mapWidth(b.getB().x), mapHeight(b.getB().y));
		}
		gc.strokeOval(mapWidth(track.getStart().x), mapHeight(track.getStart().y), 5, 5);
		// draw the checkpoints
		gc.setStroke(Color.RED);
		for (Boundary b : track.getCheckpoints()) {
			gc.strokeLine(mapWidth(b.getA().x), mapHeight(b.getA().y), mapWidth(b.getB().x), mapHeight(b.getB().y));
		}

		// draw the vehicles
		List<Vehicle> vehicles = ga.getPopulation();
		gc.setStroke(Color.BLACK);
		for (int i = 0; i < vehicles.size(); i++) {
			Vehicle v = vehicles.get(i);
			if (v.equals(ga.getPrevBest()))
				continue;
			drawVehicle(v, gc);
		}
		gc.setStroke(Color.RED);
		drawVehicle(ga.getPrevBest(), gc);
	}

	private void drawVehicle(Vehicle v, GraphicsContext gc) {
		if (v == null)
			return;
		gc.save();
		gc.translate(mapWidth(v.getPos().x), mapHeight(v.getPos().y));
		gc.rotate(Math.toDegrees(v.getVel().heading()));
		gc.strokeRect(mapWidth(-v.getVechileWidth() / 2), mapHeight(-v.getVehicleHeight()),
				mapWidth(v.getVechileWidth()), mapHeight(v.getVehicleHeight()));
		gc.restore();
	}

	@Override
	public void update(Observable observable) {
		Platform.runLater(this::update);
	}

	private void update() {
		paint();
	}

	private int mapWidth(double x) {
		return (int) (x * getWidth());
	}

	private int mapHeight(double y) {
		return (int) (y * getHeight());
	}

}
