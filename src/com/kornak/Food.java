package com.kornak;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;


public class Food extends GameObject{
    Food() {
        super(new Circle(15,15,15, Color.BLACK));
    }

    public void moveDown(int y) {
        //setVelocity(new Point2D.Double(0,0));
        setVelocity(new Point2D.Double(0, y));

    }
}
