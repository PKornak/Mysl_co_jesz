package com.kornak;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class MCJApp extends Application {
    private final int gameScreenWidth = 1024;
    private final int gameScreenHeight = 768;
    private int foodAmount = 10;
    private double oldVar=-100;

    private Pane root;

    private List<Food> foodList = new ArrayList<>();
    private Player player;
    public Text timeText = new Text();
    public Text textScrooreGood = new Text();
    public Text textScrooreBad = new Text();


    long timeOld = System.currentTimeMillis();

    private void addFood(Food food, double x, double y) {
        foodList.add(food);
        addGameObject(food, x, y);
    }

    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        root.getChildren().add(object.getView());
    }

    private void onUpdate(){
        long timeNew = (System.currentTimeMillis()-timeOld)/1000;
        timeText.setText("Time: " + String.valueOf(timeNew));
        textScrooreGood.setText("Marchewki: " + String.valueOf(Food.goodcounter));
        textScrooreBad.setText("Burgery: " + String.valueOf(Food.badcounter));

        for (Food food : foodList) {
            if (food.isColliding(player)) {
                food.setFlying(false);
                root.getChildren().removeAll(food.getView());
                if(food.isGood())
                    food.goodCounter();
                else
                    food.badCounter();
            }
            else if (food.getView().getBoundsInParent().getMinY() >= gameScreenHeight ) {
                food.setFlying(false);
            }
        }


        foodList.removeIf(GameObject::isDefeat);

        foodList.forEach(GameObject::update);
        player.update();


        if (Math.random() < 0.1 && foodList.size()<foodAmount){
            int a,b;
            boolean isBad;
            double var;
            if(Math.random()<0.2) {
                a=255;
                b=0;
                isBad=false;
            }
            else {
                a=0;
                b=255;
                isBad=true;
            }
            do {
                var = ((Math.random() * (root.getPrefWidth() - 250)) + 10);
            }
            while(abs(oldVar-var) <=50);
            oldVar = var;

            addFood(new Food(a,b,isBad), var, 50);
            foodList.stream()
                    .filter(GameObject::isFlying)
                    .forEach(x -> x.moveDown(5));
        }

    }

    @Override
    public void start(Stage stage) throws Exception {

        /*
        //primaryStage.setScene(new Scene(root, 300, 275));
         */
//        TODO przemyśleć dodanie switch(case)

        stage.setTitle("Myśl co jesz");
        stage.setScene(new Scene(createContent()));
        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                player.moveLeft();
            }
            else if (e.getCode() == KeyCode.RIGHT) {
                player.moveRight();
            }
        });
        stage.getScene().setOnKeyReleased(f -> {
            if (f.getCode() == KeyCode.LEFT || f.getCode() == KeyCode.RIGHT) {
                player.stop();
            }
        });
        timeText.setX(10);
        timeText.setY(28);
        timeText.setFont(new Font("Georgia", 28));
        root.getChildren().add(timeText);

        textScrooreGood.setX(140);
        textScrooreGood.setY(28);
        textScrooreGood.setFont(new Font("Georgia", 28));
        root.getChildren().add(textScrooreGood);

        textScrooreBad.setX(360);
        textScrooreBad.setY(28);
        textScrooreBad.setFont(new Font("Georgia", 28));
        root.getChildren().add(textScrooreBad);

        stage.show();
        stage.setResizable(false);
    }

    private Parent createContent() throws IOException {
        root = new Pane();
        root.setPrefSize(gameScreenWidth,gameScreenHeight);

        player = new Player();
        player.setVelocity(new Point2D.Double(0,0));
        addGameObject(player,512,740);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();

            }
        };
        timer.start();

        return root;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
