package com.kornak;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;


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

    public void moveDown(double y) {
        setVelocity(new Point2D.Double(0, y));
    }

    public boolean isGood(){
        return isGood;
    }

    public void goodCounter(){
        goodCounterValue++;
    }
    public void badCounter(){
        badCounterValue++;
    }
    public void goodDroopedCounter(){
        goodDroppedValue++;
    }

    public static void resetCounters(){
        goodCounterValue=0;
        badCounterValue=0;
        goodDroppedValue=0;
    }

    public int getGoodCounter(){
        return goodCounterValue;
    }
    public int getBadCounter(){
        return badCounterValue;
    }
    public int getGoodDropedValue(){
        return goodDroppedValue;
    }


}
