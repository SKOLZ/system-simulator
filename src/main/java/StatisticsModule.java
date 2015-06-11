import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class StatisticsModule extends Application {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StatisticsModule.class);

	private static StatisticsModule instance;

	public static StatisticsModule getInstance() {
		if (instance == null)
			instance = new StatisticsModule();
		return instance;
	}

	private Map<Device, Integer> attemptedRequests = Maps.newHashMap();
	private Map<Device, Integer> startedRequests = Maps.newHashMap();
	private Map<Device, Integer> completedRequests = Maps.newHashMap();
	private Map<Device, Integer> exceededBandwithRequests = Maps.newHashMap();

	public void logExceededBandwithRequest(Device device) {
		this.logRequestOnMap(exceededBandwithRequests, device);
	}

	public void logStartedRequest(Device device) {
		this.logRequestOnMap(startedRequests, device);
	}

	public void logCompletedRequest(Device device) {
		completed++;
		this.logRequestOnMap(completedRequests, device);
	}

	public void logAttemptedRequest(Device device) {
		attempted++;
		this.logRequestOnMap(attemptedRequests, device);
	}

//	public void printExceededBandwithRequestsPerDevice() {
//		this.printRequestStatistics(exceededBandwithRequests,
//				"exceeded bandwith");
//	}
//
//	public void printStartedRequestsPerDevice() {
//		this.printRequestStatistics(startedRequests, "started");
//	}
//
//	public void printCompletedRequestsPerDevice() {
//		this.printRequestStatistics(completedRequests, "completed");
//	}
//
//	public void printAttemptedRequestsPerDevice() {
//		this.printRequestStatistics(attemptedRequests, "attempted");
//	}

	private void logRequestOnMap(Map<Device, Integer> map, Device device) {
		map.putIfAbsent(device, 0);
		map.put(device, map.get(device) + 1);
	}

//	private void printRequestStatistics(Map<Device, Integer> map, String message) {
//		for (Entry<Device, Integer> entry : map.entrySet()) {
//			LOGGER.info(entry.getKey() + " " + message + " requests: "
//					+ entry.getValue());
//		}
//	}
	
	private static Network network = NetworkFactory.getInstance()
			.createCircuitDomiciliaryNetwork();
	private static Integer attempted = 0, completed = 0;
	private static long time = 0;
	
	private XYChart.Series<Number,Number> attemptedDataSeries;
	private XYChart.Series<Number,Number> completedDataSeries;
    private NumberAxis xAxis;
    private Timeline animation;

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
        root.getChildren().add(createChart());
        // create timeline to add new data every 60th of second
        animation = new Timeline();
        animation.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60), new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                // 6 minutes data per frame
                for(int count=0; count < 6; count++) {
                	network.update();
                	attemptedDataSeries.getData().add(
        					new XYChart.Data<Number, Number>(time, attempted));
                	completedDataSeries.getData().add(
        					new XYChart.Data<Number, Number>(time, completed));
        			time++;
        			if (time % 1000 == 0) {
                        xAxis.setUpperBound(xAxis.getUpperBound()+1000);
        			}
//        			if (time % 10000 == 0) {
//        				xAxis.setTickLength(xAxis.getTickLength() * 2);
//        			}
                }
            }
        }));
        animation.setCycleCount(Animation.INDEFINITE);
    }

    protected LineChart<Number, Number> createChart() {
        xAxis = new NumberAxis(0,1000,100);
        final NumberAxis yAxis = new NumberAxis(0,100000,100);
        final LineChart<Number,Number> lc = new LineChart<Number,Number>(xAxis,yAxis);
        // setup chart
        lc.setId("lineStockDemo");
        lc.setCreateSymbols(false);
        lc.setAnimated(false);
        lc.setLegendVisible(false);
        lc.setTitle("Circuit switching");
        xAxis.setLabel("Time");
        xAxis.setForceZeroInRange(false);
        
        yAxis.setLabel("Requests");
        attemptedDataSeries = new XYChart.Series<Number,Number>();
        attemptedDataSeries.setName("Attempted Requests");
        
        completedDataSeries = new XYChart.Series<Number,Number>();
        completedDataSeries.setName("Completed Requests");
        
        lc.getData().add(attemptedDataSeries);
        lc.getData().add(completedDataSeries);
        return lc;
    }

   public void play() {
        animation.play();
    }

    @Override public void stop() {
        animation.pause();
    }    

    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
        play();
    }
    public static void main(String[] args) {		
    	launch(args); 
    }

}
