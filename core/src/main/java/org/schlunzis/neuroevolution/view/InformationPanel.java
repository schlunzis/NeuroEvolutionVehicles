package org.schlunzis.neuroevolution.view;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.schlunzis.neuroevolution.model.GeneticAlgorithm;
import org.schlunzis.neuroevolution.util.Observable;
import org.schlunzis.neuroevolution.util.Observer;

public class InformationPanel extends Pane implements Observer {

    private final Label generationCount;
    private final Label currentScore;
    private final Label bestFitness;

    public InformationPanel(GeneticAlgorithm ga) {
        ga.addObserver(this);
        generationCount = new Label("gen: #0");
        generationCount.setPrefWidth(70);
        currentScore = new Label("Score: ");
        currentScore.setPrefWidth(75);
        bestFitness = new Label("max Fit:");
        bestFitness.setPrefWidth(80);
        var hBox = new HBox(generationCount, currentScore, bestFitness);
        getChildren().add(hBox);

    }

    @Override
    public void update(Observable observable) {
        if (observable instanceof GeneticAlgorithm geneticAlgorithm) {
            Platform.runLater(() -> {
                generationCount.setText("gen #" + geneticAlgorithm.getGenerationCount());
                currentScore.setText("Score: ");
                bestFitness.setText("max Fit: ");
            });
        }
    }
}
