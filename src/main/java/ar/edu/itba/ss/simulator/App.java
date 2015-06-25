package ar.edu.itba.ss.simulator;

import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ar.edu.itba.ss.simulator.model.NetworkFactory;
import ar.edu.itba.ss.simulator.model.TimedObject;

import com.google.common.collect.Lists;

public class App extends Application {

	private static List<TimedObject> timedObjects = Lists.newLinkedList();
	private List<Animation> animations = Lists.newLinkedList();

	public static void main(String[] args) throws InterruptedException,
			ParseException {
		double lambda = 0.05;
		int bandwidth = 1000;
		int transferRate = 200;
		int users = 3;
		int routers = 3;

		Options options = new Options();
		options.addOption("p", false, "packet switching");
		options.addOption("c", false, "circuit switching");
		options.addOption("l", true, "lambda");
		options.addOption("b", true, "routers bandwitdh");
		options.addOption("tr", true, "routers transfer rate");
		options.addOption("u", true, "users");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("l")) {
			lambda = Double.valueOf(cmd.getOptionValue("l"));
		}
		if (cmd.hasOption("b")) {
			bandwidth = Integer.valueOf(cmd.getOptionValue("b"));
		}
		if (cmd.hasOption("tr")) {
			transferRate = Integer.valueOf(cmd.getOptionValue("tr"));
		}
		if (cmd.hasOption("u")) {
			users = Integer.valueOf(cmd.getOptionValue("u"));
		}

		if (cmd.hasOption("p")) {
			timedObjects = NetworkFactory.getPacketNetwork(bandwidth,
					transferRate, lambda, users, routers).getTimedObjects();
		} else {
			timedObjects = NetworkFactory.getCircuitNetwork(bandwidth,
					transferRate, lambda, users, routers).getTimedObjects();
		}

