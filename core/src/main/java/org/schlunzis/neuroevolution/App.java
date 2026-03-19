package org.schlunzis.neuroevolution;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.neuroevolution.model.track.TrackFactory;
import org.schlunzis.neuroevolution.sdk.track.Track;
import org.schlunzis.neuroevolution.util.I18nUtils;
import org.schlunzis.neuroevolution.util.PluginLoader;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class App extends Application {

    @Override
    public void init() {
        log.debug("Initializing Application");
        I18nUtils.setBundle(ResourceBundle.getBundle("lang.messages", Locale.GERMANY));

        PluginLoader.init();
        List<Track> tracks = PluginLoader.loadPlugins(Track.class);
        TrackFactory.setCustomTracks(tracks);
        log.debug("Loaded {} tracks", tracks.size());
    }

    @Override
    public void start(Stage stage) throws IOException {
        log.debug("Starting Application");
        Parent root = FXMLLoader.load(App.class.getResource("/org/schlunzis/neuroevolution/view/main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(App.class.getResource("/org/schlunzis/neuroevolution/style/theme.css").toExternalForm());
        stage.setTitle("Demo");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @Override
    public void stop() {
        log.debug("Stopping Application");
    }

}
