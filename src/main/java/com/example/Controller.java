package com.example;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;

public class Controller {
    double moveSpeed = 2;

    @FXML
    private Circle player;
    double x;
    double y;
    @FXML
    private MediaView mediaView;

    @FXML
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
        player.setCenterY(y-=moveSpeed);
    }

    public void moveDown() {
        player.setCenterY(y+=moveSpeed);
    }

    public void moveRight() {
        player.setCenterX(x+=moveSpeed);
    }

    public void moveLeft() {
        player.setCenterX(x-=moveSpeed);
    }

}