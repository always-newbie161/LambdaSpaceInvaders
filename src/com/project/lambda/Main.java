package com.project.lambda;


import javafx.application.Application;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;


import javafx.scene.layout.*;

import javafx.scene.Scene;

import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.util.List;
import java.util.stream.Collectors;


public class Main extends Application {

    private Controller controller;
    private Controller login;


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Pane hroot = homeLoader.load();
        login = homeLoader.getController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("invaders.fxml"));
        VBox root = loader.load();
        controller = loader.getController();


	    /*menubar.setOnMouseEntered(e->{
	    	menubar.setVisible(true);
	    });
	    menubar.setOnDragExited(e ->{
	    	menubar.setVisible(false);
	    });*/
        Scene hscene = new Scene(hroot);
        controller.scene = new Scene(root);
        primaryStage.setWidth(800);
        primaryStage.setScene(hscene);
        controller.onMusic("LNbgm.mp3");
        List<Label> labels = hroot.getChildren().stream().filter(n -> n.getClass() == Label.class).map(i -> (Label) i).collect(Collectors.toList());
        for (Label i : labels) {
            primaryStage.setTitle("The Long Knight");
            i.setOnMouseEntered(e -> {
                login.nking.setImage(controller.createImage("homepic2.jpg"));
                i.setTextFill(Color.valueOf("#D4AF37"));
                i.setStyle("-fx-border-color: #D4AF37;-fx-border-width: 3; -fx-border-radius: 15;");

            });

            i.setOnMouseExited(e -> {
                login.nking.setImage(controller.createImage("homepic1.png"));
                i.setTextFill(Color.valueOf("#087194"));
                i.setStyle("-fx-border-color: #087194;-fx-border-width: 3; -fx-border-radius: 15;");

            });
        }

        login.start.setOnMouseClicked(e -> {
            controller.createContent();
            primaryStage.setWidth(800);
            primaryStage.setScene(controller.scene);
        });
        login.exit.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setResizable(false);
        primaryStage.show();

    }




}