package com.kornak;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;


public class Player extends GameObject {
    Player() {
        super(new Rectangle(40, 20, Color.RED));
    }

    public void moveLeft() {
        //setVelocity(new Point2D.Double(0,0));
        setVelocity(new Point2D.Double(-3, velocity.getY()));

    }

    public void moveRight() {
        setVelocity(new Point2D.Double(3, velocity.getY()));

    }

    public void stop() {
        setVelocity(new Point2D.Double(0, 0));
    }
}
