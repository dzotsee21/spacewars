package com.example;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class Controller {
    private double moveSpeed = 4;

    private double ammoStartX;
    private double ammoStartY;
    private double ammoEndX;
    private double ammoEndY;

    @FXML
    private Circle player;
    double playerX;
    double playerY;
    @FXML
    private Circle enemyObject;
    private double enemyObjectX;
    private double enmyObjectY;

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

    public Line shootAmmo() {
        Line ammo = new Line();
        ammo.setFill(Color.AQUA);
        ammoStartX = playerX+300;
        ammoStartY = playerY+313;

        ammoEndX = playerX+300;
        ammoEndY = playerY+333;
        
        ammo.setStartX(ammoStartX);
        ammo.setStartY(ammoStartY);

        ammo.setEndX(ammoEndX);
        ammo.setEndY(ammoEndY);

        return ammo;
    }

    public HashMap<Circle, List<Double>> getEnemyPos() {
        double startX = ((enemyObject.getCenterX()+300)-enemyObject.getRadius());
        double endX = ((enemyObject.getCenterX()+300)+enemyObject.getRadius());

        HashMap<Circle, List<Double>> enemyPos = new HashMap<Circle, List<Double>>();
        enemyPos.put(enemyObject, Arrays.asList(startX, endX));

        return enemyPos;
    }
}