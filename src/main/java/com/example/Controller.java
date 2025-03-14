package com.example;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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
    private double ammoSpeedY = 6.0;
    private double enemyAmmoSpeedY = 4.0; 
    private int waveNum = 1;
    private int enemyHealth = 1;
    private int[] costOfGoldPerHit = {0, 10, 20, 40, 80, 120, 160, 240, 360};
    private int goldPerHitLevel = 0;
    double moveSpeed = 5;
    int playerDamage = 1;
    List<Enemy> enemiesList = new ArrayList<>();
    List<Line> ammoList = new ArrayList<>();
    List<Line> enemyAmmoList = new ArrayList<>();

    @FXML
    Circle player;
    double playerX;
    double playerY;
    int gold;
    int goldPerHit = 1;
    @FXML
    Label goldCount;
    @FXML
    Label damageDealt;
    @FXML
    Label waveCount;
    @FXML
    private MediaView mediaView;

    // methods
    public void onPlayAudio() {
        try {
            String path = getClass().getResource("static/shoot.wav").toURI().toString();
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

    public void doublePlayerDamage() {
        if(gold >= playerDamage*10) {
            gold -= playerDamage*10;
            playerDamage = playerDamage*2;
            goldCount.setText(Integer.valueOf(gold).toString());
        }
        else {
            System.out.println("NOT ENOUGH GOLD! - need: " + playerDamage*10);
        }        
    }
    
    public void doubleGoldPerHit() {
        if(goldPerHitLevel+1 < 9) {
            int costOfUpgrade = costOfGoldPerHit[goldPerHitLevel+1];
            if(gold >= costOfUpgrade) {
                goldPerHit *= 2;
                gold -= costOfUpgrade;
                goldCount.setText(Integer.valueOf(gold).toString());
                goldPerHitLevel += 1;
            }
            else {
                System.out.println("NOT ENOUGH GOLD! - need: " + costOfUpgrade);

            }
        }
        else {
            System.out.println("REACHED MAX LEVEL!!");
        }
    }

    public void shootAmmo(Pane rootPane) {
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

    
    public void shootEnemyAmmo(Pane rootPane) {
        Random random = new Random();
        boolean[] shootOrNo = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true};
        int shootRandomChoice = random.nextInt(shootOrNo.length);
        boolean shoot = shootOrNo[shootRandomChoice];

        for(Enemy enemyObject : enemiesList) {
            if(enemyObject.shoot && shoot) {
                Line ammo = new Line();
                ammo.setFill(Color.WHITE);
                ammo.setStroke(Color.WHITE);
                double ammoStartX;
                double ammoStartY;
                double ammoEndX;
                double ammoEndY;

                ammoStartX = enemyObject.getCenterX();
                ammoStartY = enemyObject.getCenterY();

                ammoEndX = enemyObject.getCenterX();
                ammoEndY = enemyObject.getCenterY()+10;

                ammo.setStartX(ammoStartX);
                ammo.setEndX(ammoEndX);
                ammo.setStartY(ammoStartY);
                ammo.setEndY(ammoEndY);
                
                rootPane.getChildren().add(ammo);
                enemyAmmoList.add(ammo);                   
            }
        }
    }

    public void moveAmmo(Pane rootPane) {
        ammoList = checkAmmos(rootPane);
        for(Line ammo : ammoList) {
            double newStartY = ammo.getStartY()-ammoSpeedY;
            double newEndY = ammo.getEndY()-ammoSpeedY;
            ammo.setStartY(newStartY);
            ammo.setEndY(newEndY);
        }
    }

    public void moveEnemyAmmo(Pane rootPane, double width) {
        enemyAmmoList = checkEnemyAmmos(rootPane, width);
        for(Line ammo : enemyAmmoList) {
            double newStartY = ammo.getStartY()+enemyAmmoSpeedY;
            double newEndY = ammo.getEndY()+enemyAmmoSpeedY;
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
                    boolean damaged = enemyObject.damage(playerDamage);
                    gold += goldPerHit;
                    rootPane.getChildren().remove(ammo);
                    ammoToRemove.add(ammo);
                    if(damaged) {
                        gold += 10;
                        enemiesToRemove.add(enemyObject);
                    }
                    goldCount.setText(Integer.valueOf(gold).toString());
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

    public void checkIfPlayerHit(Pane rootPane, Stage stage) {
        double startX = player.getCenterX()-player.getRadius()+300;
        double endX = player.getCenterX()+player.getRadius()+300;
        double startY = player.getCenterY()-player.getRadius()+333;
        double endY = player.getCenterY()+player.getRadius()+333;

        for(Line ammo : enemyAmmoList) {
            if((ammo.getStartX() >= startX && ammo.getEndX() <= endX) && (ammo.getStartY() >= startY && ammo.getEndY() <= endY)) {
                System.out.println("YOU DIED");
                stage.close();
            }
        }
    }

    void spawnEnemies(Pane rootPane, Stage stage, int spawnNum, int enemyHealth, double speed) {
        boolean downByOne = false;
        int enemySpawnDistanceX = 0;
        int enemySpawnDistanceY = 0;
        while(spawnNum > 0) {
            Enemy enemyObj = new Enemy(enemyHealth, speed, damageDealt, waveNum);

            enemyObj.setCenterX(250+enemySpawnDistanceX);
            enemyObj.setCenterY(100+enemySpawnDistanceY);
            enemyObj.setRadius(26);
            enemyObj.setFill(Color.RED);

            rootPane.getChildren().add(enemyObj);
            enemiesList.add(enemyObj);
            
            spawnNum--;
            if(enemySpawnDistanceY > 0 && !downByOne && enemySpawnDistanceX-80>= 0) {
                enemySpawnDistanceY+=80;
                enemySpawnDistanceX-=80;
                downByOne = true;
            }
            else if(downByOne) {
                enemySpawnDistanceX-=80;
            }
            else {
                if(enemySpawnDistanceX+80+250 >= stage.getWidth()) {
                    enemySpawnDistanceY+=80;
                    enemySpawnDistanceX-=80;
                    downByOne = true;
                }
                else {
                    enemySpawnDistanceX+=80;
                }                
            }

        }
        downByOne = false;
    }
    
    public void moveEnemies(Stage stage) {
        for(Enemy enemyObject : enemiesList) {
            double enemySpeedX = enemyObject.speed;
            double newCenterX = enemyObject.getCenterX();
            if(newCenterX >= stage.getWidth()-5 || newCenterX <= 0) {
                enemySpeedX = -enemySpeedX;
                enemyObject.speed = enemySpeedX;
            }
            newCenterX += enemySpeedX;
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

    private List<Line> checkEnemyAmmos(Pane rootPane, double width) {
        List<Line> inRangeAmmos = new ArrayList<>();
        for(Line ammo : enemyAmmoList) {
            if(ammo.getStartY() <= width) {
                inRangeAmmos.add(ammo);
            }
            else {
                rootPane.getChildren().remove(ammo);
            }
        }
        return inRangeAmmos;
    }

    private boolean checkWave() {
        List<Enemy> aliveEnemies = new ArrayList<>();
        for(Enemy enemyObject : enemiesList) {
            if(enemyObject.hp > 0) {
                aliveEnemies.add(enemyObject);
            }
        }
        if(aliveEnemies.isEmpty()) {
            return true;
        }
        return false;
    }

    public void checkNewWave(Pane rootPane, Stage stage) {
        boolean waveEnded = checkWave();
        if(waveEnded) {
            enemyHealth *= 2;
            spawnEnemies(rootPane, stage, 2*waveNum, enemyHealth, 5);
            waveNum++;
            waveCount.setText(Integer.valueOf(waveNum).toString());
        }
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

    public void touchEnemy(@SuppressWarnings("exports") Circle player, @SuppressWarnings("exports") Stage stage) {
        HashMap<Enemy, List<Double>> enemiesPos = getEnemyPos();
        double playerStartX = player.getCenterX()-player.getRadius()+300;
        double playerEndX = player.getCenterX()+player.getRadius()+300;
        double playerStartY = player.getCenterY()-player.getRadius()+333;
        double playerEndY = player.getCenterY()+player.getRadius()+333;

        for(Enemy object : enemiesPos.keySet()) {
            List<Double> enemyPosition = enemiesPos.get(object);
            if((playerStartX >= enemyPosition.get(0) && playerEndX <= enemyPosition.get(1)) && (playerStartY <= enemyPosition.get(3) && playerEndY >= enemyPosition.get(2))) {
                System.out.println("YOU DIED");
                stage.close();
            }
        }
    }
}