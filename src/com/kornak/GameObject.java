package com.kornak;

import javafx.scene.Node;

import java.awt.geom.Point2D;

public class GameObject {

    public GameObject(Node view){
        this.view = view;
    }
    public GameObject() { }
    public void setView(Node view) { this.view = view; }

    private boolean exist = true;
    private Node view;

    public Point2D velocity = new Point2D.Double(0,0);

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVisibility(boolean state){
        view.setVisible(state);
    }


    public boolean isFlying(){
        return exist;
    }

    public void setFlying(boolean exist){
        this.exist=exist;
    }

    public void update() {
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    public Node getView() {
        return view;
    }

    public boolean isColliding(GameObject other) {
        return getView().getBoundsInParent().intersects((other.getView().getBoundsInParent()));
    }

    public boolean isDefeat(){
        return !exist;
    }


}
