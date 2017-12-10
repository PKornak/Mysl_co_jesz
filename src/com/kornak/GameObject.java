package com.kornak;

import javafx.scene.Node;
import java.awt.geom.Point2D;

public class GameObject {

    private boolean exist = true;
    private Node view;

    public Point2D velocity = new Point2D.Double(0,0);



    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public Node getView() {
        return view;
    }

    public boolean isFlying(){
        return exist;
    }

    public void setFlying(boolean exist){
        this.exist=exist;
    }

    public void update() {
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    public GameObject(Node view){
        this.view = view;
    }

    public boolean isColliding(GameObject other) {
        return getView().getBoundsInParent().intersects((other.getView().getBoundsInParent()));
    }

    public boolean isDefeat(){
        return !exist;
    }

}
