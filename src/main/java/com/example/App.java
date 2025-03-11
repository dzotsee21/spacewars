package com.example;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {
    private boolean pressedForward = false;
    private boolean pressedBackward = false;
    private boolean pressedRight = false;
    private boolean pressedLeft = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        Scene scene = new Scene(root);


        Pane rootPane = (Pane) root;

        setupKeyHandlers(scene, controller, rootPane);
        mainGameLoop(controller, stage);

        stage.setScene(scene);
        stage.show();
    }

    private void mainGameLoop(Controller controller, Stage stage) {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // movements
                if(pressedForward && (controller.playerY+333) > 0) {
                        controller.moveUp();
                }
                if(pressedBackward && (controller.playerY+333) < stage.getHeight()-40) {
                    controller.moveDown();
                }
                if(pressedRight && (controller.playerX+300) < stage.getWidth()-5) {
                    controller.moveRight();
                }
                if(pressedLeft && (controller.playerX+300) > 0) {
                    controller.moveLeft();
                }
            }
        };
        gameLoop.start();
    }

    private void setupKeyHandlers(Scene scene, Controller controller, Pane rootPane) {
        scene.setOnKeyPressed(event -> {
            switch(event.getCode()) {
                case W:
                    pressedForward = true;
                    break;
                case S:
                    pressedBackward = true;
                    break;
                case D:
                    pressedRight = true;
                    break;
                case A:
                    pressedLeft = true;
                    break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch(event.getCode()) {
                case W:
                    pressedForward = false;
                    break;
                case S:
                    pressedBackward = false;
                    break;
                case D:
                    pressedRight = false;
                    break;
                case A:
                    pressedLeft = false;
                    break; 
                case SPACE:
                    controller.onPlayAudio();
                    HashMap<Circle, List<Double>> enemyPositions = controller.getEnemyPos();
                    Line ammo = controller.shootAmmo();

                    rootPane.getChildren().add(ammo);

                    TranslateTransition translate = new TranslateTransition();
                    translate.setNode(ammo);
                    translate.setByY(ammo.getStartY()-1500);
                    translate.setDuration(Duration.millis(1000));
                    translate.play();

                    for(Circle object : enemyPositions.keySet()) {
                        if(ammo.getStartX() >= enemyPositions.get(object).get(0) && ammo.getStartX() <= enemyPositions.get(object).get(1)) {
                            rootPane.getChildren().remove(object);
                        }
                    }

                    break;
            }
        });
    }
}