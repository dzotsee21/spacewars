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

        controller.spawnEnemies(rootPane, stage, 1);

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

                controller.moveEnemies(stage);
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
                    HashMap<Enemy, List<Double>> enemyPositions = controller.getEnemyPos();
                    Line ammo = controller.shootAmmo(rootPane);

                    for(Enemy object : enemyPositions.keySet()) {
                        List<Double> enemyPosition = enemyPositions.get(object);
                        if(ammo.getStartX() >= enemyPosition.get(0) && ammo.getStartX() <= enemyPosition.get(1) && ammo.getStartY() >= enemyPosition.get(2)) {
                            boolean damaged = object.damage(1);
                            if(damaged) {
                                rootPane.getChildren().remove(object);
                            }
                            else {
                                System.out.println("DAMAGE DEALT!");
                            }
                        }
                    }
                    break;
            }
        });
    }
}