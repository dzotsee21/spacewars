package com.example;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

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
    private double EnemySpeedX = 5.0;
    private double ammoSpeedY = -5.0;
    List<Enemy> enemiesList = new ArrayList<>();
    List<Line> ammoList = new ArrayList<>();

    @FXML
    Circle player;
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

    public void shootAmmo(Pane rootPane, Stage stage) {
        Line ammo = new Line();
        ammo.setFill(Color.WHITE);
        ammo.setStroke(Color.WHITE);
        double ammoStartX;
        double ammoStartY;
        double ammoEndX;
        double ammoEndY;

        ammoStartX = playerX+300;
        ammoStartY = playerY+313;

        ammoEndX = playerX+300;
        ammoEndY = playerY+333;

        ammo.setStartX(ammoStartX);
        ammo.setEndX(ammoEndX);
        ammo.setStartY(ammoStartY);
        ammo.setEndY(ammoEndY);

        rootPane.getChildren().add(ammo);
        ammoList.add(ammo);        
    }

    public void moveAmmo(Pane rootPane) {
        ammoList = checkAmmos(rootPane);
        for(Line ammo : ammoList) {
            double newStartY = ammo.getStartY()+ammoSpeedY;
            double newEndY = ammo.getEndY()+ammoSpeedY;
            ammo.setStartY(newStartY);
            ammo.setEndY(newEndY);
        }
    }

    public void checkIfHit(Pane rootPane) {
        HashMap<Enemy, List<Double>> enemyPositions = getEnemyPos();
        List<Line> ammoToRemove = new ArrayList<>();
        List<Enemy> enemiesToRemove = new ArrayList<>();
        for(Line ammo : ammoList) {
            for(Enemy enemyObject : enemyPositions.keySet()) {
                List<Double> enemyPosition = enemyPositions.get(enemyObject);
                if((ammo.getStartX() >= enemyPosition.get(0) && ammo.getStartX() <= enemyPosition.get(1)) && (ammo.getStartY() >= enemyPosition.get(2) && ammo.getStartY() <= enemyPosition.get(3))) {
                    boolean damaged = enemyObject.damage(10);
                    rootPane.getChildren().remove(ammo);
                    ammoToRemove.add(ammo);
                    if(damaged) {
                        enemiesToRemove.add(enemyObject);
                    }
                    else {
                        System.out.println("HIIT BITCH! HIT!");
                    }
                    break;
                }
            }
        }

        for(Enemy enemyObject : enemiesToRemove) {
            enemiesList.remove(enemyObject);
            rootPane.getChildren().remove(enemyObject);
        }

        for(Line ammo : ammoToRemove) {
            ammoList.remove(ammo);
        }
    }

    public void spawnEnemies(Pane rootPane, Stage stage, int spawnNum, int enemyHealth) {
        int distance = 0;
        while(spawnNum > 0) {
            Enemy enemyObj = new Enemy(enemyHealth, damageDealt);

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
            double newCenterX = enemyObject.getCenterX()+EnemySpeedX;
            if(newCenterX >= stage.getWidth()-5 || newCenterX <= 0) {
                EnemySpeedX = -EnemySpeedX;
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

    private List<Line> checkAmmos(Pane rootPane) {
        List<Line> inRangeAmmos = new ArrayList<>();
        for(Line ammo : ammoList) {
            if(ammo.getStartY() >= -10) {
                inRangeAmmos.add(ammo);  
            }
            else {
                rootPane.getChildren().remove(ammo);
            }
        }
        return inRangeAmmos;
    }

    public HashMap<Enemy, List<Double>> getEnemyPos() {
        HashMap<Enemy, List<Double>> enemiesPos = new HashMap<Enemy, List<Double>>();
        enemiesList = checkEnemies();
        for(Enemy enemyObject : enemiesList) {
            double startX = enemyObject.getCenterX()-enemyObject.getRadius();
            double endX = enemyObject.getCenterX()+enemyObject.getRadius();
            double startY = enemyObject.getCenterY()-enemyObject.getRadius();
            double endY = enemyObject.getCenterY()+enemyObject.getRadius();

            enemiesPos.put(enemyObject, Arrays.asList(startX, endX, startY, endY));
        }
        return enemiesPos;
    }

    public void touchEnemy(Circle player, Stage stage) {
        HashMap<Enemy, List<Double>> enemiesPos = getEnemyPos();
        double playerPosX = playerX+300;
        double playerPosY = playerY+333;

        for(Enemy object : enemiesPos.keySet()) {
            List<Double> enemyPosition = enemiesPos.get(object);
            if((playerPosX >= enemyPosition.get(0) && playerPosX <= enemyPosition.get(1)) && (playerPosY <= enemyPosition.get(3) && playerPosY >= enemyPosition.get(2))) {
                System.out.println("YOU DIED!");
                stage.close();
            }
        }
    }
}