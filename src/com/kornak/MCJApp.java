package com.kornak;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
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
    private Player player = new Player();
    public Text timeText = new Text();
    public Text textScoreDrooped = new Text();
    public Text textScoreGood = new Text();
    public Text textScoreBad = new Text();
    Image imagePause = new Image("img/pause2.png",false);
    Image imageRestart = new Image("img/pause2.png",false);
    Image imageExit = new Image("img/pause2.png",false);


    Label popUpLabel = new Label();


    BackgroundImage myBi= new BackgroundImage(new Image("img/popUpBackground.jpg",popUpLabel.getMaxWidth(),popUpLabel.getHeight(),false,true),
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
        }
        else {
            isPaused=false;
            timeOld= System.currentTimeMillis()-varTime;
            hidePopUp();
        }
    }

    private void restart() {
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

    void showPopUp(){
        if(!popUpLabel.isVisible()) {
            UIGenerator ui = new UIGenerator();
            popUpLabel.setText(ui.getTip());
            Image image = new Image(ui.getImageUrl(),false);
           // popUpLabel.setGraphic(new ImageView(image));
            popUpLabel.setVisible(true);
            for (Food food : foodList) {
                if (popUpLabel.getBoundsInParent().intersects((food.getView().getBoundsInParent()))) {
                    food.setVisibility(false);
                }
            }
        }
    }
    void hidePopUp(){
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
                    food.goodDroopedCounter();
                    
                    if(food.getGoodDropedValue()>3){
                        textScoreDrooped.setFill(Color.RED);
                        defeat=true;
                    }
                    textScoreDrooped.setText("Wasted: " + String.valueOf(food.getGoodDropedValue() + " /3"));
                }
                food.setFlying(false);
                root.getChildren().removeAll(food.getView());
            }
        }


        foodList.removeIf(GameObject::isDefeat);

        foodList.forEach(GameObject::update);
        player.update();

        if (Math.random() < (double)timeNew/1000+0.01 && foodList.size()<timeNew/10+2 && !isPaused && !defeat){
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
        topPanel.getChildren().add(textScoreDrooped);
        topPanel.getChildren().add(textScoreBad);
        topPanel.getChildren().add(textScoreGood);
        topPanel.getChildren().add(sidePanel);
        topPanel.setSpacing(40);
        topPanel.setAlignment(Pos.CENTER);
        root.getChildren().add(topPanel);


       /* timeText.setX(10);
        timeText.setY(28);*/
        timeText.setFont(new Font("Georgia", 28));
        //root.getChildren().add(timeText);

        /*textScoreDrooped.setX(220);
        textScoreDrooped.setY(28);*/
        textScoreDrooped.setFont(new Font("Georgia", 28));
        textScoreDrooped.setText("Wasted: 0/3");
       /* root.getChildren().add(textScoreDrooped);*/

        /*textScoreGood.setX(370);
        textScoreGood.setY(28);*/
        textScoreGood.setFont(new Font("Georgia", 28));
        textScoreGood.setText("Good food: 0");
       /* root.getChildren().add(textScoreGood);*/

        /*textScoreBad.setX(555);
        textScoreBad.setY(28);*/
        textScoreBad.setFont(new Font("Georgia", 28));
        textScoreBad.setText("Bad food: 0");
        /*root.getChildren().add(textScoreBad);*/

        popUpLabel.setTranslateX(10);
        popUpLabel.setTranslateY(300);
        //popUpLabel.setGraphicTextGap(4);
        popUpLabel.setPrefSize(400,200);
        popUpLabel.setBackground(new Background(myBi));
        root.getChildren().add(popUpLabel);
        popUpLabel.setVisible(false);

        root.setBackground(new Background(myBi));


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
        sidePanel.setMaxWidth(500);

        root.getChildren().add(sidePanel);

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
    public static void main(String[] args) {
        launch(args);
    }
}
