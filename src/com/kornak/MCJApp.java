package com.kornak;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    private double oldVar=-100;

    private Pane root;

    private List<Food> foodList = new ArrayList<>();
    private Player player;
    public Text timeText = new Text();
    public Text textScoreGood = new Text();
    public Text textScoreBad = new Text();
    public Button buttonPause = new Button();
    public Button buttonRestart = new Button();
    public Button buttonExit = new Button();
    Image imagePause = new Image("pause2.png",false);
    Image imageRestart = new Image("pause2.png",false);
    Image imageExit = new Image("pause2.png",false);

    private boolean isPaused=false;
    private long varTime=0;
    private long timeOld = System.currentTimeMillis();
    private long timeNew;

    private void pause(){
        if(!isPaused){
            isPaused=true;
            varTime = timeNew*1000;
            timeNew=0;
        }
        else {
            isPaused=false;
            timeOld= System.currentTimeMillis()-varTime;
        }
    }

    private void restart() {
        isPaused=false;
        timeOld=System.currentTimeMillis();
        Food.resetCounters();
        root.getChildren().removeAll(player.getView());
        for (Food food : foodList){
            food.setFlying(false);
            root.getChildren().removeAll(food.getView());
        }

        player.setVelocity(new Point2D.Double(0,0));
        addGameObject(player,362,740);

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
        if (!isPaused) {
            timeNew = (System.currentTimeMillis() - timeOld) / 1000;
            timeText.setText("Time: " + String.valueOf(timeNew));
        }
        else
            timeText.setText("Time: PAUSED");



        for (Food food : foodList) {
            if(isPaused){
                food.moveDown(0);
            }
            else
                food.moveDown(timeNew/15+4);

            if (food.isColliding(player)) {
                food.setFlying(false);
                root.getChildren().removeAll(food.getView());
                if(food.isGood()) {
                    food.goodCounter();
                    textScoreGood.setText("Carrots: " + String.valueOf(food.getGoodCounter()));
                }
                else {
                    food.badCounter();
                    textScoreBad.setText("Burgers: " + String.valueOf(food.getBadCounter()));
                }
            }
            else if (food.getView().getBoundsInParent().getMinY() >= gameScreenHeight ) {
                if(food.isGood())
                    food.goodDropedCounter();
                food.setFlying(false);
                root.getChildren().removeAll(food.getView());
            }
        }


        foodList.removeIf(GameObject::isDefeat);

        foodList.forEach(GameObject::update);
        player.update();

        if (Math.random() < (double)timeNew/1000+0.01 && foodList.size()<timeNew/10+2 && !isPaused){
            boolean isGood;
            double var;
            if(Math.random()<0.2)
                isGood=false;
            else
                isGood=true;

            do {
                var = ((Math.random() * (root.getPrefWidth() - 450)) + 10);
            }
            while(abs(oldVar-var) <=50);
            oldVar = var;


            addFood(new Food(isGood), var, 50);

         }

    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Myśl co jesz");
        stage.setScene(new Scene(createContent()));

        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT && !isPaused) {
                player.moveLeft();
            }
            else if (e.getCode() == KeyCode.RIGHT  && !isPaused) {
                player.moveRight();
            }
        });
        stage.getScene().setOnKeyReleased(f -> {
            if (f.getCode() == KeyCode.LEFT || f.getCode() == KeyCode.RIGHT) {
                player.stop();
            }
        });

        HBox topPanel = new HBox();
        VBox sidePanel = new VBox();

        topPanel.getChildren().add(timeText);
        topPanel.getChildren().add(textScoreBad);
        topPanel.getChildren().add(textScoreGood);
        topPanel.getChildren().add(sidePanel);
        root.getChildren().add(topPanel);

        timeText.setX(10);
        timeText.setY(28);
        timeText.setFont(new Font("Georgia", 28));
        root.getChildren().add(timeText);

        textScoreGood.setX(140);
        textScoreGood.setY(28);
        textScoreGood.setFont(new Font("Georgia", 28));
        root.getChildren().add(textScoreGood);

        textScoreBad.setX(360);
        textScoreBad.setY(28);
        textScoreBad.setFont(new Font("Georgia", 28));
        root.getChildren().add(textScoreBad);



        ImageView b1 = new ImageView();
        ImageView b2 = new ImageView();
        ImageView b3 = new ImageView();
        b1.setImage(imageRestart);
        b1.setOnMousePressed(e -> pause());
        b2.setImage(imageRestart);
        b2.setOnMousePressed(e -> restart());
        b3.setImage(imageRestart);
        b3.setOnMousePressed(e -> stage.close());


        sidePanel.setTranslateX(724);
        sidePanel.getChildren().add(b1);
        sidePanel.getChildren().add(b2);
        sidePanel.getChildren().add(b3);
        sidePanel.setMaxWidth(500);

        root.getChildren().add(sidePanel);

        stage.show();
        stage.setResizable(false);
    }

    private Parent createContent() throws IOException {
        root = new Pane();
        root.setPrefSize(gameScreenWidth,gameScreenHeight);

        player = new Player();
        player.setVelocity(new Point2D.Double(0,0));
        addGameObject(player,362,gameScreenHeight-65);

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
