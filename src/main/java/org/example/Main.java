package org.example;

import org.example.gameObjects.Block;
import org.example.gameObjects.Human;
import org.example.gameObjects.interfaces.IFallingObject;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class Main extends PApplet{

    private Human human;
    private List<IFallingObject> fallingObjects;
    private static int height=700;
    private static int width = 400;
    public static PApplet processing;
    public static boolean canAdd = true;
    public static int lastBlockX = 0;
    public static void main(String[] args) {
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
        size(400,700);
    }

    @Override
    public void draw() {
        background(255);
        human.moveObject();
        human.showObject();
        if (canAdd){
            Block b = new Block((int) (0.13*width),(int)(0.4*width),2);
            fallingObjects.add(b);
            canAdd = false;
        }
        for (int i = 0;i<fallingObjects.size();i++){
            IFallingObject obj = fallingObjects.get(i);
            if (obj.Fall()){
                fallingObjects.remove(obj);
                i--;
            }
        }
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }
}