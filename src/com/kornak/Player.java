package com.kornak;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;


public class Player extends GameObject {
    Player() {
        super(new Rectangle(150, 30, Color.RED));
    }

    public void moveLeft() {
        setVelocity(new Point2D.Double(-10, velocity.getY()));

    }

    public void moveRight() {
        setVelocity(new Point2D.Double(10, velocity.getY()));

    }

    public void stop() {
        setVelocity(new Point2D.Double(0, 0));
    }

}
