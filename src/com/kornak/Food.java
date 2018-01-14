package com.kornak;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;

/**
 * Klasa odpowiedzialna za spadające obiekty
 */
public class Food extends GameObject{
    private boolean isGood;

    Food(boolean isGood) {
        super();
        Image image;
        this.isGood=isGood;
        if(isGood){
            int rand = (int)(Math.random() * 5);
            switch (rand) {
                case 0:{
                    image = new Image("img/carrot.png",false);
                    break;
                }
                case 1:{
                    image = new Image("img/apple.png",false);
                    break;
                }
                case 2:{
                    image = new Image("img/broccoli.png",false);
                    break;
                }
                case 3:{
                    image = new Image("img/pear.png",false);
                    break;
                }
                case 4:{
                    image = new Image("img/carrot2.png",false);
                    break;
                }
                default:{
                    image = new Image("carrot.png",false);
                    System.out.println("Something goes wrong");
                    break;
                }

            }
        }
        else {
            int rand = (int)(Math.random() * 2);
            switch (rand) {
                case 0:{
                    image = new Image("img/burger.png",false);
                    break;
                }
                case 1:{
                    image = new Image("img/pizza.png",false);
                    break;
                }
                default:{
                    image = new Image("burger.png",false);
                    System.out.println("Something goes wrong");
                    break;
                }

            }
        }
        Circle circle = new Circle(15,15,30, Color.rgb(0,0,0));
        circle.setFill(new ImagePattern(image));
        super.setView(circle);
    }


    private static int badCounterValue;
    private static int goodCounterValue;
    private static int goodDroppedValue;

    /**
     * Nadaje prędkość opadania obiektom
     * @param y wartość prędkości opadania
     */
    public void moveDown(double y) {
        setVelocity(new Point2D.Double(0, y));
    }

    /**
     *  Zwraca czy obiekt jest zdrowy, czy nie
     */
    public boolean isGood(){
        return isGood;
    }

    /**
     * Zliczanie zebranych zdrowych obiektów
     */
    public void goodCounter(){
        goodCounterValue++;
    }

    /**
     * Zliczanie zebranych niezdrowych obiektów
     */
    public void badCounter(){
        badCounterValue++;
    }

    /**
     * Zliczanie zdrowych obiektów, które wypadł za obszar gry
     */
    public void goodDroppedCounter(){
        goodDroppedValue++;
    }

    /**
     * Resetuje wartość wszystkich liczników obiektów
     */
    public static void resetCounters(){
        goodCounterValue=0;
        badCounterValue=0;
        goodDroppedValue=0;
    }

    /**
     * Zwraca ilość zebranych zdrowych obiektów
     */
    public int getGoodCounter(){
        return goodCounterValue;
    }

    /**
     * Zwraca ilość zebranych niezdrowych obiektów
     */
    public int getBadCounter(){
        return badCounterValue;
    }

    /**
     * Zwraca ilość niezebranych zdrowych obiektów
     */
    public int getGoodDroppedValue(){
        return goodDroppedValue;
    }


}
