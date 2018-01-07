package com.kornak;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;


public class Food extends GameObject{
    private boolean isGood;

    Food() {
        super(new Circle(15,15,15, Color.rgb(255,0,0)));
    }

    Food(int a, int b, boolean isGood) {
        //super(new Circle(15,15,25, Color.rgb(a,b,0)));
        super();
        Image image;
        this.isGood=isGood;
        if(isGood)
            image = new Image("carrot.png",false);
        else
            image = new Image("burger.png",false);
        Circle circle = new Circle(15,15,25, Color.rgb(a,b,0));
        circle.setFill(new ImagePattern(image));
        super.setView(circle);
    }

    // jak uzaleznic od konstruktora to gowno

    static int badcounter;
    static int goodcounter;

    public void moveDown(int y) {
        setVelocity(new Point2D.Double(0, y));
    }

    public boolean isGood(){
        return isGood;
    }

    public void goodCounter(){
        goodcounter++;
        System.out.println("Dobre kuleczki: " + goodcounter);
    }
    public void badCounter(){
        badcounter++;
        System.out.println("Złe kuleczki: " + badcounter);
    }

    public int getGoodCounter(){
        return goodcounter;
    }
    public int getBadCounter(){
        return badcounter;
    }


}