		new Thread(() -> {
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				try {
					Thread.sleep(1000 / 60);
				} catch (Exception e) {
					e.printStackTrace();
				}
				timedObjects.forEach(to -> to.tick());
				Statistics.tick();
			}
		}).start();

		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		HBox upper = new HBox();
		upper.getChildren().addAll(networkUsageChart(), byterateChart());
		HBox lower = new HBox();
		lower.getChildren().addAll(transferredPacketsChart(), latencyChart());

		VBox root = new VBox(upper, lower);

		ImageView gifView = new ImageView();
		gifView.setImage(new Image("das giffen swai.gif"));
		gifView.setFitWidth(500);
		gifView.setPreserveRatio(true);
		gifView.setSmooth(true);
		gifView.setCache(true);

		HBox group = new HBox(root, gifView);

		stage.setScene(new Scene(group));
		stage.show();

		// tick();

		this.animations.forEach(a -> a.play());
	}

	private void tick() {
		Timeline animation = new Timeline();
		animation.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000 / 60),
						new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent actionEvent) {
								timedObjects.forEach(to -> to.tick());
								Statistics.tick();
							}
						}));
		animation.setCycleCount(Animation.INDEFINITE);
		this.animations.add(animation);
	}

	private LineChart<Number, Number> networkUsageChart() {
		final NumberAxis xAxis = new NumberAxis(0, 1000, 5);
		final NumberAxis yAxis = new NumberAxis(0, 100, 1);
		final LineChart<Number, Number> lc = new LineChart<Number, Number>(
				xAxis, yAxis);

		yAxis.setLabel("Ancho de banda utilizado");

		lc.setCreateSymbols(false);
		lc.setAnimated(false);
		lc.setLegendVisible(false);
		lc.setTitle("Network usage average");

		XYChart.Series<Number, Number> networkUsageSeries = new XYChart.Series<Number, Number>();
		lc.getData().add(networkUsageSeries);

		Timeline animation = new Timeline();
		animation.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000 / 60),
						new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent actionEvent) {
								if (Statistics.getTime() > 750) {
									xAxis.setUpperBound(xAxis.getUpperBound() + 1);
									xAxis.setLowerBound(xAxis.getLowerBound() + 1);
								}
								networkUsageSeries
										.getData()
										.add(new XYChart.Data<Number, Number>(
												Statistics.getTime(),
												Statistics
														.getNetworkUsageAverage()));
							}
						}));
		animation.setCycleCount(Animation.INDEFINITE);
		this.animations.add(animation);
		return lc;
	}

	private LineChart<Number, Number> byterateChart() {
		final NumberAxis xAxis = new NumberAxis(0, 1000, 5);
		final NumberAxis yAxis = new NumberAxis(0, 150, 5);
		final LineChart<Number, Number> lc = new LineChart<Number, Number>(
				xAxis, yAxis);

		yAxis.setLabel("bytes / unidad de tiempo");

		lc.setCreateSymbols(false);
		lc.setAnimated(false);
		lc.setLegendVisible(false);
		lc.setTitle("Byterate average");

		XYChart.Series<Number, Number> byterateSeries = new XYChart.Series<Number, Number>();
		lc.getData().add(byterateSeries);

		Timeline animation = new Timeline();
		animation.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000 / 60),
						new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent actionEvent) {
								if (Statistics.getTime() > 750) {
									xAxis.setUpperBound(xAxis.getUpperBound() + 1);
									xAxis.setLowerBound(xAxis.getLowerBound() + 1);
								}
								byterateSeries.getData().add(
										new XYChart.Data<Number, Number>(
												Statistics.getTime(),
												Statistics.getByterate()));
							}
						}));
		animation.setCycleCount(Animation.INDEFINITE);
		this.animations.add(animation);
		return lc;
	}

	private LineChart<Number, Number> transferredPacketsChart() {
		final NumberAxis xAxis = new NumberAxis(0, 1000, 5);
		final NumberAxis yAxis = new NumberAxis(0, 1000, 10);
		final LineChart<Number, Number> lc = new LineChart<Number, Number>(
				xAxis, yAxis);

		yAxis.setLabel("Paquetes");

		lc.setCreateSymbols(false);
		lc.setAnimated(false);
		lc.setLegendVisible(false);
		lc.setTitle("Paquetes transferidos");

		XYChart.Series<Number, Number> transferredPacketsSeries = new XYChart.Series<Number, Number>();
		lc.getData().add(transferredPacketsSeries);

		Timeline animation = new Timeline();
		animation.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000 / 60),
						new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent actionEvent) {
								if (Statistics.getTime() > 750) {
									xAxis.setUpperBound(xAxis.getUpperBound() + 1);
									xAxis.setLowerBound(xAxis.getLowerBound() + 1);
								}
								transferredPacketsSeries
										.getData()
										.add(new XYChart.Data<Number, Number>(
												Statistics.getTime(),
												Statistics
														.getTransferedPackets()));
							}
						}));
		animation.setCycleCount(Animation.INDEFINITE);
		this.animations.add(animation);
		return lc;
	}

	private LineChart<Number, Number> latencyChart() {
		final NumberAxis xAxis = new NumberAxis(0, 1000, 5);
		final NumberAxis yAxis = new NumberAxis(0, 10000, 100);
		final LineChart<Number, Number> lc = new LineChart<Number, Number>(
				xAxis, yAxis);

		yAxis.setLabel("Latencia (ms)");

		lc.setCreateSymbols(false);
		lc.setAnimated(false);
		lc.setLegendVisible(false);
		lc.setTitle("Latencia promedio de mensajes");

		XYChart.Series<Number, Number> averageLatencySeries = new XYChart.Series<Number, Number>();
		lc.getData().add(averageLatencySeries);

		Timeline animation = new Timeline();
		animation.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000 / 60),
						new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent actionEvent) {
								if (Statistics.getTime() > 750) {
									xAxis.setUpperBound(xAxis.getUpperBound() + 1);
									xAxis.setLowerBound(xAxis.getLowerBound() + 1);
								}
								averageLatencySeries
										.getData()
										.add(new XYChart.Data<Number, Number>(
												Statistics.getTime(),
												Statistics.getAverageLatency()));
							}
						}));
		animation.setCycleCount(Animation.INDEFINITE);
		this.animations.add(animation);
		return lc;
	}

}
