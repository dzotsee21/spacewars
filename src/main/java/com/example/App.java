package com.example;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Line;

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
    private boolean pressedSpace = false;
    private boolean pressedShift = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        root.setId("pane");
        Controller controller = loader.getController();
        Scene scene = new Scene(root);
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

        Pane rootPane = (Pane) root;

        controller.spawnEnemies(rootPane, stage, 3, 25, 5);

        setupKeyHandlers(scene, controller, rootPane);
        mainGameLoop(controller, stage, rootPane);

        stage.setScene(scene);
        stage.show();
    }

    private void mainGameLoop(Controller controller, Stage stage, Pane rootPane) {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.moveEnemies(stage);
                controller.touchEnemy(controller.player, stage);

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
                if(pressedShift) {
                    controller.moveSpeed = 7;
                }
                else if(!pressedShift) {
                    controller.moveSpeed = 5;
                }
                if(pressedSpace) {
                    controller.onPlayAudio();
                    controller.shootAmmo(rootPane);
                    controller.shootEnemyAmmo(rootPane);
                    pressedSpace = false;
                }
                
                controller.moveAmmo(rootPane);
                controller.moveEnemyAmmo(rootPane, stage.getWidth());
                controller.checkIfHit(rootPane);
                controller.checkIfPlayerHit(rootPane);
                controller.checkNewWave(rootPane, stage);
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
                case SHIFT:
                    pressedShift = true;
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
                case SHIFT:
                    pressedShift = false;
                    break;
                case SPACE:
                    pressedSpace = true;
                    break;
            }
        });
    }
}