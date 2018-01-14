package com.kornak;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;

/**
 * Klasa odpowiedzialna za obiekt gracza w grze
 */
public class Player extends GameObject {
    private Image player_right = new Image("img/player_right.png",false);
    private Image player_left = new Image("img/player_left.png",false);
    private Circle circle;
    Player() {
        super();
        circle = new Circle(0,0,70, Color.rgb(0,0,0));
        circle.setFill(new ImagePattern(player_right));
        super.setView(circle);

    }

    /**
     *  Weryfikuje kierunek poruszania się gracza, zmienia model, oraz zabezpiecza przed wyjściem poza obszar gry
     */
    @Override
    public void update(){
        if(getView().getBoundsInParent().getMinX() >= 10 && velocity.getX() < 0) {
            circle.setFill(new ImagePattern(player_left));
            getView().setTranslateX(getView().getTranslateX() + velocity.getX());
        }
        if(getView().getBoundsInParent().getMaxX() < 724 && velocity.getX() > 0) {
            circle.setFill(new ImagePattern(player_right));
            getView().setTranslateX(getView().getTranslateX() + velocity.getX());
        }
    }

    /**
     * Porusza gracza w lewo
     */
    public void moveLeft() {
        setVelocity(new Point2D.Double(-10, velocity.getY()));
    }

    /**
     * Porusza gracza w prawo
     */
    public void moveRight() {
        setVelocity(new Point2D.Double(10, velocity.getY()));
    }

    /**
     * Zatrzymanie gracza
     */
    public void stop() {
        setVelocity(new Point2D.Double(0, 0));
    }

}
