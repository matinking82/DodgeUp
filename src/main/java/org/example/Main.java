package org.example;

import org.example.Services.GameRecordDbServices;
import org.example.gameObjects.Block;
import org.example.gameObjects.Human;
import org.example.gameObjects.interfaces.IFallingObject;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class Main extends PApplet{

    public static Human human;
    public static boolean gameOver = false;
    public static boolean pause = false;
    private List<IFallingObject> fallingObjects;
    private int points = 0;
    private static int height=700;
    private static int width = 400;
    public static PApplet processing;
    public static boolean canAdd = true;
    public static int lastBlockX = 0;
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
                    Block b = new Block((int) (0.13*width),(int)(0.4*width),2+points/20);
                    fallingObjects.add(b);
                    canAdd = false;
                }
                for (int i = 0;i<fallingObjects.size();i++){
                    IFallingObject obj = fallingObjects.get(i);
                    if (obj.Fall()){
                        fallingObjects.remove(obj);
                        i--;
                        points++;
                    }
                }


            drawButton("Pause",5,5,100,50,20);
                fill(0);
            text("Points: "+points,width-100,15);
        }else {
            if (gameOver){
                background(0);
                textSize(40);
                fill(255, 153, 0);
                text("Game Over", (float) (0.5*width),(int) (0.2*height)+70);
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
        exit();
    }

    private void restart() {
        fallingObjects = new ArrayList<IFallingObject>();
        pause=false;
        gameOver = false;
        canAdd = true;
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
}