package com.example;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Enemy extends Circle{
    int hp;
    Label damageDealt;


    Enemy(int hp, Label damageDealt) {
        this.hp = hp;
        this.damageDealt = damageDealt;
    }

    public boolean damage(int dmg) {
        this.hp -= dmg;
        damageDealt.setText(Integer.valueOf(Integer.parseInt(damageDealt.getText())+dmg).toString());
        if(this.hp <= 0) {
            return true;
        }
        return false;
    }
}
