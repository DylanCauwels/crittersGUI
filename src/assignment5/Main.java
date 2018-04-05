package assignment5;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;

import javax.swing.*;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.ArrayList;

public class Main extends Application implements EventHandler<ActionEvent>{

	private int stepInput = 1;
	private int quantityInput = 1;
	private String lastSeed = "~~~~~~~~~~~~~";
	private int stepsPerFrame;

	public static void main(String[] args) {
		System.out.println(Arrays.toString(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()));
		launch(args);
	}



	@Override
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setTitle("Critter World");

		//Central Pane
		//TODO this is your pane to work in
		Pane center = new Pane();
		center.setPadding(new Insets(5, 5, 5, 5));
		center.setBorder(new Border((new BorderStroke(Color.BLACK,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));

		Canvas canvas = new Canvas((int)center.getWidth(),(int)center.getHeight());
		center.getChildren().add(canvas);

		canvas.widthProperty().bind(center.widthProperty());
		canvas.heightProperty().bind(center.heightProperty());
		// redraw when resized
		canvas.widthProperty().addListener(event -> displayWorld(canvas));
		canvas.heightProperty().addListener(event -> displayWorld(canvas));

		displayWorld(canvas);


		//CritterQuantity Pane Title
		Label quantityStepTitle = new Label("Critter Population Settings");


		//CritterQuantity 'Add' Button
		Button sizeStep = new Button();
		sizeStep.setText("Add");
		sizeStep.setOnAction(event -> {
            //TODO add specified Critters to World

        });
		Label currentQuantityInput = new Label("Current Quantity: " + quantityInput + " Critters");

		//CritterQuantity Slider
		Slider quantityMultiplier = new Slider();
		quantityMultiplier.setMin(0);
		quantityMultiplier.setMax(100);
		quantityMultiplier.setValue(1);
		quantityMultiplier.setShowTickLabels(true);
		quantityMultiplier.setShowTickLabels(true);
		quantityMultiplier.valueProperty().addListener((observable, oldValue, newValue) -> {
            quantityInput = (int)quantityMultiplier.getValue();
            currentQuantityInput.setText("Current Interval: " + quantityInput + " Critters");
        });

		//CritterQuantity Slider
		Button submit = new Button("Custom");
		submit.setOnAction(event -> {
			long newInput = inputAlert("Input", "Set your desired custom interval range 1-1000");
			if(newInput > 1000) {
				displayAlert("Error", "Error: Invalid Input. Critter Interval Unchanged.");
			} else {
				quantityInput = (int)newInput;
				currentQuantityInput.setText("Current Interval: " + quantityInput + " Steps");
			}
		});

		//CritterQuantity ChoiceBox
		ChoiceBox<String> critterTypes = new ChoiceBox<String>();
		critterTypes.getItems().add("Craig");
		critterTypes.getItems().add("Tragic Critter");
		critterTypes.getItems().add("Algae");
		critterTypes.getItems().add("Algaephobic Critter");
		//TODO implement automatic scanner that detects Critter classes in the assignment package
		critterTypes.setValue("Craig");

		//Quantity Pane
		HBox quant = new HBox(quantityMultiplier, submit);
		quant.setAlignment(Pos.CENTER_RIGHT);
		quant.setSpacing(15);

		HBox quantz = new HBox(currentQuantityInput, sizeStep);
		quantz.setAlignment(Pos.CENTER_RIGHT);
		quantz.setSpacing(25);

		VBox quantities = new VBox(quantityStepTitle, critterTypes, quant, quantz);

		quantities.setSpacing(15);
		quantities.setBorder(new Border(new BorderStroke(Color.BLACK,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		quantities.setAlignment(Pos.CENTER);
		quantities.setPadding(new Insets(15, 5, 15, 5));


		//WorldStep Pane Title
		Label timeStepTitle = new Label("Time-Step Settings");


		//WorldStep 'Step' Button
		Button inputStep = new Button();
		inputStep.setText("Step");
		inputStep.setOnAction(event -> {
			for(int i = 0; i < stepInput; i++) {
				Critter.worldTimeStep();
				displayWorld(canvas);
			}
		});
		Label currentStepInput = new Label("Current Interval: " + stepInput + " Steps");

		//WorldStep Multiplier Slider
		Slider stepMultiplier = new Slider(0, 100, 1);
		stepMultiplier.setShowTickLabels(true);
		stepMultiplier.setShowTickMarks(true);
		stepMultiplier.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				stepInput = (int)stepMultiplier.getValue();
				currentStepInput.setText("Current Interval: " + stepInput + " Steps");
			}
		});

		//WorldStep 'Custom' Button
		Button custom = new Button("Custom");
		custom.setOnAction(event -> {
			long newInput = inputAlert("Input", "Set your desired custom interval range 1-1000");
			if(newInput > 1000) {
				displayAlert("Error", "Error: Invalid Input. World-Step Interval Unchanged.");
			} else {
				stepInput = (int)newInput;
				currentStepInput.setText("Current Interval: " + stepInput + " Steps");
			}
		});

		//TimeStep Panes
		HBox steps = new HBox(stepMultiplier, custom);
		steps.setAlignment(Pos.CENTER_RIGHT);
		steps.setSpacing(15);

		HBox stepz = new HBox(currentStepInput, inputStep);
		stepz.setAlignment(Pos.CENTER_RIGHT);
		stepz.setSpacing(25);

		VBox stepping = new VBox(timeStepTitle, steps, stepz);
		stepping.setSpacing(15);
		stepping.setBorder(new Border(new BorderStroke(Color.BLACK,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		stepping.setAlignment(Pos.CENTER);
		stepping.setPadding(new Insets(15, 5, 15, 5));

		//Seed Pane Title
		Label seedTitle = new Label("Seed Settings");

		//Seed Button && CurrentSeed Label
		TextField seed = new TextField();
		Button setSeed = new Button("Set");
		Label currentSeed = new Label("Current Seed:  ~~~~~~~~~~");
		setSeed.setOnAction(event -> {
			flag = false;
			int[] submission = seed.getCharacters().chars().toArray();
			long total = 0;
			total = condense(submission);
			if(!flag) {
				Critter.setSeed(total);
				lastSeed = Long.toString(total);
				currentSeed.setText("Current Seed: " + lastSeed);
			} else {
				displayAlert("Error", "Error: Invalid Input. Seed Unchanged.");
			}
		});

		//Seed Panes
		HBox changeSeed = new HBox(seed, setSeed);
		changeSeed.setAlignment(Pos.CENTER);
		changeSeed.setSpacing(15);

		VBox seedBox = new VBox(seedTitle, changeSeed, currentSeed);
		seedBox.setSpacing(15);
		seedBox.setAlignment(Pos.CENTER);
		seedBox.setBorder(new Border(new BorderStroke(Color.BLACK,
			BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		seedBox.setPadding(new Insets(15, 5, 15, 5));


		//End Program Button
		Button end = new Button("Close");
		end.setOnAction(event -> System.exit(0));
		end.setPadding(new Insets(15, 115, 15, 115));

		//RunStats Pane Title
		Label runStatsTitle = new Label("Critter Statistics");

		//RunStats Text Box
		TextArea stats = new TextArea();
		stats.setMaxSize(265, 300);
		stats.appendText("Testy Testicles");

		//RunStats ChoiceBox Header
		Label statsHeader = new Label("Critter: ");

		//RunStats ChoiceBox
		ChoiceBox<String> statOptions = new ChoiceBox<>();
		statOptions.getItems().add("Craig");
		statOptions.getItems().add("Tragic Critter");
		statOptions.getItems().add("Algae");
		statOptions.getItems().add("Algaephobic Critter");
		//TODO implement automatic scanner that detects Critter classes in the assignment package
		statOptions.setValue("Craig");

		//RunStats Pane
			HBox relevantCritter = new HBox(statsHeader, statOptions);
			relevantCritter.setAlignment(Pos.CENTER);

			VBox runStats = new VBox(runStatsTitle, relevantCritter, stats);
			runStats.setAlignment(Pos.CENTER);
			runStats.setSpacing(15);
			runStats.setBorder(new Border(new BorderStroke(Color.BLACK,
					BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			runStats.setAlignment(Pos.CENTER);
			runStats.setPadding(new Insets(15, 5, 15, 5));

		//Empty Spacing BOx
		VBox nothin = new VBox();

		//Right ControlPanel Pane
		VBox rightPane = new VBox();
		rightPane.getChildren().addAll(seedBox, quantities, stepping, runStats, end, nothin);
		rightPane.setSpacing(5);
		rightPane.setAlignment(Pos.CENTER);
		rightPane.setBorder(new Border(new BorderStroke(Color.BLACK,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


		//Animation 'Current'
		Label currentSPF = new Label(Integer.toString(stepsPerFrame));

		//Animation Steps/Frame Slider
		Slider animationScale = new Slider(0,25,5);
		animationScale.setShowTickMarks(true);
		animationScale.setShowTickLabels(true);
		stepsPerFrame = 5;
		animationScale.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				stepsPerFrame = (int)animationScale.getValue();
				currentSPF.setText(Integer.toString(stepsPerFrame));
			}
		});

		//Animation 'Animate' Button
		Button set = new Button("Start");
		set.setOnAction(event -> {
			for(int i = 0; i < stepInput; i++) {
				if(i % stepsPerFrame == 0) {
//					displayWorld();

					//TODO have displayWorld function here for the FX display
				}
			}
		});

		//Animation Pane Title
		Label bottomTitle = new Label("Animation");

		//Animation Control Pane
		HBox bottomControl = new HBox(currentSPF, animationScale, set);
		bottomControl.setMinHeight(50);
		bottomControl.setSpacing(15);
		bottomControl.setAlignment(Pos.CENTER);

		//Bottom Animation Pane
		VBox bottomPane = new VBox(bottomTitle, bottomControl);
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setBorder(new Border(new BorderStroke(Color.BLACK,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		bottomPane.setPadding(new Insets(15, 5, 15, 5));

		//BottomBottomPane (for the sxe visuals)
		VBox BottomBottomPane = new VBox(bottomPane);
		BottomBottomPane.setPadding(new Insets(5, 5, 15, 5));
		BottomBottomPane.setBorder(new Border(new BorderStroke(Color.BLACK,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));




		//BorderPane
		BorderPane border = new BorderPane();
		border.setBottom(BottomBottomPane);
		border.setRight(rightPane);
		border.setCenter(center);
		border.autosize();

		//Setting the Scene
		Scene scene = new Scene(border, 1500, 750);

		//And Off we Go
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void drawSquare(GraphicsContext gc, double x, double y, double length){
		gc.fillPolygon(new double[]{x, x+length, x+length, x},
				new double[]{y, y, y+length, y+length}, 4);
	}

	public void drawCircle(GraphicsContext gc, double x, double y, double diameter){
		gc.fillOval(x, y, diameter, diameter);
	}

	public void drawTriangle(GraphicsContext gc, double x, double y, double length){
		gc.fillPolygon(new double[]{x, x+length/2, x+length},
				new double[]{y+length, y, y+length}, 3);
	}

	public void drawDiamond(GraphicsContext gc, double x, double y, double length){
		gc.fillPolygon(new double[]{x, x+length/2, x+length, x+length/2},
				new double[]{y+length/2, y+length, y+length/2, y}, 4);
	}

	public void drawStar(GraphicsContext gc, double x, double y, double length){ //TODO make not cancerous
		drawTriangle(gc, x+5, y-5, length-10);
		gc.fillPolygon(new double[]{x+5, x+5+(length-10)/2, x+length-5},
				new double[]{y, y+length-10, y}, 3);
	}

	public void drawGrid(Canvas canvas, double cell_width, double cell_height){
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(),canvas.getHeight());
		// vertical lines
		gc.setStroke(Color.BLUE);
		for(int i = 0 ; i < Params.world_width; i++){
			gc.strokeLine(cell_width*i, 0, cell_width*i, canvas.getHeight());
		}
		// horizontal lines
		gc.setStroke(Color.RED);
		for(int i = 0 ; i < Params.world_height; i++){
			gc.strokeLine(0, cell_height*i, canvas.getWidth(), cell_height*i);
		}
	}

	public void drawCritter(Canvas canvas, Critter critter, int cell_x, int cell_y){
		GraphicsContext gc = canvas.getGraphicsContext2D();
		double cell_width = canvas.getWidth()/Params.world_width;
		double cell_height = canvas.getHeight()/Params.world_width;

		double length, padding = 5, x_offset = padding/2, y_offset = padding/2;
		if(cell_width<cell_height){
			length = cell_width;
			y_offset += (cell_height-cell_width)/2;
		}else{
			length = cell_height;
			x_offset += (cell_width-cell_height)/2;
		}

		gc.setFill(critter.viewFillColor());
		gc.setStroke(critter.viewOutlineColor());

		switch (critter.viewShape()){
			case CIRCLE: 	drawCircle(gc, x_offset + cell_x*cell_width, y_offset + cell_y*cell_height, length-padding);
						 	break;
			case SQUARE: 	drawSquare(gc, x_offset + cell_x*cell_width, y_offset + cell_y*cell_height, length-padding);
							break;
			case TRIANGLE: 	drawTriangle(gc, x_offset + cell_x*cell_width, y_offset + cell_y*cell_height, length-padding);
							break;
			case DIAMOND: 	drawDiamond(gc, x_offset + cell_x*cell_width, y_offset + cell_y*cell_height, length-padding);
							break;
			case STAR:		drawStar(gc, x_offset + cell_x*cell_width, y_offset + cell_y*cell_height, length-padding);
							break;
		}
	}

	public void displayWorld(Canvas canvas){
		ArrayList<Critter>[][] world = Critter.displayWorld();

		int width = (int) canvas.getWidth();
		int height = (int) canvas.getHeight();
		double cell_width = canvas.getWidth()/Params.world_width;
		double cell_height = canvas.getHeight()/Params.world_width;

		GraphicsContext gc = canvas.getGraphicsContext2D();
		drawGrid(canvas, cell_width, cell_height);

		for(int i = 0; i < Params.world_width; i++) {
			for(int j = 0; j < Params.world_height; j++) {
				if(world[i][j]!=null && world[i][j].size()>0){
					Critter critter = (Critter)world[i][j].get(0);
					drawCritter(canvas, critter, i, j);
				}
			}
		}
	}

	@Override
	public void handle(ActionEvent event) {
		System.out.println("Error- ActionEvent not Handled: " + event.getSource());
	}

	private void displayAlert(String title, String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setHeight(100);

		Label errorMessage = new Label(message);
		errorMessage.setPrefWidth(150 + message.length() * 5);
		errorMessage.setAlignment(Pos.CENTER);
		Button close = new Button("Okay");
		close.setOnAction(event -> window.close());

		window.setWidth(errorMessage.getPrefWidth());

		VBox layout = new VBox();
		layout.getChildren().addAll(errorMessage, close);
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(15);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}

	private static boolean answer;
	private boolean confirmAlert(String title, String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setHeight(100);

		Label errorMessage = new Label(message);
		errorMessage.setPrefWidth(150 + message.length() * 5);
		errorMessage.setAlignment(Pos.CENTER);
		window.setWidth(errorMessage.getPrefWidth());

		Button yes = new Button("yes");
		Button no =  new Button("no");

		yes.setOnAction(event -> {
			window.close();
			answer = true;
		});
		no.setOnAction(event -> {
			window.close();
			answer = false;
		});


		HBox answers = new HBox(yes, no);
		answers.setSpacing(15);
		answers.setAlignment(Pos.CENTER);
		VBox layout = new VBox();
		layout.getChildren().addAll(errorMessage, answers);
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(15);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

		return answer;
	}

	private static long alertInput;
	private boolean flag;
	private long inputAlert(String title, String input) {
		flag = false;

		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setHeight(100);

		Label errorMessage = new Label(input);
		errorMessage.setPrefWidth(150 + input.length() * 5);
		errorMessage.setAlignment(Pos.CENTER);
		window.setWidth(errorMessage.getPrefWidth());

		TextField alert = new TextField();
		Button submit = new Button("Submit");

		submit.setOnAction(event -> {
			int[] inputs = alert.getCharacters().chars().toArray();
			int total = condense(inputs);
			if(!flag) {
				alertInput = total;
			} else {
				alertInput = 100000000; 	//sets to be flagged later on
			}
			window.close();
		});

		HBox submission = new HBox(alert, submit);
		submission.setSpacing(15);
		submission.setAlignment(Pos.CENTER);
		VBox layout = new VBox();
		layout.getChildren().addAll(errorMessage, submission);
		layout.setAlignment(Pos.CENTER);
		layout.setSpacing(15);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		return alertInput;
	}

	private int condense(int[] inputs) {
		int total = 0;
		flag = false;
		for(int i = 0; i < inputs.length; i++) {
			inputs[i] -= 48;
			if(inputs[i] < 0 || inputs[i] > 9) {
				flag = true;
				break;
			}
			total *= 10;
			total += inputs[i];
		}
		return total;
	}
}