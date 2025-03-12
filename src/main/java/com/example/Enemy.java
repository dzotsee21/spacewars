package com.example;

import java.util.Random;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Enemy extends Circle{
    int hp;
    double speed;
    Label damageDealt;


    Enemy(int hp, double speed, Label damageDealt) {
        Random random = new Random();
        double[] leftOrRightSpeed = {speed, -speed};
        int randomChoice = random.nextInt(leftOrRightSpeed.length);
        this.hp = hp;
        this.speed = leftOrRightSpeed[randomChoice];
        this.damageDealt = damageDealt;
    }

    public boolean damage(int dmg) {
        int damageTaken = dmg;
        if(dmg > this.hp) {
            damageTaken = this.hp;
        }
        this.hp -= dmg;
        damageDealt.setText(Integer.valueOf(Integer.parseInt(damageDealt.getText())+damageTaken).toString());
        if(this.hp <= 0) {
            return true;
        }
        return false;
    }
}
