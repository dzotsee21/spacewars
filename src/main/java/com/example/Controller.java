package com.example;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller {
    private double moveSpeed = 4;
    private double speedX = 5.0;
    List<Enemy> enemiesList = new ArrayList<>();

    @FXML
    private Circle player;
    double playerX;
    double playerY;
    @FXML
    Label damageDealt;
    @FXML
    private MediaView mediaView;

    // methods
    public void onPlayAudio() {
        try {
            String path = getClass().getResource("shoot.wav").toURI().toString();
            Media media = new Media(path);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
        } catch(Exception e) {
            e.printStackTrace();
        }
        mediaView.getMediaPlayer().play();       
    }

    public void moveUp() {
        player.setCenterY(playerY-=moveSpeed);
    }

    public void moveDown() {
        player.setCenterY(playerY+=moveSpeed);
    }

    public void moveRight() {
        player.setCenterX(playerX+=moveSpeed);
    }

    public void moveLeft() {
        player.setCenterX(playerX-=moveSpeed);
    }

    public Line shootAmmo(Pane rootPane) {
        Line ammo = new Line();
        ammo.setFill(Color.WHITE);
        ammo.setStroke(Color.WHITE);
        double ammoStartX;
        double ammoStartY;
        double ammoEndX;
        double ammoEndY;

        ammo.setFill(Color.AQUA);
        ammoStartX = playerX+300;
        ammoStartY = playerY+313;

        ammoEndX = playerX+300;
        ammoEndY = playerY+333;
        
        ammo.setStartX(ammoStartX);
        ammo.setStartY(ammoStartY);

        ammo.setEndX(ammoEndX);
        ammo.setEndY(ammoEndY);

        rootPane.getChildren().add(ammo);

        TranslateTransition translate = new TranslateTransition();
        translate.setNode(ammo);
        translate.setByY(ammo.getStartY()-1500);
        translate.setDuration(Duration.millis(1000));
        translate.play();

        return ammo;
    }

    public void spawnEnemies(Pane rootPane, Stage stage, int spawnNum) {
        int distance = 0;
        while(spawnNum > 0) {
            Enemy enemyObj = new Enemy(4, damageDealt);

            enemyObj.setCenterX(250+distance);
            enemyObj.setCenterY(100);
            enemyObj.setRadius(26);
            enemyObj.setFill(Color.RED);

            rootPane.getChildren().add(enemyObj);

            enemiesList.add(enemyObj);
            
            spawnNum--;
            distance+=80;
        }
    }

    public void moveEnemies(Stage stage) {
        for(Enemy enemyObject : enemiesList) {
            double newCenterX = enemyObject.getCenterX()+speedX;
            if(newCenterX >= stage.getWidth()-5 || newCenterX <= 0) {
                speedX = -speedX;
            }
            enemyObject.setCenterX(newCenterX);
        }
    }

    private List<Enemy> checkEnemies() {
        List<Enemy> aliveEnemies = new ArrayList<>();
        for(Enemy enemyObject : enemiesList) {
            if(enemyObject.hp > 0) {
                aliveEnemies.add(enemyObject);
            }
        }
        return aliveEnemies;
    }

    public HashMap<Enemy, List<Double>> getEnemyPos() {
        HashMap<Enemy, List<Double>> enemiesPos = new HashMap<Enemy, List<Double>>();
        enemiesList = checkEnemies();
        for(Enemy enemyObject : enemiesList) {
            double startX = ((enemyObject.getCenterX())-enemyObject.getRadius());
            double endX = ((enemyObject.getCenterX())+enemyObject.getRadius());
            double startY = enemyObject.getCenterY();

            enemiesPos.put(enemyObject, Arrays.asList(startX, endX, startY));
        }
        return enemiesPos;
    }
}