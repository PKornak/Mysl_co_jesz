package com.kornak;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MCJApp extends Application{

    private Pane root;

    private List<Food> foodList = new ArrayList<>();
    private Player player;

    private Parent createContent(){
        root = new Pane();
        root.setPrefSize(1200,900);

        player = new Player();
        player.setVelocity(new Point2D.Double(0,0));
        addGameObject(player,450,860);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();

            }
        };
        timer.start();

        return root;
    }

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
        for (Food food : foodList) {
            if (food.isColliding(player)) {
                food.setFlying(false);
            }
        }

        foodList.removeIf(GameObject::isDefeat);

        foodList.forEach(GameObject::update);
        player.update();

        if (Math.random() < 0.03 && foodList.size()<7){
            addFood(new Food(), Math.random() * root.getPrefWidth(), 10);
        //    foodList.stream
        //            .filter(GameObject::isFlying)
        //            .foreach(x -> x.moveDown(10));
        }

    }

    @Override
    public void start(Stage stage) throws Exception {

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
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
