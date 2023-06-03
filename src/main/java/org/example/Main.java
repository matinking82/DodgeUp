package org.example;

import org.example.Models.GameRecord;
import org.example.Services.GameRecordDbServices;
import org.example.gameObjects.Block;
import org.example.gameObjects.Heart;
import org.example.gameObjects.Human;
import org.example.gameObjects.Shield;
import org.example.gameObjects.interfaces.IFallingObject;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Main extends PApplet{

    public static Human human;
    public static boolean gameOver = false;
    public static boolean pause = true;
    private List<IFallingObject> fallingObjects;
    private static int height=700;
    private static int width = 400;
    public static PApplet processing;
    public static boolean canAdd = true;
    public static int points = 0;
    public static int lastBlockX = 0;
    public static int lives = 3;
    public static int shield = 0;
    public static int speed = 2;
    public static boolean itemCheck = false;
    private static GameRecordDbServices db;
    public static void main(String[] args) {
        db = new GameRecordDbServices();
        PApplet.main("org.example.Main");
    }

    @Override
    public void setup() {
        processing = this;
        human = new Human(0,(int)(0.10*height),(int)(0.10*width));
        fallingObjects = new ArrayList<IFallingObject>();
        background(240);

    }

    @Override
    public void settings() {
        size(width,height);
    }

    @Override
    public void draw() {
        if (!pause){
            background(255);
                human.moveObject();
                human.showObject();
                if (canAdd){
                    Block b = new Block((int) (0.13*width),(int)(0.4*width));
                    fallingObjects.add(b);
                    if (!itemCheck && points%20==0&&points!=0) {
                        if (new Random().nextInt(3)==0){
                            Heart h = new Heart((int) (0.06 * width));
                            fallingObjects.add(h);
                        }else if(new Random().nextInt(3)==1){
                            Shield s = new Shield((int) (0.06 * width));
                            fallingObjects.add(s);
                        }
                        itemCheck = true;
                    }else{
                        itemCheck = false;
                    }
                    canAdd = false;
                }
                for (int i = 0;i<fallingObjects.size();i++){
                    IFallingObject obj = fallingObjects.get(i);
                    if (obj.Fall()){
                        fallingObjects.remove(obj);
                        i--;
                        if (obj instanceof Block){
                            points++;
                            if (points%20 == 0){
                                speed++;
                            }
                        }
                    }
                }


            drawButton("Pause",5,5,100,50,20);
                fill(255,0,0);
            text("Lives: "+lives,width-100,15);
            fill(20,255,150);
            text("Shield: "+shield,width-200,15);
            fill(0);
            text("Points: "+points,width-100,45);
        }else {
            if (gameOver){
                background(0);
                textSize(40);
                fill(255, 153, 0);
                text("Game Over", (float) (0.5*width),(int) (0.2*height));
                textSize(25);
                fill(255);
                text("Your record: "+points, (float) (0.5*width),(int) (0.2*height)+70);
                drawButton("Restart",(int) (0.3*width),(int) (0.2*height)+140,(int) (0.4*width),50,20);
                drawButton("Quit",(int) (0.3*width),(int) (0.2*height)+210,(int) (0.4*width),50,20);

            }else {
                fill(200);
                rect((float) (width*0.1),(float) (height*0.2),(float) (width*0.8),230);
                drawButton("Resume",(int) (0.3*width),(int) (0.2*height)+20,(int) (0.4*width),50,20);
                drawButton("Restart",(int) (0.3*width),(int) (0.2*height)+90,(int) (0.4*width),50,20);
                drawButton("Quit",(int) (0.3*width),(int) (0.2*height)+160,(int) (0.4*width),50,20);
            }
        }
    }

    @Override
    public void mouseClicked() {
        if (!pause){
            if (mouseX>5&&mouseX<105&&mouseY>5&&mouseY<55){
                pause = true;
            }
        }else {
            if (gameOver){
                if (mouseX>0.3*width&&mouseX<0.7*width&&mouseY>(0.2*height)+140&&mouseY<(0.2*height)+190){
                    restart();
                }else if (mouseX>0.3*width&&mouseX<0.7*width&&mouseY>(0.2*height)+210&&mouseY<(0.2*height)+260){
                    quit();
                }
            }else {
                if (mouseX>0.3*width&&mouseX<0.7*width&&mouseY>(0.2*height)+20&&mouseY<(0.2*height)+70){
                    if (fallingObjects.size()==0){
                        List<Block> blocks = db.LoadGame();
                        for (Block b :
                                blocks) {
                            fallingObjects.add(b);
                            if (!b.check){
                                canAdd = false;
                            }
                        }
                    }
                    pause = false;
                }else if (mouseX>0.3*width&&mouseX<0.7*width&&mouseY>(0.2*height)+90&&mouseY<(0.2*height)+140){
                    restart();
                }else if (mouseX>0.3*width&&mouseX<0.7*width&&mouseY>(0.2*height)+160&&mouseY<(0.2*height)+210){
                    quit();
                }
            }
        }

    }

    private void quit() {
        db.deleteAllBlocks();
        if (!gameOver){
            for (IFallingObject obj :
                    fallingObjects) {
                if (obj instanceof Block){
                    db.addBlock((Block) obj);
                }
            }
            db.SaveGame();
        }
        exit();
    }

    private void restart() {
        fallingObjects = new ArrayList<IFallingObject>();
        pause=false;
        gameOver = false;
        canAdd = true;
        lives = 3;
        shield = 0;
        itemCheck = false;
        speed = 2;
        points = 0;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    private void drawButton(String label, int x, int y,int buttonWidth,int buttonHeight,int fontSize) {
        if (mouseX >= x && mouseX <= x + buttonWidth && mouseY >= y && mouseY <= y + buttonHeight) {
            fill(150);
        } else {
            fill(200);
        }
        rect(x, y, buttonWidth, buttonHeight);
        fill(0);
        textSize(fontSize);
        textAlign(CENTER, CENTER);
        text(label, x + (buttonWidth/2), y + (buttonHeight/2));
    }
    public static void hitBlock(){
        if (shield>0){
            shield--;
        }else {
            lives--;
            if (lives <= 0){
                gameLost();
            }
        }
    }

    public static void hitHeart(){
        lives++;
    }
    public static void hitShield(){
        shield++;
    }
    private static void gameLost(){
        gameOver = true;
        pause = true;
        GameRecord gr = new GameRecord((new Date()).toString(),points);
        db.add(gr);
    }
}