package assignment5;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.util.stream.IntStream;

public class Main extends Application implements EventHandler<ActionEvent>{

	private int stepInput = 0;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setTitle("Critter World");

		Button singleStep = new Button();
		singleStep.setText("World Time Step(s)");
		singleStep.setOnAction(event -> {
            for(int i = 0; i < stepInput; i++) {
				Critter.worldTimeStep();
			}
        });



		Label label1 = new Label("Desired Steps: ");
		TextField textField = new TextField ();
		HBox hb = new HBox();
		Button submit = new Button("Submit");
		hb.getChildren().addAll(label1, textField, submit, singleStep);
		hb.setSpacing(10);
		submit.setOnAction(event -> {
			int[] input = textField.getCharacters().codePoints().toArray();
			//TODO check for invalid input
			int total = 0;
			for (int i = 0; i < input.length; i++) {
				input[i] -= 48;
				total *= 10;
				total += input[i];
			}
			//TODO check for excessive worldStep count
			stepInput = total;
		});

		StackPane layout = new StackPane();
		layout.getChildren().addAll(hb);

		Scene scene = new Scene(layout, 500, 250);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void handle(ActionEvent event) {
		System.out.println("Error- ActionEvent not Handled: " + event.getSource());
	}
}