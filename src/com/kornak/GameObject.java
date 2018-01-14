package com.kornak;

import javafx.scene.Node;
import java.awt.geom.Point2D;

/**
 * Klasa główna dla obiektów w grze
 */
public class GameObject {

    public GameObject() { }
    public void setView(Node view) { this.view = view; }

    private boolean exist = true;
    private Node view;

    /**
     * Wartość prędkości x, y
     */
    public Point2D velocity = new Point2D.Double(0,0);


    /** Ustala wartość prędkości
     *
     * @param velocity parametr Point2D zawierający prędkość
     */
    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    /**
     * Zwraca prędkość obiektu
     */
    public Point2D getVelocity() {
        return velocity;
    }

    /** Zmienia ustawienie widzialności obiektu w zależności od parametru
     *
     * @param state true/false aby zmienić widzialność obiektu
     */
    public void setVisibility(boolean state){
        view.setVisible(state);
    }


    /** Ustala ze obiekt lata
     *
     * @param exist - wartosc true/false
     */
    public void setFlying(boolean exist){
        this.exist=exist;
    }

    /** Odświeżanie pozycji obiektu
     *
     */
    public void update() {
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    }

    /** Zwraca parametry opisujące pozycje obiektu
     *
     */
    public Node getView() {
        return view;
    }

    /** Funkcja sprawdzająca kolizje z innym obiektem
     *
     * @param other obiekt wzgledem ktorego sprawdzamy kolizje
     */
    public boolean isColliding(GameObject other) {
        return getView().getBoundsInParent().intersects((other.getView().getBoundsInParent()));
    }

    /** Sprawdza czy obiekt zostal zniszczony
     *
     */
    public boolean isDefeat(){
        return !exist;
    }


}
