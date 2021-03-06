package ar.edu.itba.ss.simulator;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
	private static AtomicInteger maxTime = new AtomicInteger(2000);

	private SimpleStringProperty networkUsageProp = new SimpleStringProperty(
			String.valueOf(Statistics.getNetworkUsageAverage()));
	private SimpleStringProperty latencyProp = new SimpleStringProperty(
			String.valueOf(Statistics.getAverageLatency()));
	private SimpleStringProperty byterateProp = new SimpleStringProperty(
			String.valueOf(Statistics.getByterate()));
	private SimpleStringProperty desvestLatencyProp = new SimpleStringProperty(
			String.valueOf(Statistics.getDesvestLatency()));

	public static void main(String[] args) throws InterruptedException,
			ParseException {
		double lambda = 0.05;
		int bandwidth = 1000;
		int transferRate = 200;
		int users = 3;
		int routers = 3;
		int channels = 4;

		Options options = new Options();
		options.addOption("p", false, "packet switching");
		options.addOption("c", false, "circuit switching");
		options.addOption("l", true, "lambda");
		options.addOption("b", true, "routers bandwitdh");
		options.addOption("tr", true, "routers transfer rate");
		options.addOption("u", true, "users");
		options.addOption("r", true, "routers");
		options.addOption("t", true, "time");
		options.addOption("k", true, "channels");

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
		if (cmd.hasOption("r")) {
			routers = Integer.valueOf(cmd.getOptionValue("r"));
		}
		if (cmd.hasOption("t")) {
			maxTime.set(Integer.valueOf(cmd.getOptionValue("t")));
		}
		if (cmd.hasOption("k")) {
			channels = Integer.valueOf(cmd.getOptionValue("k"));
		}

		if (cmd.hasOption("p")) {
			timedObjects = NetworkFactory.getPacketNetwork(bandwidth,
					transferRate, lambda, users, routers, channels)
					.getTimedObjects();
		} else {
			timedObjects = NetworkFactory.getCircuitNetwork(bandwidth,
					transferRate, lambda, users, routers).getTimedObjects();
		}

		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		GifAnimation ani = new AnimatedGif(getClass().getResource(
				"/das giffen swai.gif").toExternalForm(), 1000, 500);
		ani.setCycleCount(10000);

		Thread thread = new Thread(() -> {
			for (int i = 0; i < maxTime.get(); i++) {
				timedObjects.forEach(to -> to.tick());
				Statistics.tick();
				Platform.runLater(() -> {
					networkUsageProp.set(String.valueOf(Statistics
							.getNetworkUsageAverage()));
					latencyProp.set(String.valueOf(Statistics
							.getAverageLatency()));
					byterateProp.set(String.valueOf(Statistics.getByterate()));
					desvestLatencyProp.set(String.valueOf(Statistics
							.getDesvestLatency()));
				});
				try {
					Thread.sleep(1000 / 60);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			ani.stop();
			this.animations.forEach(a -> a.stop());
		});

		HBox upper = new HBox();
		upper.getChildren().addAll(networkUsageChart(), byterateChart());
		HBox lower = new HBox();
		lower.getChildren().addAll(desvestChart(), latencyChart());

		VBox root = new VBox(upper, lower);

		HBox ctn = new HBox();

		ctn.getChildren().addAll(ani.getView());

		HBox btnCtn = new HBox();
		Button btPause = new Button("Stop");
		btPause.setOnAction(e -> {
			ani.stop();
			this.animations.forEach(a -> a.stop());
			thread.stop();
			btPause.setDisable(true);
		});

		Button btResume = new Button("Start");
		btResume.setOnAction(e -> {
			ani.play();
			this.animations.forEach(a -> a.play());
			thread.start();
			btResume.setDisable(true);
		});
		btnCtn.getChildren().addAll(btResume, btPause);

		HBox group = new HBox(root, new VBox(ctn, new VBox(
				networkUsage(networkUsageProp), latency(latencyProp),
				byteRate(byterateProp), desvestLatency(desvestLatencyProp)),
				btnCtn));

		stage.setScene(new Scene(group));
		stage.show();
	}

	private Pane byteRate(SimpleStringProperty property) {
		Text label = new Text("Byterate: ");
		Text text = new Text();
		text.textProperty().bindBidirectional(property);
		Text ms = new Text(" bytes/u.t.");
		return new HBox(label, text, ms);
	}

	private Pane networkUsage(SimpleStringProperty property) {
		Text label = new Text("Network usage: ");
		Text text = new Text();
		text.textProperty().bindBidirectional(property);
		Text ms = new Text(" %");
		return new HBox(label, text, ms);
	}

	private Pane latency(SimpleStringProperty property) {
		Text label = new Text("Latency: ");
		Text text = new Text();
		text.textProperty().bindBidirectional(property);
		Text ms = new Text("ms");
		return new HBox(label, text, ms);
	}

	private Pane desvestLatency(SimpleStringProperty property) {
		Text label = new Text("Desvío de la latencia: ");
		Text text = new Text();
		text.textProperty().bindBidirectional(property);
		Text ms = new Text("ms");
		return new HBox(label, text, ms);
	}

	private LineChart<Number, Number> networkUsageChart() {
		final NumberAxis xAxis = new NumberAxis(0, 1000, 5);
		final NumberAxis yAxis = new NumberAxis(0, 100, 1);
		final LineChart<Number, Number> lc = new LineChart<Number, Number>(
				xAxis, yAxis);

		yAxis.setLabel("Ancho de banda utilizado");
		xAxis.setLabel("Tiempo");

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
							private int previousTime = 750;

							@Override
							public void handle(ActionEvent actionEvent) {
								if (Statistics.getTime() > 750
										&& Statistics.getTime() > previousTime) {
									xAxis.setUpperBound(xAxis.getUpperBound()
											+ Statistics.getTime()
											- previousTime);
									xAxis.setLowerBound(xAxis.getLowerBound()
											+ Statistics.getTime()
											- previousTime);
									previousTime += Statistics.getTime()
											- previousTime;
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
		xAxis.setLabel("Tiempo");

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
							private int previousTime = 750;

							@Override
							public void handle(ActionEvent actionEvent) {
								if (Statistics.getTime() > 750
										&& Statistics.getTime() > previousTime) {
									xAxis.setUpperBound(xAxis.getUpperBound()
											+ Statistics.getTime()
											- previousTime);
									xAxis.setLowerBound(xAxis.getLowerBound()
											+ Statistics.getTime()
											- previousTime);
									previousTime += Statistics.getTime()
											- previousTime;
								}
								if (Statistics.getByterate() > 100) {
									yAxis.setUpperBound(Statistics
											.getByterate() + 100);
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

	private LineChart<Number, Number> desvestChart() {
		final NumberAxis xAxis = new NumberAxis(0, 1000, 10);
		final NumberAxis yAxis = new NumberAxis(0, 10, 0.1);
		final LineChart<Number, Number> lc = new LineChart<Number, Number>(
				xAxis, yAxis);

		yAxis.setLabel("Jitter (ms)");
		xAxis.setLabel("Tiempo");

		lc.setCreateSymbols(false);
		lc.setAnimated(false);
		lc.setLegendVisible(false);
		lc.setTitle("Jitter/Desvío");

		XYChart.Series<Number, Number> transferredPacketsSeries = new XYChart.Series<Number, Number>();
		lc.getData().add(transferredPacketsSeries);

		Timeline animation = new Timeline();
		animation.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000 / 60),
						new EventHandler<ActionEvent>() {
							private int previousTime = 750;

							@Override
							public void handle(ActionEvent actionEvent) {
								if (Statistics.getTime() > 750
										&& Statistics.getTime() > previousTime) {
									xAxis.setUpperBound(xAxis.getUpperBound()
											+ Statistics.getTime()
											- previousTime);
									xAxis.setLowerBound(xAxis.getLowerBound()
											+ Statistics.getTime()
											- previousTime);
									previousTime += Statistics.getTime()
											- previousTime;
								}
								transferredPacketsSeries
										.getData()
										.add(new XYChart.Data<Number, Number>(
												Statistics.getTime(),
												Statistics.getDesvestLatency()));
							}
						}));
		animation.setCycleCount(Animation.INDEFINITE);
		this.animations.add(animation);
		return lc;
	}

	private LineChart<Number, Number> latencyChart() {
		final NumberAxis xAxis = new NumberAxis(0, 1000, 5);
		final NumberAxis yAxis = new NumberAxis(0, 1000, 10);
		final LineChart<Number, Number> lc = new LineChart<Number, Number>(
				xAxis, yAxis);

		yAxis.setLabel("Latencia (ms)");
		xAxis.setLabel("Tiempo");

		lc.setCreateSymbols(false);
		lc.setAnimated(false);
		lc.setLegendVisible(false);
		lc.setTitle("Tiempo de transferencia (ms)");

		XYChart.Series<Number, Number> averageLatencySeries = new XYChart.Series<Number, Number>();
		lc.getData().add(averageLatencySeries);

		Timeline animation = new Timeline();
		animation.getKeyFrames().add(
				new KeyFrame(Duration.millis(1000 / 60),
						new EventHandler<ActionEvent>() {
							private int previousTime = 750;

							@Override
							public void handle(ActionEvent actionEvent) {
								if (Statistics.getTime() > 750
										&& Statistics.getTime() > previousTime) {
									xAxis.setUpperBound(xAxis.getUpperBound()
											+ Statistics.getTime()
											- previousTime);
									xAxis.setLowerBound(xAxis.getLowerBound()
											+ Statistics.getTime()
											- previousTime);
									previousTime += Statistics.getTime()
											- previousTime;
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

	public class AnimatedGif extends GifAnimation {

		public AnimatedGif(String filename, double durationMs, int scale) {

			GifDecoder d = new GifDecoder();
			d.read(filename);

			Image[] sequence = new Image[d.getFrameCount()];
			for (int i = 0; i < d.getFrameCount(); i++) {

				WritableImage wimg = null;
				BufferedImage bimg = d.getFrame(i);
				sequence[i] = SwingFXUtils.toFXImage(bimg, wimg);

			}

			super.init(sequence, durationMs, scale);
		}

	}

	public class GifAnimation extends Transition {

		private ImageView imageView;
		private int count;

		private int lastIndex;

		private Image[] sequence;

		private GifAnimation() {
		}

		public GifAnimation(Image[] sequence, double durationMs, int scale) {
			init(sequence, durationMs, scale);
		}

		private void init(Image[] sequence, double durationMs, int scale) {
			this.imageView = new ImageView(sequence[0]);
			this.imageView.setFitWidth(scale);
			this.imageView.setFitHeight(scale);
			this.sequence = sequence;
			this.count = sequence.length;

			setCycleCount(1);
			setCycleDuration(Duration.millis(durationMs));
			setInterpolator(Interpolator.LINEAR);

		}

		protected void interpolate(double k) {

			final int index = Math.min((int) Math.floor(k * count), count - 1);
			if (index != lastIndex) {
				imageView.setImage(sequence[index]);
				lastIndex = index;
			}

		}

		public ImageView getView() {
			return imageView;
		}

	}

}
