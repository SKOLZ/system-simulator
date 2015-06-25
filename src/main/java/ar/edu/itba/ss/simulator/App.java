package ar.edu.itba.ss.simulator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ar.edu.itba.ss.simulator.model.Network;

public class App extends Application {

	public static void main(String[] args) throws InterruptedException {
		int simulationDelay = 10;
		int maxTime = 1000;
		double lambda = 0.05;

		new Network().run(simulationDelay, maxTime, lambda);
		// Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		ImageView sender1iv = new ImageView();
		sender1iv.setImage(new Image("user.jpg", 500, 500, false, false));
		sender1iv.setPreserveRatio(true);
		sender1iv.setFitHeight(150);
		sender1iv.setFitWidth(150);

		ImageView sender2iv = new ImageView();
		sender2iv.setImage(new Image("user.jpg", 500, 500, false, false));
		sender2iv.setPreserveRatio(true);
		sender2iv.setFitHeight(150);
		sender2iv.setFitWidth(150);

		ImageView sender3iv = new ImageView();
		sender3iv.setImage(new Image("user.jpg", 500, 500, false, false));
		sender3iv.setPreserveRatio(true);
		sender3iv.setFitHeight(150);
		sender3iv.setFitWidth(150);

		ImageView router1iv = new ImageView();
		router1iv.setImage(new Image("router.jpg", 500, 500, false, false));
		router1iv.setPreserveRatio(true);
		router1iv.setFitHeight(150);
		router1iv.setFitWidth(150);

		ImageView router2iv = new ImageView();
		router2iv.setImage(new Image("router.jpg", 500, 500, false, false));
		router2iv.setPreserveRatio(true);
		router2iv.setFitHeight(150);
		router2iv.setFitWidth(150);

		ImageView router3iv = new ImageView();
		router3iv.setImage(new Image("router.jpg", 500, 500, false, false));
		router3iv.setPreserveRatio(true);
		router3iv.setFitHeight(150);
		router3iv.setFitWidth(150);

		ImageView receiveriv = new ImageView();
		receiveriv.setImage(new Image("user.jpg", 500, 500, false, false));
		receiveriv.setPreserveRatio(true);
		receiveriv.setFitHeight(150);
		receiveriv.setFitWidth(150);

		VBox sender = new VBox(sender1iv, sender2iv, sender3iv);
		StackPane router1 = new StackPane(router1iv);
		StackPane router2 = new StackPane(router2iv);
		StackPane router3 = new StackPane(router3iv);
		StackPane receiver = new StackPane(receiveriv);

		HBox root = new HBox();
		root.getChildren().addAll(sender, router1, router2, router3, receiver);

		Scene scene = new Scene(root, 1024, 768);

		stage.setScene(scene);
		stage.show();
	}

}
