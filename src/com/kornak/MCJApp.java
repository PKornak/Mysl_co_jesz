package com.kornak;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Klasa główna gry
 */
public class MCJApp extends Application {
    private final int gameScreenWidth = 1024;
    private final int gameScreenHeight = 768;
    private double oldVar=-100;

    private Pane root;

    private List<Food> foodList = new ArrayList<>();
    private Player player = new Player();

    private Text timeText = new Text();
    private Text textScoreDrooped = new Text();
    private Text textScoreGood = new Text();
    private Text textScoreBad = new Text();
    private Text textInfo = new Text();

    private Image imagePause = new Image("img/pause.png",false);
    private Image imageRestart = new Image("img/restart.png",false);
    private Image imageExit = new Image("img/exit.png",false);

    private Label popUpLabel = new Label();


    private BackgroundImage myBi= new BackgroundImage(new Image("img/popUpBg.png",popUpLabel.getMaxWidth(),popUpLabel.getHeight(),false,true),
            BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER,
            BackgroundSize.DEFAULT);
    private BackgroundImage myBi2= new BackgroundImage(new Image("img/Bg.png",popUpLabel.getMaxWidth(),popUpLabel.getHeight(),false,true),
            BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER,
            BackgroundSize.DEFAULT);


    private boolean isPaused=false;
    private boolean defeat =false;
    private boolean timerStopped =true;
    private long varTime=0;
    private long timeOld = System.currentTimeMillis();
    private long timeNew;

    private void pause(){
        if(!isPaused || defeat){
            isPaused=true;
            varTime = timeNew*1000;
            timeNew=0;
            textInfo.setText("Game PAUSED");
        }
        else {
            isPaused=false;
            timeOld= System.currentTimeMillis()-varTime;
            hidePopUp();
            textInfo.setText("");
        }
    }

    private void restart() {
        textInfo.setText("");
        isPaused=false;
        defeat =false;
        timerStopped = false;
        timeOld=System.currentTimeMillis();
        hidePopUp();
        Food.resetCounters();
        root.getChildren().removeAll(player.getView());
        for (Food food : foodList){
            food.setFlying(false);
            root.getChildren().removeAll(food.getView());
        }
        textScoreDrooped.setFill(Color.BLACK);
        textScoreDrooped.setText("Wasted: 0/3");
        textScoreBad.setText("Bad food: 0");
        textScoreGood.setText("Good food: 0");
        player.setVelocity(new Point2D.Double(0,0));
        addGameObject(player,362,gameScreenHeight-65);

    }

    private void showPopUp(){
        if(!popUpLabel.isVisible()) {
            popUpController ui = new popUpController();
            popUpLabel.setText(ui.getTip());

            popUpLabel.setVisible(true);
            for (Food food : foodList) {
                if (popUpLabel.getBoundsInParent().intersects((food.getView().getBoundsInParent()))) {
                    food.setVisibility(false);
                }
            }
        }
    }
    private void hidePopUp(){
        popUpLabel.setVisible(false);
        for(Food food : foodList) {
            if(popUpLabel.getBoundsInParent().intersects((food.getView().getBoundsInParent()))){
                food.setVisibility(true);
            }
        }
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
        if (!isPaused && !defeat) {
            timeNew = (System.currentTimeMillis() - timeOld) / 1000;
            timeText.setText("Time: " + String.valueOf(timeNew));
        }
        else if (defeat){
            textInfo.setText("YOU LOSE");
            if(timerStopped){
                timeNew = (System.currentTimeMillis() - timeOld) / 1000;
                timerStopped =false;
            }
            timeText.setText("Time: " + String.valueOf(timeNew));
        }
        else
            timeText.setText("Time: PAUSED");

        if(isPaused || defeat) showPopUp();


        for (Food food : foodList) {
            if(isPaused || defeat){
                food.moveDown(0);
            }
            else
                food.moveDown(timeNew/15+4);

            if (food.isColliding(player)) {
                food.setFlying(false);
                root.getChildren().removeAll(food.getView());
                if(food.isGood()) {
                    food.goodCounter();
                    textScoreGood.setText("Good food: " + String.valueOf(food.getGoodCounter()));
                }
                else {
                    food.badCounter();
                    textScoreBad.setText("Bad food: " + String.valueOf(food.getBadCounter()));
                }
            }
            else if (food.getView().getBoundsInParent().getMinY() >= gameScreenHeight ) {
                if(food.isGood()) {
                    food.goodDroppedCounter();
                    
                    if(food.getGoodDroppedValue()>3){
                        textScoreDrooped.setFill(Color.RED);
                        defeat=true;
                    }
                    textScoreDrooped.setText("Wasted: " + String.valueOf(food.getGoodDroppedValue() + " /3"));
                }
                food.setFlying(false);
                root.getChildren().removeAll(food.getView());
            }
        }


        foodList.removeIf(GameObject::isDefeat);

        foodList.forEach(GameObject::update);
        player.update();

        if (Math.random() < (double)timeNew/1000+0.01 && foodList.size()<timeNew/10+2 && !isPaused && !defeat){
            double var;
            boolean isGood = !(Math.random() < 0.2);

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
            if(!isPaused && !defeat) {
                if (e.getCode() == KeyCode.LEFT) {
                    player.moveLeft();
                } else if (e.getCode() == KeyCode.RIGHT) {
                    player.moveRight();
                }
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
       // topPanel.getChildren().add(textScoreDrooped);

        topPanel.setSpacing(150);
        root.getChildren().add(topPanel);

        timeText.setFont(new Font("Georgia", 28));

        textScoreDrooped.setTranslateX(250);
        textScoreDrooped.setTranslateY(28);
        textScoreDrooped.setFont(new Font("Georgia", 28));
        textScoreDrooped.setText("Wasted: 0/3");
        root.getChildren().add(textScoreDrooped);

        textScoreGood.setFont(new Font("Georgia", 28));
        textScoreGood.setText("Good food: 0");

        textScoreBad.setFont(new Font("Georgia", 28));
        textScoreBad.setText("Bad food: 0");

        textInfo.setFont(new Font("Georgia", 28));


        ImageView b1 = new ImageView();
        ImageView b2 = new ImageView();
        ImageView b3 = new ImageView();
        b1.setImage(imagePause);
        b1.setOnMousePressed(e -> pause());
        b2.setImage(imageRestart);
        b2.setOnMousePressed(e -> restart());
        b3.setImage(imageExit);
        b3.setOnMousePressed(e -> stage.close());

        sidePanel.setTranslateX(724);
        sidePanel.getChildren().add(b1);
        sidePanel.getChildren().add(b2);
        sidePanel.getChildren().add(b3);
        sidePanel.getChildren().add(textInfo);
        sidePanel.getChildren().add(textScoreGood);
        sidePanel.getChildren().add(textScoreBad);
        sidePanel.setMaxWidth(500);

        root.getChildren().add(sidePanel);


        popUpLabel.setTranslateX(50);
        popUpLabel.setTranslateY(300);
        popUpLabel.setPrefSize(590,200);
        popUpLabel.setBackground(new Background(myBi));
        popUpLabel.setVisible(false);
        popUpLabel.setWrapText(true);
        popUpLabel.setFont(Font.font("Georgia", 20));
        popUpLabel.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().add(popUpLabel);
        root.setBackground(new Background(myBi2));

        stage.show();
        stage.setResizable(false);
    }

    private Parent createContent() throws IOException {

        root = new Pane();
        root.setPrefSize(gameScreenWidth,gameScreenHeight);

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
}
