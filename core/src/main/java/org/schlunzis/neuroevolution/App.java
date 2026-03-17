package org.schlunzis.neuroevolution;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.schlunzis.neuroevolution.model.track.TrackFactory;
import org.schlunzis.neuroevolution.sdk.track.Track;
import org.schlunzis.neuroevolution.util.I18nUtils;
import org.schlunzis.neuroevolution.util.PluginLoader;
import org.schlunzis.neuroevolution.view.MainStage;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		log.debug("Initializing Application");
		I18nUtils.setBundle(ResourceBundle.getBundle("lang.messages", Locale.GERMANY));

		PluginLoader.init();
		List<Track> tracks = PluginLoader.loadPlugins(Track.class);
		TrackFactory.setCustomTracks(tracks);
		log.debug("Loaded {} tracks", tracks.size());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		log.debug("Starting Application");
		new MainStage();
	}

	@Override
	public void stop() throws Exception {
		log.debug("Stopping Application");
	}

}
